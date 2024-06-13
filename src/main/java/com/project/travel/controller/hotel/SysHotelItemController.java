package com.project.travel.controller.hotel;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.travel.domain.Result;
import com.project.travel.domain.SysHotel;
import com.project.travel.domain.SysHotelItem;
import com.project.travel.domain.SysHotelOrder;
import com.project.travel.enums.ResultCode;
import com.project.travel.service.SysHotelItemService;
import com.project.travel.service.SysHotelOrderService;
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
 * @description: 房型controller
 * @date 2024/04/07 08:47
 */
@Controller
@ResponseBody
@RequestMapping("item")
public class SysHotelItemController {

    @Autowired
    private SysHotelItemService sysHotelItemService;
    @Autowired
    private SysHotelOrderService sysHotelOrderService;

    /** 分页获取房型 */
    @PostMapping("getSysHotelItemPage")
    public Result getSysHotelItemPage(@RequestBody SysHotelItem sysHotelItem) {
        Page<SysHotelItem> page = new Page<>(sysHotelItem.getPageNumber(),sysHotelItem.getPageSize());
        QueryWrapper<SysHotelItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(StringUtils.isNotBlank(sysHotelItem.getHotelId()),SysHotelItem::getHotelId,sysHotelItem.getHotelId())
                .like(StringUtils.isNotBlank(sysHotelItem.getName()),SysHotelItem::getName,sysHotelItem.getName());
        Page<SysHotelItem> sysHotelItemPage = sysHotelItemService.page(page, queryWrapper);
        return Result.success(sysHotelItemPage);
    }

    @GetMapping("getSysHotelItemList")
    public Result getSysHotelItemList(@RequestParam("id") String id) {
        QueryWrapper<SysHotelItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysHotelItem::getHotelId,id);
        List<SysHotelItem> itemList = sysHotelItemService.list(queryWrapper);
        return Result.success(itemList);
    }

    /** 根据id获取房型 */
    @GetMapping("getSysHotelItemById")
    public Result getSysHotelItemById(@RequestParam("id")String id) {
        SysHotelItem sysHotelItem = sysHotelItemService.getById(id);
        return Result.success(sysHotelItem);
    }

    /** 保存房型 */
    @PostMapping("saveSysHotelItem")
    public Result saveSysHotelItem(@RequestBody SysHotelItem sysHotelItem) {
        boolean save = sysHotelItemService.save(sysHotelItem);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑房型 */
    @PostMapping("editSysHotelItem")
    public Result editSysHotelItem(@RequestBody SysHotelItem sysHotelItem) {
        boolean save = sysHotelItemService.updateById(sysHotelItem);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除房型 */
    @GetMapping("removeSysHotelItem")
    @Transactional(rollbackFor = Exception.class)
    public Result removeSysHotelItem(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                sysHotelItemService.removeById(id);
                QueryWrapper<SysHotelOrder> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(SysHotelOrder::getItemId,id);
                sysHotelOrderService.remove(queryWrapper);
            }
            return Result.success();
        } else {
            return Result.fail("房型id不能为空！");
        }
    }

}
