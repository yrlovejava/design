package com.book.ordermanagement.listener;

import com.book.ordermanagement.state.OrderState;
import com.book.ordermanagement.state.OrderStateChangeAction;
import com.book.pojo.Order;
import com.book.utils.RedisCommonProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.statemachine.annotation.OnTransition;
import org.springframework.statemachine.annotation.WithStateMachine;
import org.springframework.stereotype.Component;

/**
 * 订单状态监听者
 */
@Component
@WithStateMachine(name = "orderStateMachine") // 开启对 orderStateMachine 的监听
public class OrderStateListener {

    @Autowired
    private RedisCommonProcessor redisCommonProcessor;

    /**
     * 订单等待支付状态 -> 订单等待发出状态
     * @param message 消息
     * @return
     */
    @OnTransition(source = "ORDER_WAIT_PAY",target = "ORDER_WAIT_SEND")
    public boolean payToSend(Message<OrderStateChangeAction> message){
        // 从 Redis 中获取订单，并判断当前订单状态是否为待支付
        Order order = (Order)message.getHeaders().get("order");
        if(order.getOrderState() != OrderState.ORDER_WAIT_PAY){
            throw new UnsupportedOperationException("Order State error");
        }
        //支付成功后修改订单状态为待发货。并更新Redis缓存
        order.setOrderState(OrderState.ORDER_WAIT_SEND);
        redisCommonProcessor.set(order.getOrderId(), order);
        //命令模式相关处理
        return true;
    }

    /**
     * 订单发送 -> 订单接收
     * @param message
     * @return
     */
    @OnTransition(source = "ORDER_WAIT_SEND", target = "ORDER_WAIT_RECEIVE")
    public boolean sendToReceive(Message<OrderStateChangeAction> message){
        Order order = (Order) message.getHeaders().get("order");
        if(order.getOrderState() != OrderState.ORDER_WAIT_SEND) {
            throw new UnsupportedOperationException("Order state error!");
        }
        order.setOrderState(OrderState.ORDER_WAIT_RECEIVE);
        redisCommonProcessor.set(order.getOrderId(), order);
//        new OrderCommandInvoker().invoke(orderCommand, order );
        //命令模式进行相关处理（本章4.10节和4.11节进行实现）
        return true;
    }

    /**
     * 订单接受->订单结束
     * @param message
     * @return
     */
    @OnTransition(source = "ORDER_WAIT_RECEIVE", target = "ORDER_FINISH")
    public boolean receiveToFinish(Message<OrderStateChangeAction> message){
        Order order = (Order) message.getHeaders().get("order");
        if(order.getOrderState() != OrderState.ORDER_WAIT_RECEIVE) {
            throw new UnsupportedOperationException("Order state error!");
        }
        order.setOrderState(OrderState.ORDER_FINISH);
        redisCommonProcessor.remove(order.getOrderId());
        //移除状态机信息
        redisCommonProcessor.remove(order.getOrderId() + "STATE");
//        new OrderCommandInvoker().invoke(orderCommand, order);
        //命令模式进行相关处理（本章4.10节和4.11节进行实现）
        return true;
    }
}
