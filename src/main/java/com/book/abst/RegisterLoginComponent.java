package com.book.abst;

import com.book.bridge.function.RegisterLoginFuncInterface;
import com.book.pojo.UserInfo;

import javax.servlet.http.HttpServletRequest;

public class RegisterLoginComponent extends AbstractRegisterLoginComponent{

    public RegisterLoginComponent(RegisterLoginFuncInterface funcInterface) {
        super(funcInterface);
    }

    // 直接通过桥梁，调用右路Implementor方式，把具体实现交给右路的实现类

    @Override
    public String login(String account, String password) {
        return funcInterface.login(account,password);
    }

    @Override
    public String register(UserInfo userInfo) {
        return funcInterface.register(userInfo);
    }

    @Override
    public boolean checkUserExists(String userName) {
        return funcInterface.checkUserExists(userName);
    }

    @Override
    public String login3rd(HttpServletRequest request) {
        return funcInterface.login3rd(request);
    }
}
