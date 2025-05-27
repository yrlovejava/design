package com.book.pay.factory;


import com.book.pay.context.PayContext;
import com.book.pay.strategy.PayStrategyInterface;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 支付上下文工厂
 *
 * @param <T>
 */
@Component
public class PayContextFactory<T> extends AbstractPayContextFactory<PayContext> {

    //创建 Map 数据结构作为缓存存储 PayContext
    private static final Map<String, PayContext> payContexts = new ConcurrentHashMap<>();

    @Override
    public PayContext getContext(Integer payType) {
        // 根据 payType 获取 PayContext
        StrategyEnum strategyEnum =
                payType == 1 ? StrategyEnum.alipay : payType == 2 ? StrategyEnum.wechat : null;
        if(strategyEnum == null){
            throw new UnsupportedOperationException("payType not support!");
        }
        
        // 尝试从 map 中获取 PayContext
        PayContext payContext = payContexts.get(strategyEnum.name());
        // 第一次调用 context 为 null
        if(payContext == null){
            // 通过反射创建具体策略类
            try {
                PayStrategyInterface payStrategy = (PayStrategyInterface) Class.forName(strategyEnum.getValue()).newInstance();
                //将具体策略类作为入参，创建 payContext 类
                PayContext context = new PayContext(payStrategy);
                //将payContext类存储Map缓存，下次可直接使用
                payContexts.put(strategyEnum.name(), context);
            }catch (Exception ex){
                throw new UnsupportedOperationException("get payStrategy failed!" + ex);
            }
        }
        return payContexts.get(strategyEnum.name());
    }
}
