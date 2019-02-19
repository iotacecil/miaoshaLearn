package com.cloud.miaosha.redis;

public class AccessKey extends BasePrefix {
    private AccessKey( int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    // 比枚举好 因为可以new一个动态参数的
    public static AccessKey withExpire(int expireSeconds) {
        return new AccessKey(expireSeconds, "access");
    }
}
