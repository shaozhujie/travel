package com.project.travel.controller.favor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.travel.domain.Result;
import com.project.travel.domain.SysFavor;
import com.project.travel.domain.SysLine;
import com.project.travel.enums.ResultCode;
import com.project.travel.service.SysFavorService;
import com.project.travel.service.SysLineService;
import com.project.travel.utils.TokenUtils;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 收藏controller
 * @date 2024/04/07 10:06
 */
@Controller
@ResponseBody
@RequestMapping("favor")
public class SysFavorController {

    @Autowired
    private SysFavorService sysFavorService;
    @Autowired
    private SysLineService sysLineService;

    /** 分页获取收藏 */
    @PostMapping("getSysFavorPage")
    public Result getSysFavorPage(@RequestBody SysFavor sysFavor) {
        Page<SysFavor> page = new Page<>(sysFavor.getPageNumber(),sysFavor.getPageSize());
        QueryWrapper<SysFavor> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(StringUtils.isNotBlank(sysFavor.getLineId()),SysFavor::getLineId,sysFavor.getLineId())
                .eq(StringUtils.isNotBlank(sysFavor.getName()),SysFavor::getName,sysFavor.getName())
                .eq(StringUtils.isNotBlank(sysFavor.getIntroduce()),SysFavor::getIntroduce,sysFavor.getIntroduce())
                .eq(StringUtils.isNotBlank(sysFavor.getImages()),SysFavor::getImages,sysFavor.getImages())
                .eq(StringUtils.isNotBlank(sysFavor.getUserId()),SysFavor::getUserId,sysFavor.getUserId());
        Page<SysFavor> sysFavorPage = sysFavorService.page(page, queryWrapper);
        return Result.success(sysFavorPage);
    }

    @GetMapping("getSysFavor")
    public Result getSysFavor(@RequestParam("id")String id) {
        String userIdByToken = TokenUtils.getUserIdByToken();
        QueryWrapper<SysFavor> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysFavor::getUserId,userIdByToken)
                .eq(SysFavor::getLineId,id).last("limit 1");
        SysFavor favor = sysFavorService.getOne(queryWrapper);
        if (favor != null) {
            return Result.success(favor);
        } else {
            return Result.fail();
        }
    }

    /** 根据id获取收藏 */
    @GetMapping("getSysFavorById")
    public Result getSysFavorById(@RequestParam("id")String id) {
        SysFavor sysFavor = sysFavorService.getById(id);
        return Result.success(sysFavor);
    }

    /** 保存收藏 */
    @PostMapping("saveSysFavor")
    public Result saveSysFavor(@RequestBody SysFavor sysFavor) {
        sysFavor.setUserId(TokenUtils.getUserIdByToken());
        SysLine line = sysLineService.getById(sysFavor.getLineId());
        sysFavor.setName(line.getName());
        sysFavor.setIntroduce(line.getIntroduce());
        sysFavor.setImages(line.getImages());
        boolean save = sysFavorService.save(sysFavor);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑收藏 */
    @PostMapping("editSysFavor")
    public Result editSysFavor(@RequestBody SysFavor sysFavor) {
        boolean save = sysFavorService.updateById(sysFavor);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除收藏 */
    @GetMapping("removeSysFavor")
    public Result removeSysFavor(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                sysFavorService.removeById(id);
            }
            return Result.success();
        } else {
            return Result.fail("收藏id不能为空！");
        }
    }

}