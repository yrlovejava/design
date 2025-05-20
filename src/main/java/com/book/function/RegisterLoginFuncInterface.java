package com.book.function;

import com.book.pojo.UserInfo;

import javax.servlet.http.HttpServletRequest;

/**
 * 桥接模式-实现化角色，定义了具体的实现方式
 * 桥接模式的实现化角色，需要和抽象化角色形成聚合关系，
 * 即抽象化角色的属性引用实现化角色，二者可以是双向的聚合关系，也可以是单向的聚合关系
 * 此处是单向聚合关系，即抽象化角色引用实现化角色，实现化角色不引用抽象化角色
 */
public interface RegisterLoginFuncInterface {
    String login(String account, String password);

    String register(UserInfo userInfo);

    boolean checkUserExists(String userName);

    String login3rd(HttpServletRequest request);
}
