package com.book.service;

import com.book.items.composite.AbstractProductItem;
import com.book.items.composite.ProductComposite;
import com.book.pojo.ProductItem;
import com.book.repo.ProductItemRepository;
import com.book.utils.RedisCommonProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 商品service层
 */
@Service
public class ProductItemService {

    @Autowired
    private RedisCommonProcessor redisCommonProcessor;

    @Autowired
    private ProductItemRepository productItemRepository;

    /**
     * 获取商品类目信息
     * @return 商品类目信息
     */
    public ProductComposite fetchAllItems(){
        //先查询redis缓存，如果不为null,直接返回即可
        Object cacheItems = redisCommonProcessor.get("items");
        if (cacheItems != null) {
            return (ProductComposite) cacheItems;
        }

        //如果redis缓存为null则查询DB，调用findAll方法获取所有商品类目
        List<ProductItem> fetchDbItems = productItemRepository.findAll();

        // 将 DB 中的商品类目信息拼接成组合模式的树形结构
        ProductComposite items = generateProductTree(fetchDbItems);
        if(items == null){
            throw new UnsupportedOperationException("Product items should not be empty in DB !");
        }

        // 将商品类目信息存入redis缓存中
        redisCommonProcessor.set("items", items);
        return items;
    }

    /**
     * 将 DB 中的商品类目信息拼接成组合模式的树形结构
     * @param fetchItems DB 中的商品类目信息
     * @return 组合模式的树形结构
     */
    private ProductComposite generateProductTree(List<ProductItem> fetchItems){
        ArrayList<ProductComposite> productComposites = new ArrayList<>(fetchItems.size());
        fetchItems.forEach(each -> {
            productComposites.add(ProductComposite.builder()
                    .id(each.getId())
                    .name(each.getName())
                    .pid(each.getPid())
                    .build());
        });

        // 遍历所有的商品类目，将其添加到父类目下
        Map<Integer, List<ProductComposite>> groupingList = productComposites.stream()
                .collect(Collectors.groupingBy(ProductComposite::getPid));
        for (ProductComposite productComposite : productComposites) {
            Integer pid = productComposite.getPid();
            List<ProductComposite> productList = groupingList.get(pid);
            if (productList != null) {
                List<AbstractProductItem> childList = productList.stream()
                        .map(each -> (AbstractProductItem) each)
                        .toList();
                productComposite.setChild(childList);
            }
        }

        return productComposites.isEmpty() ? null : productComposites.get(0);
    }
}
