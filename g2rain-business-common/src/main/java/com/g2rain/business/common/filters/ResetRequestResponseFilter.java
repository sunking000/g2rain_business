package com.g2rain.business.common.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.g2rain.business.common.servlet.BufferedServletRequestWrapper;
import com.g2rain.business.common.servlet.BufferedServletResponseWrapper;
import com.g2rain.business.common.utils.CommonContextContainer;

/**
 * @ClassName ResetRequestResponseFilter
 * @Description 重置request的输入流
 *
 * @author sunhaojie sunhaojie@kingsoft.com
 * @date 2017年1月13日 下午11:57:14
 */
public class ResetRequestResponseFilter implements Filter {

	private String encoding = null;
	
	public ResetRequestResponseFilter() {
	}

	// public void setServletPathPatternMatcher(ServletPathPatternMatcher
	// servletPathPatternMatcher) {
	// this.servletPathPatternMatcher = servletPathPatternMatcher;
	// }

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.encoding = filterConfig.getInitParameter("encode");
		if(StringUtils.isBlank(this.encoding)) {
			this.encoding = "UTF-8";
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		if (isExclude((HttpServletRequest) request)) {
			chain.doFilter(request, response);
			return;
		}
		HttpServletRequest httpServletRequest = null;
        HttpServletResponse httpServletResponse = null;
		if (request instanceof HttpServletRequest) {
			httpServletRequest = (HttpServletRequest) request;
            httpServletRequest = new BufferedServletRequestWrapper(httpServletRequest);
		}

        if (response instanceof HttpServletResponse) {
            httpServletResponse = (HttpServletResponse) response;
            httpServletResponse = new BufferedServletResponseWrapper(httpServletResponse);
        }

        httpServletRequest.setCharacterEncoding(encoding);
        httpServletResponse.setCharacterEncoding(encoding);
        if (httpServletRequest != null && httpServletResponse != null) {
            chain.doFilter(httpServletRequest, httpServletResponse);
        } else {
            chain.doFilter(request, response);
        }
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy() {

	}

	public boolean isExclude(HttpServletRequest request) {
		return !CommonContextContainer.isRequestMappingFlag();
	}
}
