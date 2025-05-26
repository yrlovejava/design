package com.book.ordermanagement.command;

import com.book.pojo.Order;

/**
 * 订单命令调用器
 */
public interface OrderCommandInterface {

    void execute(Order order);
}
