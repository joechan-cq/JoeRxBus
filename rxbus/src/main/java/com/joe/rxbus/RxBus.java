package com.joe.rxbus;

import com.joe.rxbus.annotation.AnnotationHandler;
import com.joe.rxbus.annotation.Subscription;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
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

    private List<Subscription> mStickyEvents;

    private AnnotationHandler annotationHandler;

    private EventDispatcher dispatcher;

    private RxBus() {
        subscriberMap = new HashMap<>();
        annotationHandler = new AnnotationHandler(subscriberMap);
        dispatcher = new EventDispatcher();
        mStickyEvents = Collections.synchronizedList(new LinkedList<Subscription>());
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

    /**
     * 常规注册
     */
    public void register(Object master) {
        if (master == null) {
            return;
        }
        synchronized (this) {
            annotationHandler.findActionsFromMaster(master);
        }
    }

    /**
     * 粘性注册
     */
    public void registerSticky(Object master) {
        register(master);
        if (dispatcher == null) {
            dispatcher = new EventDispatcher();
        }
        dispatcher.dispatchStickyEvents(subscriberMap, mStickyEvents);
    }

    /**
     * 注销
     */
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
        dispatcher.dispatch(subscriberMap, new Subscription(event, tag));
    }

    public void postSticky(Object obj) {
        postSticky(obj, DefaultValue.DEFAULT_TAG);
    }

    public void postSticky(Object obj, String tag) {
        if (obj == null) {
            return;
        }
        if (mStickyEvents == null) {
            mStickyEvents = Collections.synchronizedList(new LinkedList<Subscription>());
        }
        Observable<Object> event = Observable.just(obj);
        mStickyEvents.add(new Subscription(event, tag));
    }

    public void removeDefaultStickyEvent() {
        removeStickyEvent(DefaultValue.DEFAULT_TAG);
    }

    public void removeStickyEvent(String tag) {
        if (mStickyEvents != null) {
            Iterator<Subscription> iterator = mStickyEvents.iterator();
            while (iterator.hasNext()) {
                Subscription subscription = iterator.next();
                if (subscription.getTag().equals(tag)) {
                    iterator.remove();
                }
            }
        }
    }

    public void cleanStickyEvent() {
        if (mStickyEvents != null) {
            mStickyEvents.clear();
        }
    }
}
