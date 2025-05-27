package com.book.pay.context;

import com.book.pojo.Order;

public abstract class AbstractPayContext {

    public abstract String execute(Order order);
}
