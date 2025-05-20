package com.book.repo;

import com.book.pojo.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserInfo,Integer> {

    /**
     * 根据用户名查询用户信息
     * @param userName
     * @return
     */
    UserInfo findByUserName(String userName);

    /**
     * 根据用户和密码查询用户信息
     * @param account
     * @param password
     * @return
     */
    UserInfo findByUserNameAndUserPassword(String account,String password);
}
