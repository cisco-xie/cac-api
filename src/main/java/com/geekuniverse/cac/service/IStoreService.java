package com.geekuniverse.cac.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.geekuniverse.cac.dto.StoreDTO;
import com.geekuniverse.cac.dto.StoreDetailDTO;
import com.geekuniverse.cac.entity.Store;
import com.baomidou.mybatisplus.extension.service.IService;
import com.geekuniverse.cac.req.StoreReq;

/**
 * <p>
 * 门店表 服务类
 * </p>
 *
 * @author 谢诗宏
 * @since 2023-05-15
 */
public interface IStoreService extends IService<Store> {

    /**
     * 根据名字获取列表页面
     *
     * @param req 要求事情
     * @return {@link IPage}<{@link StoreDTO}>
     */
    IPage<StoreDTO> listPagesByName(StoreReq req);

    /**
     * 选择通过id
     *
     * @param id id
     * @return {@link StoreDetailDTO}
     */
    StoreDetailDTO selectById(Integer id);
}
