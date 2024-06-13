package com.project.travel.utils;

import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.util.UUID;

/**
* @description: 密码工具类
* @date 2023/9/1 10:20
* @version 1.0
*/
public class PasswordUtils {

    /**
    * @description: 加盐加密
    * @param: password
    * @return: String
    * @date: 2023/9/1 10:21
    */
    public static String encrypt(String password) {
        // 1.产生盐值
        String salt = UUID.randomUUID().toString().replace("-", "");
        // 2.使用(盐值+明文密码)得到加密的密码
        String finalPassword = DigestUtils.md5DigestAsHex((salt + password).getBytes());
        // 3.将盐值和加密的密码共同返回（合并盐值和加密密码）
        String dbPassword = salt + "$" + finalPassword;
        return dbPassword;
    }

    /**
     * @description: 加盐加密
     * @param: password
     * @return: String
     * @date: 2023/9/1 10:21
     */
    public static String encrypt(String password, String salt) {
        // 1.使用(盐值+明文密码)得到加密的密码
        String finalPassword = DigestUtils.md5DigestAsHex((salt + password).getBytes());
        // 2.将盐值和加密的密码共同返回（合并盐值和加密密码）
        String dbPassword = salt + "$" + finalPassword;
        return dbPassword;
    }

    /**
     * @description: 验证加盐加密密码
     * @param: password
     * @return: String
     * @date: 2023/9/1 10:21
     */
    public static boolean decrypt(String password, String dbPassword) {
        boolean result = false;
        // 参数正确
        if (StringUtils.hasLength(password) && StringUtils.hasLength(dbPassword) &&
                dbPassword.length() == 65 && dbPassword.contains("$")) {
            // 1.得到盐值
            String[] passwrodArr = dbPassword.split("\\$");
            // 1.1 盐值
            String salt = passwrodArr[0];
            // 2.生成验证密码的加盐加密密码
            String checkPassword = encrypt(password, salt);
            if (dbPassword.equals(checkPassword)) {
                result = true;
            }
        }
        return result;
    }
}
