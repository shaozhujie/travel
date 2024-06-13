package com.project.travel.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.travel.domain.SysComments;
import com.project.travel.mapper.SysCommentsMapper;
import com.project.travel.service.SysCommentsService;
import org.springframework.stereotype.Service;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 评论service实现类
 * @date 2024/04/07 10:23
 */
@Service
public class SysCommentsServiceImpl extends ServiceImpl<SysCommentsMapper, SysComments> implements SysCommentsService {
}
