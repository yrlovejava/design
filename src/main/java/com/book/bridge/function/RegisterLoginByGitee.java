package com.book.bridge.function;

import com.alibaba.fastjson.JSONObject;
import com.book.pojo.UserInfo;
import com.book.repo.UserRepository;
import com.book.utils.HttpClientUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 桥接模式-具体实现化角色（Gitee 登录实现）
 */
@Component
public class RegisterLoginByGitee extends RegisterLoginFunc implements RegisterLoginFuncInterface {

    @Value("${gitee.state}")
    private String giteeState;

    @Value("${gitee.token.url}")
    private String giteeTokenUrl;

    @Value("${gitee.user.url}")
    private String giteeUserUrl;

    @Value("${gitee.user.prefix}")
    private String giteeUserPrefix;
    @Resource
    private UserRepository userRepository;

    @Override
    public String login3rd(HttpServletRequest request) {
        //入参为HttpServletRequest 这样方法不仅可以为Gitee账号进行第三方登录，也可用于其他第三方账号登录
        String code = request.getParameter("code");
        String state = request.getParameter("state");
        //进行state判断，state值是前端后端商定
        if (!giteeState.equals(state)) {
            throw new UnsupportedOperationException("invalid state!");
        }
        //请求gitee平台token，并携带code
        String tokenUrl = giteeTokenUrl.concat(code);
        JSONObject tokenResponse = HttpClientUtils.execute(tokenUrl, HttpMethod.POST);
        String token = String.valueOf(tokenResponse.get("access_token"));
        //请求用户信息，并携带token
        String userUrl = giteeUserUrl.concat(token);
        JSONObject userInfoResponse = HttpClientUtils.execute(userUrl, HttpMethod.GET);
        //获取用户信息，userName前缀GITEE@，密码保持与userName一致
        String userName = giteeUserPrefix.concat(String.valueOf(userInfoResponse.get("name")));
        String password = userName;
        //自动注册和登录功能，此处体现了方法的复用
        return autoRegister3rdAndLogin(userName, password);
    }

    //自动登录和注册
    private String autoRegister3rdAndLogin(String userName, String password) {
        //如果第三方账号登录过.直接登录，此时用户名有前缀了，不用担心重复
        if (super.checkUserExists(userName)) {
            return super.commonLogin(userName, password,userRepository);
        }
        //如果是第一次登录，则先注册,再登录
        UserInfo userInfo = new UserInfo();
        userInfo.setUserName(userName);
        userInfo.setUserPassword(password);
        userInfo.setCreateDate(new Date());
        super.commonRegister(userInfo,userRepository);
        return super.commonLogin(userName, password,userRepository);
    }
}
