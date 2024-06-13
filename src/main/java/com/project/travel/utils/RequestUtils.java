package com.project.travel.utils;

import com.alibaba.fastjson2.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @version 1.0
 * @description: 请求工具类
 * @date 2023/9/14 15:38
 */
public class RequestUtils {

    /**
    * @description: 请求返回
    * @param: response
    	json
    * @return:
    * @date: 2023/9/14 15:39
    */
    public static void returnJson(HttpServletResponse response, String json) throws Exception{
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        try (PrintWriter writer = response.getWriter()) {
            writer.print(json);
        }
    }

    /**
     * 获取目标主机的ip
     */
    public static String getRemoteHost(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
    }

    /**
     * 获取body请求体
     */
    public static JSONObject forwardNtyMsg(HttpServletRequest request) throws IOException {
        // 直接从HttpServletRequest的Reader流中获取RequestBody
        BufferedReader reader = request.getReader();
        StringBuilder builder = new StringBuilder();
        String line = reader.readLine();
        while(line != null){
            builder.append(line);
            line = reader.readLine();
        }
        reader.close();

        String reqBody = builder.toString();
        System.out.println("recv json data:" + reqBody);

        // string 2 jsonobject
        JSONObject json = JSONObject.parseObject(reqBody);

        System.out.println("recv ntydel from:" + request.getRequestURI());

        // todo..

        // return
        JSONObject retjson = new JSONObject();
        retjson.put("recv", "success");
        return retjson;
    }


}
