package com.joe.rxbus;

import java.lang.ref.WeakReference;

import rx.functions.Action1;

/**
 * Description
 * Created by chenqiao on 2016/1/15.
 */
public final class EventModel {

    public WeakReference<Object> master;

    public ThreadMode mode;

    public Action1 action;

    public EventModel(Object master, Action1 action, ThreadMode mode) {
        this.master = new WeakReference<>(master);
        this.action = action;
        this.mode = mode;
    }
}
