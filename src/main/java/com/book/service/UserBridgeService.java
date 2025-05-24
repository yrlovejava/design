package com.book.service;

import com.book.abst.AbstractRegisterLoginComponent;
import com.book.abst.factory.RegisterLoginComponentFactory;
import com.book.common.enums.LoginTypeEnum;
import com.book.pojo.UserInfo;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class UserBridgeService {

    public String login(String account,String password){
        // 用左路的具体子类创建调用入口
        AbstractRegisterLoginComponent component = RegisterLoginComponentFactory.getComponent(LoginTypeEnum.DEFAULT.getLoginType());
        return component.login(account,password);
    }

    public String register(UserInfo userInfo) {
        AbstractRegisterLoginComponent component = RegisterLoginComponentFactory.getComponent(LoginTypeEnum.DEFAULT.getLoginType());
        return component.register(userInfo);
    }

    public String login3rd(HttpServletRequest request, String type) {
        AbstractRegisterLoginComponent component = RegisterLoginComponentFactory.getComponent(type);
        return component.login3rd(request);
    }
}
