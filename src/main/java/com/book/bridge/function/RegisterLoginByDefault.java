package com.book.bridge.function;

import com.book.abst.factory.RegisterLoginComponentFactory;
import com.book.common.enums.LoginTypeEnum;
import com.book.pojo.UserInfo;
import com.book.repo.UserRepository;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * 桥接模式-具体实现化角色（默认实现化角色）
 */
@Component
public class RegisterLoginByDefault extends RegisterLoginFunc implements RegisterLoginFuncInterface {

    @PostConstruct
    private void initFunMap(){
        RegisterLoginComponentFactory.funcMap.put(LoginTypeEnum.DEFAULT.getLoginType(), this);
    }

    @Resource
    private UserRepository userRepository;

    @Override
    public String login(String account, String password) {
        return super.commonLogin(account,password,userRepository);
    }

    @Override
    public String register(UserInfo userInfo) {
        return super.commonRegister(userInfo,userRepository);
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
