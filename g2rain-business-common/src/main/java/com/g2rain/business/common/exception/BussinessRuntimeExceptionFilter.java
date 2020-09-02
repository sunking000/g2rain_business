package com.g2rain.business.common.exception;

import java.io.IOException;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.g2rain.business.common.exception.strategy.FailResultResponseStrategy;
import com.g2rain.business.common.result.BaseResult;

import lombok.Setter;

/**
 *
 * @ClassName BussinessRuntimeExceptionFilter
 * @Description TODO
 *
 * @date 2017年7月21日 上午10:51:28
 */
@Setter
public class BussinessRuntimeExceptionFilter implements Filter {

	private FailResultResponseStrategy failResultResponseStrategy;


    private static Logger logger = LoggerFactory.getLogger(BussinessRuntimeExceptionFilter.class);
    

    /* (non-Javadoc)
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    @Override
	public void init(FilterConfig filterConfig) throws ServletException {
        
    }

    /* (non-Javadoc)
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    @Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
        ServletException {
        BaseResult fail = null;
        try {
            chain.doFilter(request, response);
        } catch (BussinessRuntimeException e) {
			logger.error("BussinessRuntimeException error code:{}, message:{}", e.getErrorCode(), e.getMessage());
			fail = e.convertResult();
        } catch (Exception e) {
			logger.error(e.getMessage(), e);
			BussinessRuntimeException be = findBussinessRuntimeException(e);
			if (be != null) {
				fail = be.convertResult();
            } else {
                fail = new BaseResult(BaseResult.FAIL);
				logger.error(e.getMessage(), e);
            }
        } finally {
			HttpServletRequest httpServletRequest = (HttpServletRequest) request;
			HttpServletResponse httpServletResponse = (HttpServletResponse) response;
			if (failResultResponseStrategy != null) {
				failResultResponseStrategy.write(httpServletRequest, httpServletResponse, fail);
			} else {
				if (fail != null) {
					if (request instanceof HttpServletRequest) {
						String pathInfo = httpServletRequest.getServletPath();
						fail.setPath(pathInfo);
						String method = httpServletRequest.getMethod();
						fail.setMethod(method);
						String requestId = httpServletRequest.getHeader("request_id");
						fail.setRequestId(requestId);
					}
					fail.setTimestamp(new Date());
					response.setCharacterEncoding("UTF-8");
					response.setContentType("application/json");
					response.getWriter().print(JSONObject.toJSONString(fail));
				}
			}
        }
    }

	private BussinessRuntimeException findBussinessRuntimeException(Throwable cause) {
		Throwable tempCause = cause.getCause();
		int i = 0;
		while (tempCause != null && i < 10) {
			i++;
			if (tempCause instanceof BussinessRuntimeException) {
				return (BussinessRuntimeException) tempCause;
			} else if (tempCause.getCause() != null) {
				tempCause = tempCause.getCause();
			}
		}

		return null;
	}

    
    /* (non-Javadoc)
     * @see javax.servlet.Filter#destroy()
     */
    @Override
	public void destroy() {

    }

}
