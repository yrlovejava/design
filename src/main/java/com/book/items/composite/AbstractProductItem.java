package com.book.items.composite;

/**
 * 组合模式-抽象商品类目
 */
public abstract class AbstractProductItem {

    /**
     * 增加商品类目
     * @param item 商品类目
     */
    protected void addProductItem(AbstractProductItem item){
        throw new UnsupportedOperationException("Not Support child add!");
    }

    /**
     * 移除商品类目
     * @param item 商品类目
     */
    protected void delProductChild(AbstractProductItem item){
        throw new UnsupportedOperationException("Not Support child remove!");
    }
}
