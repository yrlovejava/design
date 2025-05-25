package com.book.items.vistor;

import com.book.items.composite.AbstractProductItem;
import com.book.items.composite.ProductComposite;
import com.book.utils.RedisCommonProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 访问者模式-添加商品类目实现
 */
@Component
public class AddItemVisitor<T> implements ItemVisitor<AbstractProductItem> {

    @Autowired
    private RedisCommonProcessor redisCommonProcessor;

    @Override
    public AbstractProductItem vistor(AbstractProductItem productItem) {
        // 从 redis 中获取到树形结构
        ProductComposite currentItem = (ProductComposite) redisCommonProcessor.get("items");

        // 需要新增的商品类目
        ProductComposite addItem = (ProductComposite) productItem;

        // 如果新增节点的父节点为当前节点，则直接添加
        if(addItem.getPid().equals(currentItem.getId())){
            currentItem.addProductItem(addItem);
            return currentItem;
        }

        // 否则，通过 addChild 方法进行递归寻找新增类目的插入点
        addChild(addItem,currentItem);

        return currentItem;
    }

    private void addChild(ProductComposite addItem, ProductComposite currentItem){
        // 遍历当前节点的子节点
        for (AbstractProductItem childItem : currentItem.getChild()) {
            ProductComposite item = (ProductComposite) childItem;
            // 如果子节点的 ID 等于新增节点的父节点 ID，则将新增节点添加到该子节点下
            if(item.getId().equals(addItem.getPid())){
                item.addProductItem(addItem);
                break;
            }else {
                // 否则，递归调用 addChild 方法，继续遍历子节点的子节点
                addChild(addItem,item);
            }
        }
    }
}
