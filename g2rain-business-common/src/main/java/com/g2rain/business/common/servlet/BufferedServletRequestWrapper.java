package com.g2rain.business.common.servlet;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.Part;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;

import com.alibaba.fastjson.JSONObject;
import com.g2rain.business.common.utils.MediaTypeUtil;

/**
 * @ClassName BufferedServletRequestWrapper
 * @Description 重用输入流封装
 *
 * @author sunhaojie sunhaojie@kingsoft.com
 * @date 2017年1月7日 下午3:33:38
 */
public class BufferedServletRequestWrapper extends HttpServletRequestWrapper {

    private static Logger logger = LoggerFactory.getLogger(BufferedServletRequestWrapper.class);

	private byte[] buffer;
	
	private Map<String, String> extendParams = new HashMap<String, String>();
	
	public void addParam(String key, String value) {
		String methodName = this.getMethod();
		if (HttpMethod.GET.name().equals(methodName) || HttpMethod.DELETE.name().equals(methodName)) {
			this.extendParams.put(key, value);
		} else {
			String content = new String(buffer);
			JSONObject contentJsonObject = JSONObject.parseObject(content);
			contentJsonObject.put(key, value);
			this.buffer = contentJsonObject.toJSONString().getBytes();
		}
	}

	public BufferedServletRequestWrapper(HttpServletRequest request)
			throws IOException {
		super(request);
		InputStream is = request.getInputStream();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte buff[] = new byte[1024];
		int read;
		while ((read = is.read(buff)) > 0) {
			baos.write(buff, 0, read);
		}
		this.buffer = baos.toByteArray();
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		return new BufferedServletInputStream(this.buffer);
	}

    /**
     * 获取请求header
     *
     * @Title getHeader
     * @return String
     *
     * @author sunhaojie@kingsoft.com
     * @date 2017年8月11日 下午3:58:09
     */
    public String getHeader() {
        Enumeration<String> headerNames = this.getHeaderNames();
        Map<String, String> headers = new HashMap<String, String>();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            headers.put(name, this.getHeader(name));
        }
        return JSONObject.toJSONString(headers);
    }
    
    public Map<String, String> getHeaderMap() {
        Enumeration<String> headerNames = this.getHeaderNames();
        Map<String, String> headers = new HashMap<String, String>();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            headers.put(name, this.getHeader(name));
        }
        return headers;
    }

    /**
     * 获取参数
     *
     * @Title getParam
     * @return String
     *
     * @author sunhaojie@kingsoft.com
     * @date 2017年8月11日 下午4:06:52
     */
    public String getParam() {
        String method = this.getMethod();
        String paramString = "";
        if("get".equalsIgnoreCase(method) || "delete".equalsIgnoreCase(method)) {
            Map<String, String> params = new HashMap<String, String>();
            Enumeration<String> parameterNames = this.getParameterNames();
            while (parameterNames.hasMoreElements()) {
                String parameterName = parameterNames.nextElement();
                String parameterValue = this.getParameter(parameterName);
                params.put(parameterName, parameterValue);
            }
            paramString = JSONObject.toJSONString(params);
        } else {

			String contentType = this.getContentType();
			if (MediaTypeUtil.isMultipartFormData(contentType)) {
				Enumeration<String> parameterNames = this.getParameterNames();
				Map<String, String> params = new HashMap<String, String>();
				while (parameterNames.hasMoreElements()) {
					String parameterName = parameterNames.nextElement();
					String parameterValue = this.getParameter(parameterName);
					params.put(parameterName, parameterValue);
				}
				try {
					Collection<Part> parts = this.getParts();
					for (Part p : parts) {
						if (!MediaTypeUtil.isText(p.getContentType())) {
							params.put(p.getName(), p.getSubmittedFileName());
						}
					}
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				} catch (ServletException e) {
					logger.error(e.getMessage(), e);
				}
				paramString = JSONObject.toJSONString(params);
			} else {
				StringBuffer sb = new StringBuffer("");
				BufferedReader br = null;
				try {
					br = new BufferedReader(new InputStreamReader(this.getInputStream(), "utf-8"));
					String temp = null;
					while ((temp = br.readLine()) != null) {
						sb.append(temp);
					}
				} catch (UnsupportedEncodingException e) {
					logger.error(e.getMessage(), e);
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
				paramString = sb.toString();
			}
        }

        return paramString;
    }

	public Map<String, Object> getParamMap() {
		String param = this.getParam();
		if (StringUtils.isNotBlank(param)) {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			JSONObject jsonObject = JSONObject.parseObject(param);
			for (String key : jsonObject.keySet()) {
				paramMap.put(key, jsonObject.get(key));
			}

			return paramMap;
		}
		return null;
	}

	@Override
	public String getParameter(String name) {
		String value = super.getParameter(name);
		if (StringUtils.isBlank(value)) {
			value = this.extendParams.get(name);
		}
		
		return value;
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		Map<String, String[]> parameterMap = super.getParameterMap();
		
		if(MapUtils.isNotEmpty(extendParams)) {
			for(String key : extendParams.keySet()) {
				String value = this.extendParams.get(key);
				String[] values = new String[]{value};
				parameterMap.put(key, values);
			}
		}

		return parameterMap;
	}

	@Override
	public Enumeration<String> getParameterNames() {
		Enumeration<String> parameterNames = super.getParameterNames();
		
		Vector<String> nameList = new Vector<String>();
		Set<String> names = this.extendParams.keySet();
		nameList.addAll(names);
		if(parameterNames != null) {
			while(parameterNames.hasMoreElements()) {
				String name = parameterNames.nextElement();
				nameList.add(name);
			}
		}
		
		return nameList.elements();
	}

	@Override
	public String[] getParameterValues(String name) {
		String[] values = super.getParameterValues(name);
		if (ArrayUtils.isEmpty(values) || (values.length == 1 && StringUtils.isBlank(values[0]))) {
			String value = this.extendParams.get(name);
			values = new String[]{value};
		}
		return values;
	}
}
