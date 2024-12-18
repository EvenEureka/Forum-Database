package com.zhihu.zhihubackend.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Util {

    protected static char hexDigits[] = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};

    protected static MessageDigest messagedigest = null;

    static {
        try {
            messagedigest = MessageDigest.getInstance("MD5");
        }catch (NoSuchAlgorithmException nsaex) {
            System.err.println(Md5Util.class.getName() + "初始化失败，MessageDigest不支持MD5Util.");
            nsaex.printStackTrace();
        }
    }

    /**
     * 生成字符串的md5校验值
     * @param s
     * @return
     */
    public static String getMD5String(String s)
    {
        return getMD5String(s.getBytes());
    }

    /**
     * 转换得到字节数组，再转为字符串
     * @param bytes
     * @return
     */
    private static String getMD5String(byte[] bytes) {
        byte[] digest = messagedigest.digest(bytes);
        return byteArrayToHex(digest);
    }

    /**
     * 将字节数组转换成16进制字符串
     * @param digest
     * @return
     */
    private static String byteArrayToHex(byte[] digest) {
        char[] chars = new char[digest.length * 2];

        int index = 0;
        for (byte b : digest) {
            chars[index++] = hexDigits[b >>> 4 & 0xf];
            chars[index++] = hexDigits[b & 0xf];
        }
        return new String(chars);
    }


}
