package com.project.travel.controller.login;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.project.travel.domain.Result;
import com.project.travel.domain.SysAttractionOrder;
import com.project.travel.domain.SysHotelOrder;
import com.project.travel.domain.User;
import com.project.travel.service.SysAttractionOrderService;
import com.project.travel.service.SysHotelOrderService;
import com.project.travel.service.UserService;
import com.project.travel.utils.JwtUtil;
import com.project.travel.utils.PasswordUtils;
import com.project.travel.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @version 1.0
 * @description: 登陆
 * @date 2024/2/26 21:20
 */
@Controller
@ResponseBody
@RequestMapping("login")
public class LoginController {

    @Autowired
    private UserService userService;
    @Autowired
    private SysHotelOrderService sysHotelOrderService;
    @Autowired
    private SysAttractionOrderService sysAttractionOrderService;

    @Autowired
    private RedisUtils redisUtils;

    @PostMapping()
    public Result login(HttpServletRequest request, @RequestBody JSONObject jsonObject) {
        String username = jsonObject.getString("loginAccount");
        String password = jsonObject.getString("password");
        QueryWrapper<User> query = new QueryWrapper<>();
        query.lambda().eq(User::getLoginAccount,username);
        User user = userService.getOne(query);
        if (user == null) {
            return Result.fail("用户名不存在！");
        }
        //比较加密后得密码
        boolean decrypt = PasswordUtils.decrypt(password, user.getPassword() + "$" + user.getSalt());
        if (!decrypt) {
            return Result.fail("用户名或密码错误！");
        }
        if (user.getStatus() == 1) {
            return Result.fail("用户被禁用！");
        }
        //密码正确生成token返回
        String token = JwtUtil.sign(user.getId(), user.getPassword());
        JSONObject json = new JSONObject();
        json.put("token", token);
        return Result.success(json);
    }

    @GetMapping("logout")
    public Result logout() {
        return Result.success();
    }

    @GetMapping("getManageData")
    public Result getManageData() {
        JSONObject jsonObject = new JSONObject();

        List<String> dates = new ArrayList<>();
        List<Integer> nums = new ArrayList<>();
        List<Integer> orders = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < 7; i++) {
            Date date = calendar.getTime();
            String formattedDate = dateFormat.format(date);
            //最近七日景点预约
            QueryWrapper<SysAttractionOrder> wrapper = new QueryWrapper<>();
            wrapper.lambda().ge(SysAttractionOrder::getCreateTime,formattedDate + " 00:00:00")
                    .le(SysAttractionOrder::getCreateTime,formattedDate + " 23:59:59");
            nums.add(sysAttractionOrderService.count(wrapper));
            //最近七日酒店预约
            QueryWrapper<SysHotelOrder> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().ge(SysHotelOrder::getCreateTime,formattedDate + " 00:00:00")
                    .le(SysHotelOrder::getCreateTime,formattedDate + " 23:59:59");
            orders.add(sysHotelOrderService.count(queryWrapper));
            dates.add(formattedDate);
            calendar.add(Calendar.DAY_OF_YEAR, -1);
        }

        jsonObject.put("dates",dates);
        jsonObject.put("nums",nums);
        jsonObject.put("orders",orders);
        return Result.success(jsonObject);
    }

}
