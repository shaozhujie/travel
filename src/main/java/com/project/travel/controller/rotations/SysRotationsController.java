package com.project.travel.controller.rotations;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.travel.domain.Result;
import com.project.travel.domain.SysRotations;
import com.project.travel.enums.ResultCode;
import com.project.travel.service.SysRotationsService;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 轮播图controller
 * @date 2024/04/08 02:55
 */
@Controller
@ResponseBody
@RequestMapping("rotations")
public class SysRotationsController {

    @Autowired
    private SysRotationsService sysRotationsService;

    /** 分页获取轮播图 */
    @PostMapping("getSysRotationsPage")
    public Result getSysRotationsPage(@RequestBody SysRotations sysRotations) {
        Page<SysRotations> page = new Page<>(sysRotations.getPageNumber(),sysRotations.getPageSize());
        QueryWrapper<SysRotations> queryWrapper = new QueryWrapper<>();
        Page<SysRotations> sysRotationsPage = sysRotationsService.page(page, queryWrapper);
        return Result.success(sysRotationsPage);
    }

    @GetMapping("getSysRotationsList")
    public Result getSysRotationsList() {
        List<SysRotations> rotationsList = sysRotationsService.list();
        return Result.success(rotationsList);
    }

    /** 根据id获取轮播图 */
    @GetMapping("getSysRotationsById")
    public Result getSysRotationsById(@RequestParam("id")String id) {
        SysRotations sysRotations = sysRotationsService.getById(id);
        return Result.success(sysRotations);
    }

    /** 保存轮播图 */
    @PostMapping("saveSysRotations")
    public Result saveSysRotations(@RequestBody SysRotations sysRotations) {
        boolean save = sysRotationsService.save(sysRotations);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑轮播图 */
    @PostMapping("editSysRotations")
    public Result editSysRotations(@RequestBody SysRotations sysRotations) {
        boolean save = sysRotationsService.updateById(sysRotations);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除轮播图 */
    @GetMapping("removeSysRotations")
    public Result removeSysRotations(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                sysRotationsService.removeById(id);
            }
            return Result.success();
        } else {
            return Result.fail("轮播图id不能为空！");
        }
    }

}
