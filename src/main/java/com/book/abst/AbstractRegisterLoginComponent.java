package com.book.abst;

import com.book.bridge.function.RegisterLoginFuncInterface;
import com.book.pojo.UserInfo;

import javax.servlet.http.HttpServletRequest;

/**
 * 桥接模式-抽象化角色，定义了桥接过程
 */
public abstract class AbstractRegisterLoginComponent {

    /// 面向接口编程，引入RegisterLoginFuncInterface 接口属性，“桥”
    protected RegisterLoginFuncInterface funcInterface;

    // 有参构造函数，初始化RegisterLoginFuncInterface属性
    public AbstractRegisterLoginComponent(RegisterLoginFuncInterface funcInterface) {
        validate(funcInterface);
        this.funcInterface = funcInterface;
    }

    protected final void validate(RegisterLoginFuncInterface funcInterface) {
        if(!(funcInterface instanceof RegisterLoginFuncInterface)) {
            throw new UnsupportedOperationException("UnKnown register/login function type");
        }
    }

    protected abstract String login(String account, String password);
    protected abstract String register(UserInfo userInfo);
    protected abstract boolean checkUserExists(String userName);
    protected abstract String login3rd(HttpServletRequest request);
}
