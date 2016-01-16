package com.joe.rxbus;

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
    public void dispatch(Map<String, CopyOnWriteArrayList<EventModel>> subscriberMap, String tag, Observable<Object> event) {
        CopyOnWriteArrayList<EventModel> matchList = subscriberMap.get(tag);
        if (matchList == null || matchList.size() == 0) {
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
}
