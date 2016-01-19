package com.joe.rxbus;

import com.joe.rxbus.annotation.AnnotationHandler;
import com.joe.rxbus.annotation.Subscription;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
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

    private List<WeakReference<Object>> registedMasters;

    private AnnotationHandler annotationHandler;

    private EventDispatcher dispatcher;

    private RxBus() {
        subscriberMap = new HashMap<>();
        annotationHandler = new AnnotationHandler(subscriberMap);
        dispatcher = new EventDispatcher();
        mStickyEvents = Collections.synchronizedList(new LinkedList<Subscription>());
        registedMasters = new ArrayList<>();
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
            if (!checkRegister(master)) {
                annotationHandler.findActionsFromMaster(master);
            }
        }
    }

    /**
     * 检查是否已经注册
     */
    private boolean checkRegister(Object master) {
        for (WeakReference<Object> item : registedMasters) {
            if (item.get() != null && item.get().equals(master)) {
                return true;
            }
        }
        return false;
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
            removeMasterFromList(master);
            annotationHandler.removeMaster(master);
        }
    }

    private void removeMasterFromList(Object master) {
        int index;
        for (index = 0; index < registedMasters.size(); index++) {
            if (registedMasters.get(index) != null && registedMasters.get(index).get().equals(master)) {
                break;
            }
        }
        if (index >= 0 && index < registedMasters.size()) {
            registedMasters.remove(index);
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
