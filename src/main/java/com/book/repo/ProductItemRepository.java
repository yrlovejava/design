package com.book.repo;

import com.book.pojo.ProductItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * 商品类目Repository
 */
@Repository
public interface ProductItemRepository extends JpaRepository<ProductItem,Integer> {

    //向数据库中添加新的商品类目
    @Modifying
    @Query(value = "INSERT INTO PRODUCT_ITEM (id,name,pid) " + "values ((select max(id) + 1 from PRODUCT_ITEM),?1,?2)",nativeQuery = true)
    void addItem(String name,Integer pid);

    //删除商品类目及其直接子目录,目前仅支持删除商品类目数据的最大深度为2
    @Modifying
    @Query(value = "DELETE FROM PRODUCT_ITEM WHERE " + "id=?1 or pid=?1",nativeQuery = true)
    void delItem(int id);

    //根据商品类目name和pid查询商品类目
    ProductItem findByNameAndPid(String name,int pid);
}
