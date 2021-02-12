package com.atguigu.springcloud.alibaba.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

/**
 * @author zzyy
 * @date 2020/3/8 13:55
 **/
@FeignClient(value = "seata-account")
public interface AccountService {
    /**
     * 减余额
     *
     * @param userId
     * @param money
     * @return
     */
    @PostMapping(value = "account/decrease")
    void decrease(@RequestParam("userId") Long userId, @RequestParam("money") BigDecimal money);
}
