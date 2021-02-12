package com.atguigu.springcloud.alibaba.service.impl;

import com.atguigu.springcloud.alibaba.dao.OrderDao;
import com.atguigu.springcloud.alibaba.domain.Order;
import com.atguigu.springcloud.alibaba.service.AccountService;
import com.atguigu.springcloud.alibaba.service.OrderService;
import com.atguigu.springcloud.alibaba.service.StorageService;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 账户
 *
 * @author zzyy
 * @date 2020/3/8 13:57
 **/
@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderDao orderDao;
    @Resource
    private AccountService accountService;
    @Resource
    private StorageService storageService;

    /**
     * 创建订单->调用库存服务扣减库存->调用账户服务扣减账户余额->修改订单状态
     * 简单说:
     * 下订单->减库存->减余额->改状态
     * GlobalTransactional seata开启分布式事务,异常时回滚,name保证唯一即可
     *
     * @param order 订单对象
     */
    @Override
    @GlobalTransactional//(name = "fsp-create-order", rollbackFor = Exception.class)
    public void create(Order order) {
        // 1 新建订单
        orderDao.create(order);

        // 2 扣减库存
        //storageService.decrease(order.getProductId(), order.getCount());

        // 3 扣减账户
        accountService.decrease(order.getUserId(), order.getMoney());

        // 4 修改订单状态,从0到1,1代表已完成
        orderDao.update(order.getUserId(), 0);
     //int i =1/0;
    }
}
