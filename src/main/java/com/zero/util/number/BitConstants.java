package com.zero.util.number;

/**
 * @author yezhaoxing
 * @since 2018/12/07
 */
public class BitConstants {

    // 绑定邮箱
    public static final Integer BIND_EMAIL_STATUS = 2 >> 1;

    // 绑定手机号
    public static final Integer BIND_PHONE_STATUS = 2;

    // 是否实名认证
    public static final Integer IS_VERIFIED_STATUS = 2 << 1;

    // 是否会员
    public static final Integer IS_MEMBER_STATUS = 2 << 2;
}
