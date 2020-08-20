package com.xiaoqiang.xiaoxin;

/**
 * @author Android-小强
 * @email: 15075818555@163.com
 * @data: on 2020/8/19 10:26
 */
public interface IArcheryView {

    /**
     * 添加目标
     *
     * @param target
     */
    void addTarget(Target target);

    /**
     * 添加射箭结束回调
     *
     * @param onArcheryCompleteListener
     */
    void addOnArcheryCompleteListener(OnArcheryCompleteListener onArcheryCompleteListener);

    /**
     * 射箭结束 回调
     */
    interface OnArcheryCompleteListener {

    }
}
