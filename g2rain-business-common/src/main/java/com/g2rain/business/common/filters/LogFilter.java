package com.g2rain.business.common.filters;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.g2rain.business.common.servlet.BufferedServletRequestWrapper;
import com.g2rain.business.common.servlet.BufferedServletResponseWrapper;
import com.g2rain.business.common.utils.CommonContextContainer;
import com.g2rain.business.common.utils.DateFormatUtil;
import com.g2rain.business.common.utils.MediaTypeUtil;

/**
 *
 * @ClassName RequestLogFilter
 * @Description 请求响应入参出参日志打印
 *
 * @author sunhaojie@kingsoft.com
 * @date 2017年8月10日 上午11:45:03
 */
public class LogFilter implements Filter {

    private static Logger logger = LoggerFactory.getLogger(LogFilter.class);

	private Set<String> excludePaths = new HashSet<>();

    /* (non-Javadoc)
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    @Override
	public void init(FilterConfig filterConfig) throws ServletException {

    }

	public void addExcludePath(String path) {
		this.excludePaths.add(path);
	}

	// public void setServletPathPatternMatcher(ServletPathPatternMatcher
	// servletPathPatternMatcher) {
	// this.servletPathPatternMatcher = servletPathPatternMatcher;
	// }

	public LogFilter() {
	}

    /* (non-Javadoc)
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    @Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
        ServletException {
		if (isExclude((HttpServletRequest) request)) {
			chain.doFilter(request, response);
			return;
		}

        long startTime = System.currentTimeMillis();
		String requestId = CommonContextContainer.getRequestId();
		// if (request instanceof HttpServletRequest) {
		// HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		// requestId = httpServletRequest.getHeader("X-POS-REQUEST-ID") == null
		// ? httpServletRequest.getHeader("x-pos-request-id")
		// : httpServletRequest.getHeader("X-POS-REQUEST-ID");
		// // Enumeration<String> headerNames = httpServletRequest.getHeaderNames();
		// // while (headerNames.hasMoreElements()) {
		// // String name = headerNames.nextElement();
		// // logger.info("name:{}, value:{}", name,
		// httpServletRequest.getHeader(name));
		// // }
		// }

		if (StringUtils.isBlank(requestId)) {
			logger.warn("未获取到requestId");
			requestId = UUID.randomUUID().toString();
			CommonContextContainer.setRequestId(requestId);
		}

        BufferedServletRequestWrapper bufferedServletRequestWrapper = null;
        String method = ((HttpServletRequest) request).getMethod();
        String path = ((HttpServletRequest) request).getServletPath();
        if (request instanceof BufferedServletRequestWrapper) {
            bufferedServletRequestWrapper = (BufferedServletRequestWrapper) request;
            String header = bufferedServletRequestWrapper.getHeader();
			String param = bufferedServletRequestWrapper.getParam();
			logger.info("requestId:{}, method:{}, path:{}, param:{}, header:{}", requestId, method, path, param,
					header);
        }
        chain.doFilter(request, response);
        BufferedServletResponseWrapper bufferedServletResponseWrapper = null;
        if (response instanceof BufferedServletResponseWrapper) {
            bufferedServletResponseWrapper = (BufferedServletResponseWrapper) response;
			String contentType = response.getContentType();
			if (MediaTypeUtil.isText(contentType)) {
				String result = bufferedServletResponseWrapper.getResult();
				result = result == null ? "NULL" : result.length() > 1000 ? "result lenght " + result.length() : result;
				logger.info("requestId:{}, result:{}", requestId, bufferedServletResponseWrapper.getResult());
			} else {
				logger.info("requestId:{}, result:{}, contentType:{}", requestId, "二进制流", contentType);
			}

        }
        long endTime = System.currentTimeMillis();
        long takedTime = endTime - startTime;
        logger.info("requestId:{}, startTime:{}, endTime:{}, takedTime:{}", requestId,
            DateFormatUtil.format(startTime),
            DateFormatUtil.format(endTime), takedTime
            + "ms");
    }

    /* (non-Javadoc)
     * @see javax.servlet.Filter#destroy()
     */
    @Override
	public void destroy() {

    }

	public boolean isExclude(HttpServletRequest request) {
		String servletPath = request.getServletPath();
		if (excludePaths.contains(servletPath)) {
			// 如果包含该路径，则返回false
			return true;
		}

		return !CommonContextContainer.isRequestMappingFlag();
	}
}
