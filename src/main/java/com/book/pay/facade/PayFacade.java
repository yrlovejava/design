package com.book.pay.facade;

import com.book.pay.context.PayContext;
import com.book.pay.factory.PayContextFactory;
import com.book.pojo.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PayFacade {

    @Autowired
    private PayContextFactory contextFactory;

    public String pay(Order order,Integer payType){
        //获取 payContext
        PayContext payContext = contextFactory.getContext(payType);
        //调用支付方法
        return payContext.execute(order);
    }
}
