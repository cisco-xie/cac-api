package com.geekuniverse.cac.service.impl;

import com.geekuniverse.cac.entity.Balance;
import com.geekuniverse.cac.mapper.BalanceMapper;
import com.geekuniverse.cac.service.IBalanceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户余额表 服务实现类
 * </p>
 *
 * @author 谢诗宏
 * @since 2023-07-20
 */
@Service
public class BalanceServiceImpl extends ServiceImpl<BalanceMapper, Balance> implements IBalanceService {

}
