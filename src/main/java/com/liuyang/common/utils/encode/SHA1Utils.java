package com.liuyang.common.utils.encode;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class SHA1Utils {

    /**
     * SHA1实现
     *
     * @return 该字符串的sha1校验码
     */
    public static String sha1(String inStr) {
        MessageDigest sha;
        try {
            sha = MessageDigest.getInstance("SHA");
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }

        byte[] byteArray = inStr.getBytes(StandardCharsets.UTF_8);
        byte[] md5Bytes = sha.digest(byteArray);
        StringBuilder hexValue = new StringBuilder();
        for (byte md5Byte : md5Bytes) {
            int val = ((int) md5Byte) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

    public static void main(String[] args) {
        String str = "local exists = redis.call('exists', KEYS[1]) if exists == 0 then return -1 end return redis.call('sismember', KEYS[1], ARGV[1])";
        System.out.println("原始：" + str);
        System.out.println("SHA后：" + sha1(str));
    }
}
 