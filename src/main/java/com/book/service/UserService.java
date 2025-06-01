package com.book.service;

import com.book.dutychain.AbstractBusinessHandler;
import com.book.dutychain.CityHandler;
import com.book.dutychain.builder.HandlerEnum;
import com.book.pojo.BusinessLaunch;
import com.book.pojo.UserInfo;
import com.book.repo.BusinessLaunchRepository;
import com.book.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class UserService {

    @Resource
    private UserRepository userRepository;

    //查询业务投放数据Add commentMore actions
    @Autowired
    private BusinessLaunchRepository businessLaunchRepository;
    //注入duty.chain
    @Value("${duty.chain}")
    private String handlerType;
    //记录当前的handlerType的配置，判断duty.chain的配置是否有修改
    private String currentHandlerType;
    //记录当前的责任链头节点，如果配置没有修改，下次直接返回即可Add commentMore actions
    private AbstractBusinessHandler currentHandler;

    public String login(String account, String password) {
        UserInfo userInfo = userRepository.findByUserNameAndUserPassword(account, password);
        if(userInfo == null){
            return "account / password error";
        }
        return "login success";
    }

    public String register(UserInfo userInfo) {
        if(checkUserExists(userInfo.getUserName())){
            throw new RuntimeException("user already registered");
        }
        userInfo.setCreateDate(new Date());
        userRepository.save(userInfo);
        return "register success!";
    }

    //根据用户账号名称检查用户是否已注册
    public boolean checkUserExists(String userName){
        UserInfo user = userRepository.findByUserName(userName);
        if(user == null){
            return false;
        }
        return true;
    }

    public List<BusinessLaunch> filterBusinessLaunch(String city,String sex,String product)  {
        List<BusinessLaunch> launchList = businessLaunchRepository.findAll();
        return builderChain().processHandler(launchList,city,sex,product);
    }

    // 构建责任链并返回责任链头节点
    private AbstractBusinessHandler builderChain() {
        // 如果没有配置，直接返回null
        if(handlerType == null){
            return null;
        }
        // 如果是第一次配置，将 handlerType 记录下来
        if(currentHandlerType == null){
            currentHandlerType = handlerType;
        }
        // 配置未修改 currentHandler 不为null，直接返回
        if(this.handlerType.equals(currentHandlerType) && this.currentHandler != null){
            return this.currentHandler;
        }else {
            System.out.println("配置有修改或首次初始化，组装责任链");
            synchronized (this){
                // 创建哑节点，随意找一个类型创建即可
                AbstractBusinessHandler dummyHandler = new CityHandler();
                // 创建前置节点，初始赋值未哑节点
                AbstractBusinessHandler preHandler = dummyHandler;
                // 将 duty.chain的配置用逗号分割为list类型，并通过HandlerEnum创建责任类，并配置责任链条
                List<String> handlerTypeList = Arrays.asList(handlerType.split(","));
                for (String handlerType : handlerTypeList) {
                    AbstractBusinessHandler handler = null;
                    try {
                        handler = (AbstractBusinessHandler) Class.forName(HandlerEnum.valueOf(handlerType).getValue()).newInstance();
                    } catch (Exception e) {
                        throw new UnsupportedOperationException(e);
                    }
                    preHandler.nextHandler = handler;
                    preHandler = handler;
                }
                //重新赋值新的责任链头节点Add commentMore actions
                this.currentHandler = dummyHandler.nextHandler;
                //重新赋值修改后的配置
                this.currentHandlerType = this.handlerType;
                //返回责任链头结点
                return currentHandler;
            }
        }
    }
}
