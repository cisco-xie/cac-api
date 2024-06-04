package com.geekuniverse.cac.mapper;

import com.geekuniverse.cac.dto.UsagePeriodDTO;
import com.geekuniverse.cac.entity.Order;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 订单表 Mapper 接口
 * </p>
 *
 * @author 谢诗宏
 * @since 2023-05-17
 */
public interface OrderMapper extends BaseMapper<Order> {

    @Select({"<script>"+
            "SELECT start_time, end_time FROM t_order WHERE box_id = #{boxId} AND end_time &gt; NOW()"
            +"</script>"})
    List<UsagePeriodDTO> getUsagePeriods(Integer boxId);

}
