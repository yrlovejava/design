package com.book.pay.strategy;

import com.book.pojo.Order;

/**
 * 抽象支付策略接口
 */
public interface PayStrategyInterface {

    // 定义公共的支付方法
    String pay(Order order);
}
