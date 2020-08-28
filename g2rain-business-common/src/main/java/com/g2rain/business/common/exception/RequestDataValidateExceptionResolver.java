package com.g2rain.business.common.exception;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

import com.g2rain.business.common.enums.ErrorCodeEnum;

/**
 * 需要处理字段绑定的错误
 * 
 * @author sunhaojie
 *
 */
public class RequestDataValidateExceptionResolver extends AbstractHandlerExceptionResolver {

	@Override
	protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		if (ex instanceof MethodArgumentNotValidException) {
			MethodArgumentNotValidException methodArgumentNotValidException = (MethodArgumentNotValidException) ex;
			BindingResult bindingResult = methodArgumentNotValidException.getBindingResult();

			List<ObjectError> errors = bindingResult.getAllErrors();
			BussinessRuntimeException be = new BussinessRuntimeException(ErrorCodeEnum.PARAMETER_ERROR);
			for (ObjectError error : errors) {
				if (error instanceof FieldError) {
					FieldError fieldError = (FieldError) error;
					String field = fieldError.getField();
					String code = fieldError.getCode();
					be.addSubError(code, field, null);
				}
			}

			throw be;
		}

		return null;
	}

}
