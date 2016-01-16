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

    private EventDispatcher dispatcher;

    private RxBus() {
        subscriberMap = new HashMap<>();
        annotationHandler = new AnnotationHandler(subscriberMap);
        dispatcher = new EventDispatcher();
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

    public void register(Object master) {
        if (master == null) {
            return;
        }
        synchronized (this) {
            annotationHandler.findActionsFromMaster(master);
        }
    }

    public void unRegister(Object master) {
        if (master == null) {
            return;
        }
        synchronized (this) {
            annotationHandler.removeMaster(master);
        }
    }

    public void post(Object obj) {
        post(obj, DefaultValue.DEFAULT_TAG);
    }

    public void post(Object obj, String tag) {
        if (obj == null) {
            return;
        }
        //将Object转换为被观察者
        Observable<Object> event = Observable.just(obj);
        if (dispatcher == null) {
            dispatcher = new EventDispatcher();
        }
        dispatcher.dispatch(subscriberMap, tag, event);
    }
}
