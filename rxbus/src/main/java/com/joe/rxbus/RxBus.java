package com.joe.rxbus;

import com.joe.rxbus.annotation.AnnotationHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import rx.Observable;

/**
 * Description
 * Created by chenqiao on 2016/1/15.
 */
public class RxBus {

    private static RxBus instance;

    private Map<String, CopyOnWriteArrayList<EventModel>> subscriberMap;

    private AnnotationHandler annotationHandler;

    private RxBus() {
        subscriberMap = new HashMap<>();
        annotationHandler = new AnnotationHandler();
    }

    //单例模式
    public static RxBus getInstance() {
        if (instance == null) {
            synchronized (RxBus.class) {
                if (instance == null) {
                    instance = new RxBus();
                }
            }
        }
        return instance;
    }

    public void post(Object obj, String tag) {
        //将Object转换为被观察者
        Observable<Object> event = Observable.just(obj);
    }

    public void registerMaster(Object master) {
        if (master == null) {
            return;
        }
        synchronized (this) {

        }
    }
}
