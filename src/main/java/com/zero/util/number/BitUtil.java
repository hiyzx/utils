package com.zero.util.number;

/**
 * @author yezhaoxing
 * @since 2018/12/07
 * @description 位运算
 */
public class BitUtil {

    /**
     * 增加状态值
     * 
     * @param sourceStatus
     *            原值
     * @param bindStatus
     *            要加的状态值
     * @return 加后的状态值
     */
    public static Integer bindStatus(Integer sourceStatus, Integer bindStatus) {
        return sourceStatus + bindStatus;
    }

    /**
     * 解绑状态值
     * 
     * @param sourceStatus
     *            原状态值
     * @param bindStatus
     *            解绑的状态值
     * @return 解绑后的状态值
     */
    public static Integer unBindStatus(Integer sourceStatus, Integer bindStatus) {
        return sourceStatus - bindStatus;
    }

    /**
     * 判断是否绑定某种状态值
     * 
     * @param sourceStatus
     *            总的状态值
     * @param bindStatus
     *            要判断的状态值
     * @return 是否绑定
     */
    public static Boolean isBindStatus(Integer sourceStatus, Integer bindStatus) {
        return (sourceStatus & bindStatus) == bindStatus;
    }

    public static void main(String[] args) {
        // 初始值
        int sourceStatus = 0;
        // 认证
        sourceStatus = bindStatus(sourceStatus, BitConstants.IS_VERIFIED_STATUS);
        // 判断是否绑定会员
        System.out.println(isBindStatus(sourceStatus, BitConstants.IS_MEMBER_STATUS));
        // 判断是否认证
        System.out.println(isBindStatus(sourceStatus, BitConstants.IS_VERIFIED_STATUS));
        // 绑定会员
        sourceStatus = bindStatus(sourceStatus, BitConstants.IS_MEMBER_STATUS);
        // 判断是否绑定会员
        System.out.println(isBindStatus(sourceStatus, BitConstants.IS_MEMBER_STATUS));
        // 解绑会员
        sourceStatus = unBindStatus(sourceStatus, BitConstants.IS_MEMBER_STATUS);
        // 判断是否绑定会员
        System.out.println(isBindStatus(sourceStatus, BitConstants.IS_MEMBER_STATUS));
    }
}
