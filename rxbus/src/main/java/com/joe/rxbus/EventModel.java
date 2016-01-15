package com.joe.rxbus;

import rx.functions.Action1;

/**
 * Description
 * Created by chenqiao on 2016/1/15.
 */
public final class EventModel {

    public ThreadMode mode;

    public Action1 action;

    public EventModel(Action1 action, ThreadMode mode) {
        this.action = action;
        this.mode = mode;
    }
}
