package com.geekuniverse.cac.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.geekuniverse.cac.common.enums.SystemError;
import com.geekuniverse.cac.core.exception.BusinessException;
import com.geekuniverse.cac.core.result.Result;
import com.geekuniverse.cac.dto.StoreBoxDTO;
import com.geekuniverse.cac.dto.StoreDTO;
import com.geekuniverse.cac.entity.Store;
import com.geekuniverse.cac.entity.StoreBox;
import com.geekuniverse.cac.req.StoreAddReq;
import com.geekuniverse.cac.req.StoreBoxAddReq;
import com.geekuniverse.cac.service.IStoreBoxService;
import com.geekuniverse.cac.service.IStoreService;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 包厢表 前端控制器
 * </p>
 *
 * @author 谢诗宏
 * @since 2023-05-15
 */
@RestController
@RequestMapping("/box")
public class StoreBoxController {

    @Resource
    private IStoreService storeService;

    @Resource
    private IStoreBoxService storeBoxService;

    @GetMapping("")
    public Result<List<StoreBoxDTO>> list(@RequestParam(required = false) Integer storeId){
        List<StoreBox> boxs = storeBoxService.list(Wrappers.<StoreBox>lambdaQuery().eq(storeId != null, StoreBox::getStoreId, storeId));
        List<StoreBoxDTO> result = BeanUtil.copyToList(boxs, StoreBoxDTO.class);
        result.forEach(box -> {
            Store store = storeService.getById(box.getStoreId());
            box.setStoreName(store.getName());
        });
        return Result.success(result);
    }

    @PostMapping("")
    public Result add(@RequestBody @Validated StoreBoxAddReq req){
        StoreBox box = BeanUtil.copyProperties(req, StoreBox.class);
        return Result.success(storeBoxService.save(box));
    }

    @PutMapping("")
    public Result put(@RequestBody @Validated StoreBoxAddReq req){
        if (req.getId() == null) throw new BusinessException(SystemError.SYS_409, "id");
        StoreBox box = BeanUtil.copyProperties(req, StoreBox.class);
        box.setId(req.getId());
        return Result.success(storeBoxService.updateById(box));
    }

    @DeleteMapping("/{ids}")
    public Result remove(@PathVariable Integer[] ids)
    {
        return Result.success(storeBoxService.removeByIds(Arrays.asList(ids)));
    }
}
