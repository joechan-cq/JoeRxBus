package com.joe.rxbus;

/**
 * Description
 * Created by chenqiao on 2016/1/15.
 */
public enum ThreadMode {
    /**
     * 将事件执行在UI线程
     */
    MAIN,
    /**
     * 在发布线程执行
     */
    POST,
    /**
     * 将事件执行在一个子线程中
     */
    ASYNC
}
