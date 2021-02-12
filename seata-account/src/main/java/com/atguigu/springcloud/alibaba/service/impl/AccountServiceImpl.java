package com.atguigu.springcloud.alibaba.service.impl;

import com.atguigu.springcloud.alibaba.dao.AccountDao;
import com.atguigu.springcloud.alibaba.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

/**
 * 账户业务实现类
 *
 * @author zzyy
 * @date 2020/3/8 15:56
 **/
@Service
@Slf4j
public class AccountServiceImpl implements AccountService {
    @Resource
    private AccountDao accountDao;

    @Override
    @Transactional
    public void decrease(Long userId, BigDecimal money) throws Exception {
        log.info("******************账户业务****************************************");
        log.info("*******->account-service中扣减账户余额开始");
        // 模拟超时异常,全局事务回滚
        try {
            // 暂停20秒钟
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        accountDao.decrease(userId, money);
        //throw new Exception();
        log.info("*******->account-service中扣减账户余额结束");
    }
}
