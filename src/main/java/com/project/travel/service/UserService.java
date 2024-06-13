package com.project.travel.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.project.travel.domain.User;

/**
 * @version 1.0
 * @description: 用户service
 * @date 2023/8/28 8:45
 */
public interface UserService extends IService<User> {

    /**
    * @description: 分页查询用户
    * @param: apeUser
    * @return: Page
    * @date: 2023/8/28 10:49
    */
    Page<User> getUserPage(User apeUser);

}
