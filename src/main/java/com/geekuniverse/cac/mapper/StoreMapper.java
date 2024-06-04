package com.geekuniverse.cac.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.geekuniverse.cac.dto.StoreDTO;
import com.geekuniverse.cac.entity.Store;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.geekuniverse.cac.req.StoreReq;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 门店表 Mapper 接口
 * </p>
 *
 * @author 谢诗宏
 * @since 2023-05-15
 */
public interface StoreMapper extends BaseMapper<Store> {

    @Select({"<script>"+
            "SELECT " +
            "  *," +
            "ROUND(" +
            "  (" +
            // 6371是地球半径，单位是km
            "    6371 * acos(" +
            "      cos(radians(#{req.lat})) * cos(radians(lat)) * cos(" +
            "        radians(lng) - radians(#{req.lng})" +
            "      ) + sin(radians(#{req.lat})) * sin(radians(lat))" +
            "    )" +
            "  ), 1" +
            " ) AS distance" +
            " FROM " +
            "  t_store" +
            "  WHERE" +
            " 1=1 " +
            "<if test=\"req.name != null and req.name != ''\"> and name LIKE CONCAT('%',#{req.name},'%')</if> "+
            " ORDER BY " +
            "  distance"
            +"</script>"})
    IPage<StoreDTO> storeIPage(IPage page, @Param("req") StoreReq req);

}
