package com.book.abst.factory;

import com.book.abst.AbstractRegisterLoginComponent;
import com.book.abst.RegisterLoginComponent;
import com.book.bridge.function.RegisterLoginFuncInterface;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 注册登录组件工厂
 */
public class RegisterLoginComponentFactory {
    /**
     * 缓存左路 AbstractRegisterLoginComponent，根据不同的登录方式进行缓存
     */
    public static Map<String, AbstractRegisterLoginComponent> componentMap = new ConcurrentHashMap<>();

    /**
     * 缓存右路 RegisterLoginFuncInterface，根据不同类型进行缓存
     */
    public static Map<String, RegisterLoginFuncInterface> funcMap = new ConcurrentHashMap<>();

    /**
     * 根据登录方式获取组件
     * @param loginType 登录方式
     * @return 组件
     */
    public static AbstractRegisterLoginComponent getComponent(String loginType) {
        AbstractRegisterLoginComponent component = componentMap.get(loginType);
        if (component == null) {
            // 并发情况下，汲取双重检查锁机制的设计，如果componentMap中不存在loginType，则进行创建
            synchronized (componentMap){
                component = componentMap.get(loginType);
                if (component == null) {
                    component = new RegisterLoginComponent(funcMap.get(loginType));
                    componentMap.put(loginType, component);
                }
            }
        }
        return component;
    }
}
