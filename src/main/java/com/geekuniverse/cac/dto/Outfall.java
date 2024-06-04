package com.geekuniverse.cac.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @name: Outfall
 * @author: 谢诗宏
 * @date: 2023-05-25 15:58
 **/
@Data
public class Outfall {

    @ExcelProperty("排口编号")
    private String outfallId;

    @ExcelProperty("排口名称")
    private String outfallName;

    @ExcelProperty("经度")
    private BigDecimal lng;

    @ExcelProperty("纬度")
    private BigDecimal lat;

    @ExcelProperty("排口类型")
    private String outfallType;

    @ExcelProperty("所属河流")
    private String riverName;

    @ExcelProperty("排口尺寸")
    private String outfallSize;

    @ExcelProperty("项目编号")
    private Integer projectCode;

    @ExcelProperty("现场照片")
    private String img;

    @ExcelProperty("整改前")
    private String rectifyBefore;

    @ExcelProperty("整改后")
    private String rectifyAfter;

    @ExcelProperty("排口类型id")
    private Integer outfallTypeId;

    @ExcelProperty("河流id")
    private Integer riverId;

    @ExcelProperty("省id")
    private Integer province;

    @ExcelProperty("市id")
    private Integer city;

    @ExcelProperty("区id")
    private Integer area;

    @ExcelProperty("地址")
    private String address;

    @ExcelProperty("上传人员")
    private String uploader;

    @ExcelProperty("上传人员手机号")
    private String uploaderPhone;

    @ExcelProperty("有无水")
    private String havaWater;

    @ExcelProperty("单位")
    private String company;

    @ExcelProperty("左右岸")
    private String banks;

    @ExcelProperty("是否整治")
    private String isRectify;

    @ExcelProperty("整治措施")
    private String rectifyMethod;

    @ExcelProperty("水质等级")
    private String waterGrades;

    @ExcelProperty("水温")
    private String waterTemp;

    @ExcelProperty("ph")
    private String ph;

    @ExcelProperty("电导率")
    private String cond;

    @ExcelProperty("溶解氧")
    private String DO;

    @ExcelProperty("化学需氧量")
    private String cod;

    @ExcelProperty("氨氮")
    private String NH3N;

    @ExcelProperty("浊度")
    private String td;

    @ExcelProperty("总氮")
    private String tn;

    @ExcelProperty("总磷")
    private String tp;

    @ExcelProperty("排口受水区（格式需与样例格式相同）")
    private String receivingArea;

    @ExcelProperty("污染河段")
    private String pits;

    @ExcelProperty("重点污染企业名称（包括污水处理厂）")
    private String pollutingEnterprisesName;

    @ExcelProperty("重点污染企业经度")
    private String pollutingEnterprisesLng;

    @ExcelProperty("重点污染企业纬度")
    private String pollutingEnterprisesLat;

    @ExcelProperty("重点污染企业地址")
    private String pollutingEnterprisesAddress;

    @ExcelProperty("权属单位")
    private String ownershipUnit;

    @ExcelProperty("收纳水体")
    private String receivingWater;

    @ExcelProperty("容量")
    private String capacity;

    @ExcelProperty("工艺")
    private String technology;

    @ExcelProperty("摄入量")
    private String intake;

    @ExcelProperty("服务人数")
    private String serviceNumber;

    @ExcelProperty("管理员")
    private String administrator;

    @ExcelProperty("土地利用类型（打包发给录入人员。图层字段需包括type(1生活、2鱼塘、3农田、4湖泊、5湿地、6荒地。)site_id（点位id））")
    private String landUseType;
}
