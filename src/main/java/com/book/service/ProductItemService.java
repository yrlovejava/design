package com.book.service;

import com.book.items.composite.AbstractProductItem;
import com.book.items.composite.ProductComposite;
import com.book.items.vistor.AddItemVisitor;
import com.book.items.vistor.DelItemVisitor;
import com.book.pojo.ProductItem;
import com.book.repo.ProductItemRepository;
import com.book.utils.RedisCommonProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private AddItemVisitor addItemVisitor;

    @Autowired
    private DelItemVisitor delItemVisitor;

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

    /**
     * 增加商品类目
     * @param item 商品类目
     * @return 商品类目
     */
    @Transactional(rollbackFor = Exception.class)
    public ProductComposite addItem(ProductItem  item){
        // 先更新数据库
        productItemRepository.addItem(item.getName(), item.getPid());

        // 访问者模式访问树形数据结构，并添加新的商品类目
        ProductComposite addItem = ProductComposite.builder()
                .id(productItemRepository.findByNameAndPid(item.getName(), item.getPid()).getId())
                .name(item.getName())
                .pid(item.getPid())
                .child(new ArrayList<>())
                .build();
        AbstractProductItem updateItems = addItemVisitor.vistor(addItem);

        // 更新Redis缓存，此处可做重试机制，如果重试不成功，可以人工介入
        redisCommonProcessor.set("items", updateItems);
        return (ProductComposite) updateItems;
    }

    /**
     * 删除商品类目
     * @param item 商品类目
     * @return 商品类目
     */
    @Transactional(rollbackFor = Exception.class)
    public ProductComposite delItems(ProductItem item){
        //先更新数据库
        productItemRepository.delItem(item.getId());
        //访问者模式访问树形数据结构，并添加新的商品类目
        ProductComposite delItem = ProductComposite.builder()
                .id(item.getId())
                .name(item.getName())
                .pid(item.getPid())
                .build();
        AbstractProductItem updatedItems = delItemVisitor.vistor(delItem);

        //更新Redis缓存，此处可以做重试机制，如果重试不成功，可人工介入
        redisCommonProcessor.set("items",updatedItems);
        return (ProductComposite) updatedItems;
    }
}
