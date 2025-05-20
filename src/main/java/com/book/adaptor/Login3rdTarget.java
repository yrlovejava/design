package com.book.adaptor;

public interface Login3rdTarget {

    String loginByGitee(String code,String state);

    String loginByByWechat(String... params);

    String loginByQQ(String... params);
}
