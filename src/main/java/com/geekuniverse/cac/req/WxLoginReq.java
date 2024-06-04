package com.geekuniverse.cac.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


/**
 * <p>
 * 微信登录req
 * </p>
 *
 * @author 谢诗宏
 * @since 2022-12-07
 */
@Data
@ApiModel(value = "微信登录req", description = "微信登录对象")
public class WxLoginReq {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "appid不能为空")
    @ApiModelProperty(value = "appid", required = true)
    private String appid;

    @NotNull(message = "code不能为空")
    @ApiModelProperty(value = "code", required = true)
    private String code;

    @ApiModelProperty(value = "推荐人ID，分享二维码中的scene参数", required = true)
    private Integer referrerId;

    @ApiModelProperty(value = "encryptedData")
    private String encryptedData;

    @ApiModelProperty(value = "iv")
    private String iv;

}
