package com.book.pojo;

import com.book.ordermanagement.state.OrderState;
import lombok.*;

/**
 * 订单实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Order {
    private String orderId;
    private String productId;
    private OrderState orderState;
    private Float price;
}
