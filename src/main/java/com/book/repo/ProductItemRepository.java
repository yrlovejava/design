package com.book.repo;

import com.book.pojo.ProductItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 商品类目Repository
 */
@Repository
public interface ProductItemRepository extends JpaRepository<ProductItem,Integer> {
}
