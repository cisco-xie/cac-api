package com.geekuniverse.cac.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

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
public class UserAdminDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("账号")
    private String account;

    @ApiModelProperty("密码")
    @JsonIgnore
    private String password;

    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty("token")
    private String token;

    @ApiModelProperty("token过期时间")
    private Long failureTime;
}
