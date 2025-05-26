package com.book.ordermanagement.command;

import com.book.ordermanagement.command.receiver.OrderCommandReceiver;
import com.book.pojo.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderCommand implements OrderCommandInterface {

    @Autowired
    private OrderCommandReceiver orderCommandReceiver;

    @Override
    public void execute(Order order) {
        // 调用命令接收者的 action 方法，执行命令
        orderCommandReceiver.action(order);
    }
}
