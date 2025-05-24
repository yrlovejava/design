package com.book.common.enums;

import lombok.Getter;

/**
 * 登录类型枚举
 */
@Getter
public enum LoginTypeEnum {

    /**
     * 默认登录
     */
    DEFAULT("Default"),
    /**
     * Gitee登录
     */
    GITEE("Gitee");

    private final String loginType;

    LoginTypeEnum(String loginType) {
        this.loginType = loginType;
    }
}
