package com.github.sunking000.g2rain.business.gateway.filter;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.NettyWriteResponseFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;

import com.github.sunking000.g2rain.business.gateway.adapter.CoreClient;
import com.github.sunking000.g2rain.business.gateway.exception.ErrorCodeMessageBo;
import com.github.sunking000.g2rain.business.gateway.utils.JsonObjectUtil;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class ModifyResponseBodyGlobalFilter implements GlobalFilter, Ordered {

	@Autowired
	private CoreClient coreClient;
	@Autowired
	private ErrorCodeMessageBo errorCodeMessageBo;

	private Cache<String, String> organCache = CacheBuilder.newBuilder().maximumSize(100000)
			.expireAfterWrite(60, TimeUnit.MINUTES).build();

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		ServerHttpResponse response = exchange.getResponse();
		DataBufferFactory bufferFactory = response.bufferFactory();

		ServerHttpResponseDecorator decorator = new ServerHttpResponseDecorator(response) {
			@Override
			public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
				if (body instanceof Flux) {
					Flux<? extends DataBuffer> fluxBody = (Flux<? extends DataBuffer>) body;
					return super.writeWith(fluxBody.buffer().map(dataBuffers -> {
						// List<String> list = Lists.newArrayList();
						byte[] contentBytes = null;
						// gateway 针对返回参数过长的情况下会分段返回，使用如下方式接受返回参数则可避免
						for (DataBuffer dataBuffer : dataBuffers) {
							// probably should reuse buffers
							byte[] temp = new byte[dataBuffer.readableByteCount()];
							dataBuffer.read(temp);
							// 释放掉内存
							DataBufferUtils.release(dataBuffer);
							contentBytes = ArrayUtils.addAll(contentBytes, temp);
							// list.add(new String(content, StandardCharsets.UTF_8));
						}
						// 将多次返回的参数拼接起来
						String responseData = new String(contentBytes, StandardCharsets.UTF_8);

						// 重置返回参数
						String result = response(responseData);
						byte[] uppedContent = new String(result.getBytes(StandardCharsets.UTF_8),
								StandardCharsets.UTF_8).getBytes();

						// 修改后的返回参数应该重置长度，否则如果修改后的参数长度超出原始参数长度时会导致客户端接收到的参数丢失一部分
						response.getHeaders().setContentLength(uppedContent.length);

						return bufferFactory.wrap(uppedContent);
					}));
				}
				return super.writeWith(body);
			}
		};

		return chain.filter(exchange.mutate().response(decorator).build());
	}

	private String response(String result) {
		log.debug("before add message:{}", result);
		JsonObject resultObject = JsonObjectUtil.parseObject(result);
		// JSONObject baseResult = JSONObject.parseObject(result);
		if (resultObject.has("status") && resultObject.get("status").getAsInt() == 200) {
			JsonElement resultElement = resultObject.has("data") && resultObject.get("data") != null
					? resultObject.get("data")
					: resultObject.has("resultData") && resultObject.get("resultData") != null
							? resultObject.get("resultData")
							: null;
			// JSONObject data = baseResult.get("data") != null ?
			// baseResult.getJSONObject("data")
			// : baseResult.get("resultData") != null ?
			// baseResult.getJSONObject("resultData") : null;
			// Map<String, Object> map = baseResult.getData();
			if (resultElement != null) {
				if (resultElement.isJsonObject()) {
					for (Map.Entry<String, JsonElement> entry : resultElement.getAsJsonObject().entrySet()) {
						JsonElement value = entry.getValue();
						if (value == null) {
							continue;
						}
						// boolean isList = value instanceof List;
						if (value.isJsonArray()) {
							JsonArray dataJsonArray = value.getAsJsonArray();
							dataJsonArray.forEach(item -> {
								JsonObject itemData = item.getAsJsonObject();
								addToResponseOrganName(itemData);
							});

							// @SuppressWarnings("rawtypes")
							// List list = (List) value;
							// for (Object obj : list) {
							// JSONObject jsonObject = (JSONObject) obj;
							// // 机构id转组织机构名称
							// addToResponseOrganName(jsonObject);
							// }
							continue;
						} else if (value.isJsonObject()) {
							JsonObject valueJsonObject = value.getAsJsonObject();
							// JSONObject jsonObject = (JSONObject) value;
							addToResponseOrganName(valueJsonObject);
						}
					}
				}

			}
		} else {
			addMessage(resultObject);
		}

		String returnData = resultObject.toString();
		log.debug("after add message:{}", returnData);

		return returnData;
	}

	private void addMessage(JsonObject baseResult) {
		String errorCode = baseResult.get("errorCode").getAsString();
		String errorMessage = errorCodeMessageBo.getErrorMessage(errorCode);
		baseResult.addProperty("message", errorMessage);
		JsonElement subErrorsJsonElement = baseResult.get("subErrors");
		// JSONArray subErrorsJsonArray = baseResult.getJSONArray("subErrors");
		if (subErrorsJsonElement != null && !subErrorsJsonElement.isJsonNull()) {
			JsonArray subErrorsJsonArray = subErrorsJsonElement.getAsJsonArray();
			subErrorsJsonArray.forEach(item -> {
				JsonObject itemSubError = item.getAsJsonObject();
				itemSubError.addProperty("message",
						errorCodeMessageBo.getSubErrorMessage(itemSubError.get("errorCode").getAsString(),
								itemSubError.get("message").getAsString()));
			});
			// JSONArray subErrorsJsonArrayFilledMessage = new
			// JSONArray(subErrorsJsonArray.size());
			// for (int i = 0; i < subErrorsJsonArray.size(); i++) {
			// SubError item = subErrorsJsonArray.getObject(i, SubError.class);
			// item.setMessage(errorCodeMessageBo.getSubErrorMessage(item.getErrorCode()));
			// subErrorsJsonArrayFilledMessage.add(item);
			// }
			// baseResult.put("subErrors", subErrorsJsonArrayFilledMessage);
		}
	}

	private String getOrganName(String organId) {
		if (StringUtils.isNotBlank(organId)) {
			String organName = organCache.getIfPresent(organId);
			if (StringUtils.isNotEmpty(organName)) {
				return organName;
			}
			organName = coreClient.getOrganName(organId);
			if (StringUtils.isNotBlank(organName)) {
				organCache.put(organId, organName);
				return organName;
			}
		}
		return null;
	}

	private void addToResponseOrganName(JsonObject jsonObject) {
		JsonElement jsonElement = jsonObject.get("companyOrganId");
		if (jsonElement != null && !jsonElement.isJsonNull()) {
			String companyOrganId = jsonObject.get("companyOrganId").getAsString();
			String companyOrganName = getOrganName(companyOrganId);
			if (StringUtils.isNotBlank(companyOrganName)) {
				jsonObject.addProperty("companyOrganName", companyOrganName);
			} else {
				jsonObject.addProperty("companyOrganName", "");
			}
		}

		JsonElement storeJsonElement = jsonObject.get("storeOrganId");
		if (storeJsonElement != null && !storeJsonElement.isJsonNull()) {
			String storeOrganId = jsonObject.get("storeOrganId").getAsString();
			String storeOrganName = getOrganName(storeOrganId);
			if (StringUtils.isNotBlank(storeOrganName)) {
				jsonObject.addProperty("storeOrganName", storeOrganName);
			} else {
				jsonObject.addProperty("storeOrganName", "");
			}
		}
		// String companyOrganId =
		// jsonObject.get("companyOrganId").getString("companyOrganId");
		// String companyOrganName = getOrganName(companyOrganId);
		// if (StringUtils.isNotBlank(companyOrganName)) {
		// jsonObject.put("companyOrganName", companyOrganName);
		// } else {
		// jsonObject.put("companyOrganName", companyOrganId);
		// }
		// String storeOrganId = jsonObject.getString("storeOrganId");
		// String storeOrganName = getOrganName(storeOrganId);
		// if (StringUtils.isNotBlank(storeOrganName)) {
		// jsonObject.put("storeOrganName", storeOrganName);
		// } else {
		// jsonObject.put("storeOrganName", storeOrganId);
		// }
	}

	@Override
	public int getOrder() {
		return NettyWriteResponseFilter.WRITE_RESPONSE_FILTER_ORDER - 1;
	}
}