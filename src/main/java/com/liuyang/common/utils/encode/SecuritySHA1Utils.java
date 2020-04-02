package com.liuyang.common.utils.encode;

import java.security.MessageDigest;

public class SecuritySHA1Utils {
 
    /**
     * @Comment SHA1实现
     * @Author Ron
     * @Date 2017年9月13日 下午3:30:36
     * @return
     */
    public static String sha1(String inStr) throws Exception {
        MessageDigest sha = null;
        try {
            sha = MessageDigest.getInstance("SHA");
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }
 
        byte[] byteArray = inStr.getBytes("UTF-8");
        byte[] md5Bytes = sha.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }
 
    public static void main(String args[]) throws Exception {
        String str = new String("local exists = redis.call('exists', KEYS[1]) if exists == 0 then return -1 end return redis.call('sismember', KEYS[1], ARGV[1])");
        System.out.println("原始：" + str);
        System.out.println("SHA后：" + sha1(str));
    }
}
 