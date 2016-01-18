package com.joe.rxbus;

import com.joe.rxbus.annotation.Subscription;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Description  事件分发处理器
 * Created by chenqiao on 2016/1/16.
 */
public class EventDispatcher {
    public void dispatch(Map<String, CopyOnWriteArrayList<EventModel>> subscriberMap, Subscription subscription) {
        if (subscription == null) {
            return;
        }
        CopyOnWriteArrayList<EventModel> matchList = subscriberMap.get(subscription.getTag());
        if (matchList == null || matchList.size() == 0) {
            return;
        }
        Observable<Object> event = subscription.getEvent();
        if (event == null) {
            return;
        }
        for (EventModel model : matchList) {
            if (model.master.get() != null) {
                switch (model.mode) {
                    case MAIN:
                        event.observeOn(AndroidSchedulers.mainThread()).subscribe(model.action);
                        break;
                    case IO:
                        event.observeOn(Schedulers.io()).subscribe(model.action);
                        break;
                    case NEW_THREAD:
                        event.observeOn(Schedulers.newThread()).subscribe(model.action);
                        break;
                    case IMMEDIATE:
                        event.observeOn(Schedulers.immediate()).subscribe(model.action);
                        break;
                    default:
                        event.observeOn(AndroidSchedulers.mainThread()).subscribe(model.action);
                        break;
                }
            }
        }
    }

    public void dispatchStickyEvents(Map<String, CopyOnWriteArrayList<EventModel>> subscriberMap, List<Subscription> mStickyEvents) {
        for (Subscription subscription : mStickyEvents) {
            dispatch(subscriberMap, subscription);
        }
    }
}
