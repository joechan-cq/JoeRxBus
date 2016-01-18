package com.joe.rxbus.annotation;

import com.joe.rxbus.DefaultValue;

import rx.Observable;

/**
 * Description
 * Created by chenqiao on 2016/1/18.
 */
public final class Subscription {
    private String tag = DefaultValue.DEFAULT_TAG;

    public Observable<Object> getEvent() {
        return event;
    }

    private Observable<Object> event;

    public String getTag() {
        return tag;
    }

    public Subscription(Observable<Object> event, String tag) {
        this.event = event;
        this.tag = tag;
    }
}
