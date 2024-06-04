package com.geekuniverse.cac.core.result;

import com.geekuniverse.cac.common.constants.ErrCode;
import com.geekuniverse.cac.common.enums.SystemError;
import com.geekuniverse.cac.core.exception.BusinessException;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author 谢诗宏
 * @description 统一响应格式
 * @date 2022/12/10
 */
@Data
public class Result<T> implements Serializable {

    private int code = 0;
    private String msg = "成功";
    private String errDesc;
    private T data;

    public Result() {

    }

    public Result(T data) {
        setData(data);
    }

    public static Result<?> success() {
        return new Result<>();
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(data);
    }

    public static <T> Result<T> failed(Result<?> c) {
        return new Result<>(c.getCode(), c.getMsg());
    }

    public static <T> Result<T> failed(int code, String msg) {
        return new Result<>(code, msg);
    }

    public static <T> Result<T> failed(ErrCode err, Object... format) {
        String msg = format.length > 0 ? String.format(err.getMsg(), format) : err.getMsg();
        return new Result<>(err.getCode(), msg);
    }

    public static <T> Result<T> failed(BusinessException e) {
        return new Result<>(e.getCode(), e.getMessage());
    }


    public Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Result(int code, String msg, String errDesc) {
        this.code = code;
        this.msg = msg;
        this.errDesc = errDesc;
    }

    public Result(int code, String msg, T data) {
        setCode(code);
        setMsg(msg);
        setData(data);
    }

    public Result(BusinessException e) {
        this.code = e.getCode();
        this.msg = e.getMessage();
    }

    public boolean isSuccess() {
        return this.code == 0;
    }

    /**
     * 是否操作成功
     *
     * @param <T>
     * @return
     */
    public T ok() {
        if (this.code == 0)
            return this.data;
        throw new BusinessException(SystemError.getDefined(this.code));
    }

    /**
     * 是否操作成功，且存在数据
     *
     * @param <T>
     * @return
     */
    public T okExist() {
        ok();
        if (this.data == null)
            throw new BusinessException(SystemError.SYS_411);
        if (this.data instanceof Collection<?> && CollectionUtils.isEmpty((Collection<?>) this.data))
            throw new BusinessException(SystemError.SYS_411);
        return this.data;
    }
}
