package com.geekuniverse.cac.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.geekuniverse.cac.dto.*;
import com.geekuniverse.cac.entity.Label;
import com.geekuniverse.cac.entity.Store;
import com.geekuniverse.cac.entity.StoreBox;
import com.geekuniverse.cac.entity.StoreBoxLabel;
import com.geekuniverse.cac.mapper.*;
import com.geekuniverse.cac.req.StoreReq;
import com.geekuniverse.cac.service.IStoreService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>
 * 门店表 服务实现类
 * </p>
 *
 * @author 谢诗宏
 * @since 2023-05-15
 */
@Service
public class StoreServiceImpl extends ServiceImpl<StoreMapper, Store> implements IStoreService {

    @Resource
    private StoreBoxMapper storeBoxMapper;

    @Resource
    private StoreBoxLabelMapper storeBoxLabelMapper;

    @Resource
    private OrderMapper orderMapper;
    @Resource
    private LabelMapper labelMapper;

    @Transactional
    @Override
    public IPage<StoreDTO> listPagesByName(StoreReq req) {
        //IPage<Store> storeIPage = baseMapper.selectPage(new Page<>(req.getPageIndex(), req.getPageSize()), Wrappers.<Store>lambdaQuery()
        //        .like(StringUtils.isNotBlank(req.getName()), Store::getName, req.getName()));
        //return storeIPage.convert(page -> {
        //    return BeanUtil.copyProperties(page, StoreDTO.class);
        //});
        return baseMapper.storeIPage(new Page<>(req.getPageIndex(), req.getPageSize()), req);

    }

    @Override
    public StoreDetailDTO selectById(Integer id) {
        StoreDetailDTO storeDetailDTO = new StoreDetailDTO();
        Store store = baseMapper.selectById(id);
        BeanUtil.copyProperties(store, storeDetailDTO);

        List<StoreBox> boxes = storeBoxMapper.selectList(Wrappers.<StoreBox>lambdaQuery()
                .eq(StoreBox::getStoreId, id));
        List<StoreBoxDTO> boxDTOS = BeanUtil.copyToList(boxes, StoreBoxDTO.class);
        storeDetailDTO.setBoxs(boxDTOS);
        boxDTOS.forEach(storeBox -> {
            List<UsagePeriodDTO> usagePeriodDTOS = orderMapper.getUsagePeriods(storeBox.getId());
            storeBox.setUsagePeriod(usagePeriodDTOS);
            List<StoreBoxLabel> boxLabels = storeBoxLabelMapper.selectList(Wrappers.<StoreBoxLabel>lambdaQuery()
                    .eq(StoreBoxLabel::getBoxId, storeBox.getId()));
            List<String> labelDTOS = new ArrayList<>();
            storeBox.setLabels(labelDTOS);
            boxLabels.forEach(storeBoxLabel -> {
                Label label = labelMapper.selectById(storeBoxLabel.getLabelId());
                LabelDTO labelDTO = BeanUtil.copyProperties(label, LabelDTO.class);
                labelDTOS.add(labelDTO.getLabel());
            });
        });
        return storeDetailDTO;
    }

}
