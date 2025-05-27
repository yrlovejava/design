package com.book.pay.context;

import com.book.pay.strategy.PayStrategyInterface;
import com.book.pojo.Order;

public class PayContext extends AbstractPayContext {

    //关联抽象策略类
    private PayStrategyInterface payStrategy;

    //设计具体策略
    public PayContext(PayStrategyInterface payStrategy){
        this.payStrategy = payStrategy;
    }

    //执行策略
    @Override
    public String execute(Order order){
        return this.payStrategy.pay(order);
    }
}
