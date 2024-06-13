package com.project.travel.controller.forum;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.travel.domain.Result;
import com.project.travel.domain.SysForum;
import com.project.travel.enums.ResultCode;
import com.project.travel.service.SysForumService;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 资讯controller
 * @date 2024/04/07 09:47
 */
@Controller
@ResponseBody
@RequestMapping("forum")
public class SysForumController {

    @Autowired
    private SysForumService sysForumService;

    /** 分页获取资讯 */
    @PostMapping("getSysForumPage")
    public Result getSysForumPage(@RequestBody SysForum sysForum) {
        Page<SysForum> page = new Page<>(sysForum.getPageNumber(),sysForum.getPageSize());
        QueryWrapper<SysForum> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .like(StringUtils.isNotBlank(sysForum.getTitle()),SysForum::getTitle,sysForum.getTitle())
                .orderByDesc(SysForum::getCreateTime);
        Page<SysForum> sysForumPage = sysForumService.page(page, queryWrapper);
        return Result.success(sysForumPage);
    }

    /** 根据id获取资讯 */
    @GetMapping("getSysForumById")
    public Result getSysForumById(@RequestParam("id")String id) {
        SysForum sysForum = sysForumService.getById(id);
        return Result.success(sysForum);
    }

    /** 保存资讯 */
    @PostMapping("saveSysForum")
    public Result saveSysForum(@RequestBody SysForum sysForum) {
        boolean save = sysForumService.save(sysForum);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑资讯 */
    @PostMapping("editSysForum")
    public Result editSysForum(@RequestBody SysForum sysForum) {
        boolean save = sysForumService.updateById(sysForum);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除资讯 */
    @GetMapping("removeSysForum")
    public Result removeSysForum(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                sysForumService.removeById(id);
            }
            return Result.success();
        } else {
            return Result.fail("资讯id不能为空！");
        }
    }

}