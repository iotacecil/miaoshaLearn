package com.cloud.miaosha.util;

import org.apache.commons.codec.digest.DigestUtils;

public class md5u {
    public static String md5(String src){
        return DigestUtils.md5Hex(src);
    }
    public static String pwd="123456";

    private static final String salt = "abcd1234";
    public static String inputPassFormPass(String inputPass){
        String passsalt = salt.charAt(0)+salt.charAt(2)+inputPass+salt.charAt(5);
        return md5(passsalt);

    }
    //随机的
    public static String form2DB(String pwd,String salt){
        String passsalt = salt.charAt(0)+salt.charAt(2)+pwd+salt.charAt(5);
        return md5(passsalt);
    }
    //两步合1步
    public static String input2DB(String input,String saltdb){
        String s = inputPassFormPass(pwd);
        String dbpass = form2DB(s, "sadfafd");
        return dbpass;
    }


    public static void main(String[] args) {
        //26718c17fe0b7862a27dd7dc1b532f29
        System.out.println(inputPassFormPass("123456"));
        //ab15a7091a308395d68444871c24814d
        System.out.println(input2DB("123456","salt"));
    }
}
