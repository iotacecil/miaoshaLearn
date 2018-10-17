package com.cloud.miaosha.redis;

public class tokenKey extends BasePrefix {
    public tokenKey(String prefix) {
        super(prefix);
    }
    public static tokenKey token = new tokenKey("tk");

}
