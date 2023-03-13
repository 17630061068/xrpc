package com.xzq.util;

import cn.hutool.core.util.StrUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author xzq
 * @Version 1.0.0
 * @Date 2023/3/13 18:58
 */
public class IpUtil {


    // 方法1

    // 方法3
    private static String getNowIP3() throws IOException {
        String ip = null;
        String objWebURL = "https://ip.900cha.com/";
        BufferedReader br = null;
        try {
            URL url = new URL(objWebURL);
            br = new BufferedReader(new InputStreamReader(url.openStream()));
            String s = "";
            String webContent = "";
            while ((s = br.readLine()) != null) {
                if (s.indexOf("我的IP:") != -1) {
                    ip = s.substring(s.indexOf(":") + 1);
                    break;
                }
            }
        } finally {
            if (br != null)
                br.close();
        }
        if (StrUtil.isEmpty(ip)) {
            throw new RuntimeException();
        }
        return ip;
    }
    // 方法4
    private static String getNowIP4() throws IOException {
        String ip = null;
        String objWebURL = "https://bajiu.cn/ip/";
        BufferedReader br = null;
        try {
            URL url = new URL(objWebURL);
            br = new BufferedReader(new InputStreamReader(url.openStream()));
            String s = "";
            String webContent = "";
            while ((s = br.readLine()) != null) {
                if (s.indexOf("互联网IP") != -1) {
                    ip = s.substring(s.indexOf("'") + 1, s.lastIndexOf("'"));
                    break;
                }
            }
        } finally {
            if (br != null)
                br.close();
        }
        if (StrUtil.isEmpty(ip)) {
            throw new RuntimeException();
        }
        return ip;
    }
    public static String getPublicIP() {
        String ip = null;
        // 第三种方式
        try {
            ip = getNowIP3();
            ip.trim();
        } catch (Exception e) {
            System.out.println("getPublicIP - getNowIP3 failed ~ ");
        }
        if (!StrUtil.isEmpty(ip))
            return ip;
        // 第四种方式
        try {
            ip = getNowIP4();
            ip.trim();
        } catch (Exception e) {
            System.out.println("getPublicIP - getNowIP4 failed ~ ");
        }
        if (!StrUtil.isEmpty(ip))
            return ip;
        return ip;
    }

}
