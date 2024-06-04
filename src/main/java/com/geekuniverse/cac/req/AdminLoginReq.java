package com.geekuniverse.cac.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author 谢诗宏
 * @since 2023-05-25
 */
@Getter
@Setter
@Accessors(chain = true)
public class AdminLoginReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "账号必填")
    @ApiModelProperty("账号")
    private String account;

    @NotBlank(message = "密码必填")
    @ApiModelProperty("密码")
    private String password;

}
