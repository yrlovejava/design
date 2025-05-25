package com.book.items.composite;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 组合模式-商品类目
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductComposite extends AbstractProductItem {
    private Integer id;
    private Integer pid;
    private String name;
    private List<AbstractProductItem> child = new ArrayList<>();

    @Override
    public void addProductItem(AbstractProductItem item) {
       this.child.add(item);
    }

    @Override
    public void delProductChild(AbstractProductItem item) {
        if(!(item instanceof ProductComposite removeItem)){
            throw new UnsupportedOperationException("Not Support child remove!");
        }
        Iterator<AbstractProductItem> iterator = child.iterator();
        while (iterator.hasNext()){
            ProductComposite composite = (ProductComposite) iterator.next();
            // 移除ID相同的类目
            if(composite.getId().equals(removeItem.getId())){
                iterator.remove();
                break;
            }
        }
    }
}
