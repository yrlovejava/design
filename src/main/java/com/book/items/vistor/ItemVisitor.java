package com.book.items.vistor;

import com.book.items.composite.AbstractProductItem;

/**
 * 访问者模式-商品抽象访问者
 * @param <T>
 */
public interface ItemVisitor<T> {

    //定义公共的 vistor 方法供子类实现
    T vistor(AbstractProductItem productItem);
}
