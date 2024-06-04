package com.geekuniverse.cac.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.geekuniverse.cac.common.enums.SystemError;
import com.geekuniverse.cac.core.exception.BusinessException;
import com.geekuniverse.cac.core.result.Result;
import com.geekuniverse.cac.core.support.BaseController;
import com.geekuniverse.cac.dto.StoreDTO;
import com.geekuniverse.cac.dto.StoreDetailDTO;
import com.geekuniverse.cac.entity.Store;
import com.geekuniverse.cac.req.StoreAddReq;
import com.geekuniverse.cac.req.StoreReq;
import com.geekuniverse.cac.service.IStoreService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 门店表 前端控制器
 * </p>
 *
 * @author 谢诗宏
 * @since 2023-05-15
 */
@Api(tags = "门店模块")
@RestController
@RequestMapping("/stores")
public class StoreController extends BaseController {

    @Resource
    private IStoreService storeService;

    @ApiOperation(value = "门店列表")
    @PostMapping("")
    public Result<IPage<StoreDTO>> list(@RequestBody @Validated StoreReq req){
        return Result.success(storeService.listPagesByName(req));
    }

    @ApiOperation(value = "门店列表-管理后台接口使用")
    @GetMapping("")
    public Result<List<StoreDTO>> list(){
        List<Store> stores = storeService.list();
        return Result.success(BeanUtil.copyToList(stores, StoreDTO.class));
    }

    @ApiOperation(value = "门店详情")
    @GetMapping("/{id}")
    public Result<StoreDetailDTO> detail(@PathVariable Integer id){
        return Result.success(storeService.selectById(id));
    }

    @ApiOperation(value = "新增门店")
    @PostMapping("/add")
    public Result add(@RequestBody @Validated StoreAddReq req){
        return Result.failed(SystemError.STORE_4000);
        //Store store = BeanUtil.copyProperties(req, Store.class);
        //return Result.success(storeService.save(store));
    }

    @ApiOperation(value = "修改门店")
    @PutMapping("")
    public Result put(@RequestBody @Validated StoreAddReq req){
        if (req.getId() == null) throw new BusinessException(SystemError.SYS_409, "id");
        Store store = BeanUtil.copyProperties(req, Store.class);
        store.setId(req.getId());
        return Result.success(storeService.updateById(store));
    }

    @ApiOperation("删除门店")
    @DeleteMapping("/{ids}")
    public Result remove(@PathVariable Integer[] ids)
    {
        return Result.success(storeService.removeByIds(Arrays.asList(ids)));
    }
}
