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

    public static String getPrivateIp() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    if (!inetAddress.isLinkLocalAddress() && !inetAddress.isLoopbackAddress()
                            && inetAddress instanceof java.net.Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String getPublicIp() {
        try {
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
        } catch (Exception e) {
            throw new RuntimeException("没有可用的公网ip");
        }
    }

    public static String getIp() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    if (!inetAddress.isSiteLocalAddress()
                            && !inetAddress.isLinkLocalAddress()
                            && !inetAddress.isLoopbackAddress()
                    ) {
                        return inetAddress.getHostAddress();
                    }
                }
            }


            //如果没有公网Ip ,那么就获取局域网Ip
            String privateIp = getPrivateIp();
            if (ObjectUtil.notNull(privateIp)) {
                return privateIp;
            }
            //如果没有局域网IP ,那么就本机回环地址127.0.0.1
            return "127.0.0.1";
        } catch (SocketException ex) {
            ex.printStackTrace();
        }

        throw new RuntimeException("获取本机IP地址失败");
    }

}
