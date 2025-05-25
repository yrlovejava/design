package com.book.config;

import com.book.ordermanagement.state.OrderState;
import com.book.ordermanagement.state.OrderStateChangeAction;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.EnumSet;

/**
 * 订单状态机配置类
 */
@Configuration
@EnableStateMachine(name = "OrderStateMachine")
public class OrderStateMachineConfig extends StateMachineConfigurerAdapter<OrderState, OrderStateChangeAction> {

    /**
     * 配置订单状态机初始化
     * @param config 状态
     * @throws Exception 异常
     */
    @Override
    public void configure(StateMachineStateConfigurer<OrderState, OrderStateChangeAction> config) throws Exception {
        // 设置订单创建成功后的初始化状态为待支付 ORDER_WAIT_PAY
        config.withStates()
                .initial(OrderState.ORDER_WAIT_PAY)
                .states(EnumSet.allOf(OrderState.class));// 将订单状态类 OrderState 中所有的状态，加载配置到状态机中
    }

    /**
     * 配置订单状态机的状态转移
     * @param transitions 状态转移
     * @throws Exception 异常
     */
    @Override
    public void configure(StateMachineTransitionConfigurer<OrderState, OrderStateChangeAction> transitions) throws Exception {
        transitions.withExternal()
                .source(OrderState.ORDER_WAIT_PAY)
                .target(OrderState.ORDER_WAIT_SEND)
                .event(OrderStateChangeAction.PAY_ORDER)
                .and()
                .withExternal().source(OrderState.ORDER_WAIT_SEND)
                .target(OrderState.ORDER_WAIT_RECEIVE)
                .event(OrderStateChangeAction.SEND_ORDER)
                .and()
                .withExternal().source(OrderState.ORDER_WAIT_RECEIVE)
                .target(OrderState.ORDER_FINISH)
                .event(OrderStateChangeAction.RECEIVE_ORDER);
    }
}
