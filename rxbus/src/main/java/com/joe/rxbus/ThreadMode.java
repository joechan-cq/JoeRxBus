package com.joe.rxbus;

/**
 * Description
 * Created by chenqiao on 2016/1/15.
 */
public enum ThreadMode {
    /**
     * 在主线程执行
     */
    MAIN,
    /**
     * 在读写线程执行
     */
    IO,
    /**
     * 新开线程执行
     */
    NEW_THREAD,
    /**
     * 在当前线程执行
     */
    IMMEDIATE
}
