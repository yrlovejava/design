package com.book.function;

import com.book.bridge.func.RegisterLoginFunc;
import com.book.pojo.UserInfo;
import com.book.repo.UserRepository;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 桥接模式-具体实现化角色（默认实现化角色）
 */
@Component
public class RegisterLoginByDefault extends RegisterLoginFunc implements RegisterLoginFuncInterface {

    @Resource
    private UserRepository userRepository;

    @Override
    public String login(String account, String password) {
        UserInfo userInfo = userRepository.findByUserNameAndUserPassword(account, password);
        if(userInfo == null){
            return "account / password error";
        }
        return "login success";
    }

    @Override
    public String register(UserInfo userInfo) {
        if(checkUserExists(userInfo.getUserName())){
            throw new RuntimeException("user already registered");
        }
        userInfo.setCreateDate(new Date());
        userRepository.save(userInfo);
        return "register success!";
    }

    //根据用户账号名称检查用户是否已注册
    @Override
    public boolean checkUserExists(String userName){
        UserInfo user = userRepository.findByUserName(userName);
        if(user == null){
            return false;
        }
        return true;
    }

//    //瑕疵所在，Default类不需要实现login3rd方法
//    @Override
//    public String login3rd(HttpServletRequest request) {
//        return null;
//    }
}
