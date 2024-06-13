package com.project.travel.controller.line;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.travel.domain.Result;
import com.project.travel.domain.SysFavor;
import com.project.travel.domain.SysLine;
import com.project.travel.enums.ResultCode;
import com.project.travel.service.SysFavorService;
import com.project.travel.service.SysLineService;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 旅游线路controller
 * @date 2024/04/07 02:42
 */
@Controller
@ResponseBody
@RequestMapping("line")
public class SysLineController {

    @Autowired
    private SysLineService sysLineService;
    @Autowired
    private SysFavorService sysFavorService;

    /** 分页获取旅游线路 */
    @PostMapping("getSysLinePage")
    public Result getSysLinePage(@RequestBody SysLine sysLine) {
        Page<SysLine> page = new Page<>(sysLine.getPageNumber(),sysLine.getPageSize());
        QueryWrapper<SysLine> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .like(StringUtils.isNotBlank(sysLine.getName()),SysLine::getName,sysLine.getName())
                .eq(sysLine.getUpdateTime() != null,SysLine::getUpdateTime,sysLine.getUpdateTime())
                .orderByDesc(SysLine::getCreateTime);
        Page<SysLine> sysLinePage = sysLineService.page(page, queryWrapper);
        return Result.success(sysLinePage);
    }

    @GetMapping("/getSysLineIndex")
    public Result getSysLineIndex() {
        QueryWrapper<SysLine> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().orderByDesc(SysLine::getCreateTime).last("limit 3");
        List<SysLine> lineList = sysLineService.list(queryWrapper);
        return Result.success(lineList);
    }

    /** 根据id获取旅游线路 */
    @GetMapping("getSysLineById")
    public Result getSysLineById(@RequestParam("id")String id) {
        SysLine sysLine = sysLineService.getById(id);
        return Result.success(sysLine);
    }

    /** 保存旅游线路 */
    @PostMapping("saveSysLine")
    public Result saveSysLine(@RequestBody SysLine sysLine) {
        boolean save = sysLineService.save(sysLine);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑旅游线路 */
    @PostMapping("editSysLine")
    public Result editSysLine(@RequestBody SysLine sysLine) {
        boolean save = sysLineService.updateById(sysLine);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除旅游线路 */
    @GetMapping("removeSysLine")
    @Transactional(rollbackFor = Exception.class)
    public Result removeSysLine(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                sysLineService.removeById(id);
                QueryWrapper<SysFavor> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(SysFavor::getLineId,id);
                sysFavorService.remove(queryWrapper);
            }
            return Result.success();
        } else {
            return Result.fail("旅游线路id不能为空！");
        }
    }

}
