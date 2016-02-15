package com.joe.rxbus;

import android.util.Log;

import com.joe.rxbus.annotation.Subscription;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
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
                        event.observeOn(AndroidSchedulers.mainThread()).subscribe(model.action, errorAction);
                        break;
                    case IO:
                        event.observeOn(Schedulers.io()).subscribe(model.action, errorAction);
                        break;
                    case NEW_THREAD:
                        event.observeOn(Schedulers.newThread()).subscribe(model.action, errorAction);
                        break;
                    case IMMEDIATE:
                        event.observeOn(Schedulers.immediate()).subscribe(model.action, errorAction);
                        break;
                    default:
                        event.observeOn(AndroidSchedulers.mainThread()).subscribe(model.action, errorAction);
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

    private Action1<Throwable> errorAction = new Action1<Throwable>() {
        @Override
        public void call(Throwable throwable) {
            Log.e("RxBus", "occur error:" + throwable.getMessage());
        }
    };
}
