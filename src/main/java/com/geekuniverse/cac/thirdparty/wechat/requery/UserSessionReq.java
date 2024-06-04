package com.geekuniverse.cac.thirdparty.wechat.requery;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 微信登录凭证校验请求参数
 * </p>
 *
 * @author 谢诗宏
 * @since 2023-05-15
 */
@Getter
@Setter
public class UserSessionReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("小程序 appId")
    private String appid;

    @ApiModelProperty("小程序 appSecret")
    private String secret;

    @ApiModelProperty("登录时获取的 code，可由前端通过wx.login获取")
    private String jsCode;

    @ApiModelProperty("授权类型，此处只需填写 authorization_code")
    private String grantType = "authorization_code";

}
