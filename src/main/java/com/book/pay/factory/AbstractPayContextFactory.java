package com.book.pay.factory;

public abstract class AbstractPayContextFactory<T> {

    public abstract T getContext(Integer payType);
}
