package com.project.travel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.travel.domain.User;
import org.apache.ibatis.annotations.Param;

/**
 * @version 1.0
 * @description: 用户mapper
 * @date 2023/8/28 8:41
 */
public interface UserMapper extends BaseMapper<User> {

    /**
    * 分页查询用户
    */
    Page<User> getUserPage(Page<User> page, @Param("ew") User apeUser);

}
