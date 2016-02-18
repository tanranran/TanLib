package cn.tan.lib.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Tan on 2015/8/24.
 */
public class MD5Util {
    /**
     * MD5加密
     */
    public static String md5(String string,boolean is32) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(
                    string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        if(is32){
            return hex.toString();
        }else{
            return hex.toString().substring(8,24);//16位的加密
        }
    }
    public static String md532(String string) {
        return md5(string,true);
    }
    public static String md516(String string) {
        return md5(string,false);
    }
}
