package com.geekuniverse.cac.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 包厢不可用时间段
 *
 * @name: UsagePeriodDTO
 * @author: 谢诗宏
 * @date: 2023-05-20 18:19
 **/
@Data
public class UsagePeriodDTO {

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @ApiModelProperty("不可用时间段-开始时间")
    private LocalDateTime startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @ApiModelProperty("不可用时间段-结束时间")
    private LocalDateTime endTime;
}
