package com.geekuniverse.cac.core.exception;

import com.geekuniverse.cac.common.enums.SystemError;
import com.geekuniverse.cac.core.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * @Description: 全局异常处理
 * @author: 谢诗宏
 * @data: 2022-12-15
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 绑定异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(BindException.class)
    public Result<?> defaultErrorHandler(BindException e) {
        /*log.error("【异常】{}", e);*/
        Result<?> result = new Result<>();
        result.setCode(SystemError.SYS_402.getCode());
        result.setMsg(Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage());

        return result;
    }

    /**
     * 捕获Validator产生的异常错误,Bean 校验异常 Validate
     *
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> defaultErrorHandler(MethodArgumentNotValidException e) {
        /*log.error("【异常】{}", e);*/
        Result<?> result = new Result<>();
        result.setCode(SystemError.SYS_402.getCode());
        result.setMsg(Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage());

        return result;
    }

    /**
     * 访问接口参数不全
     *
     * @param e
     * @return
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result<?> missingServletRequestParameterException(MissingServletRequestParameterException e) {
        Result<?> result = new Result<>();
        result.setCode(SystemError.SYS_409.getCode());
        result.setMsg(String.format(SystemError.SYS_409.getMsg(), e.getParameterName()));
        return result;
    }

    /**
     * HttpRequestMethodNotSupportedException 请求方式错误处理
     *
     * @param e
     * @return
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result<?> httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return Result.failed(SystemError.SYS_410);
    }

    /**
     * 系统异常处理
     *
     * @param request
     * @param e
     * @return
     * @throws Exception
     */
    @ExceptionHandler(Exception.class)
    public Result<?> defaultErrorHandler(HttpServletRequest request, HttpServletResponse response, Exception e) {
        log.error("【全局系统异常】", e);
        Result<?> result;

        if (e instanceof BusinessException) {
            BusinessException businessException = (BusinessException) e;
            result = Result.failed(businessException);
        } else {
            if (e instanceof org.springframework.web.servlet.NoHandlerFoundException) {
                result = Result.failed(SystemError.SYS_404);
            } else if (e instanceof org.springframework.http.converter.HttpMessageNotReadableException && e.getMessage().contains("Required request body is missing:")) {
                result = Result.failed(SystemError.SYS_402);
            } else if (e instanceof org.springframework.http.converter.HttpMessageNotReadableException && e.getMessage().contains("JSON parse error:")) {
                int beginIndex = e.getMessage().indexOf("[\"");
                int endIndex = e.getMessage().indexOf("\"])", beginIndex);
                result = new Result<>();
                result.setCode(SystemError.SYS_418.getCode());
                result.setMsg(String.format(SystemError.SYS_418.getMsg(), e.getMessage().substring(beginIndex + 2, endIndex)));
            } else {
                result = Result.failed(SystemError.SYS_500);
                result.setErrDesc(e.getMessage());
            }
        }

        return result;
    }

}
