package com.joe.rxbus.annotation;

import android.text.TextUtils;

import com.joe.rxbus.DefaultValue;
import com.joe.rxbus.EventModel;
import com.joe.rxbus.ThreadMode;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import rx.functions.Action1;

/**
 * Description
 * Created by chenqiao on 2016/1/15.
 */

public class AnnotationHandler {

    private Map<String, CopyOnWriteArrayList<EventModel>> subscriberMap;

    public AnnotationHandler(Map<String, CopyOnWriteArrayList<EventModel>> subscriberMap) {
        this.subscriberMap = subscriberMap;
    }

    /**
     * 找到实现注解的接口变量
     */
    public void findActionsFromMaster(Object master) {
        Class<?> clazz = master.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            //判断是否为Action1接口，并且有注解修饰
            if (field.isAnnotationPresent(Subscriber.class) && (field.getType() == Action1.class)) {
                Subscriber annotation = field.getAnnotation(Subscriber.class);
                if (annotation != null) {
                    field.setAccessible(true);
                    Action1 action;
                    //获取这个Action1接口
                    try {
                        action = (Action1) field.get(master);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        action = null;
                    }
                    if (action != null) {
                        //获取tag和mode,不存在则设置默认值
                        String tag = annotation.tag();
                        if (TextUtils.isEmpty(tag)) {
                            tag = DefaultValue.DEFAULT_TAG;
                        }
                        ThreadMode mode = annotation.mode();
                        if (mode == null) {
                            mode = ThreadMode.MAIN;
                        }
                        EventModel model = new EventModel(master, action, mode);
                        //插入到Map中
                        insertIntoMap(tag, model);
                    }
                }
            }
        }
    }

    private void insertIntoMap(String tag, EventModel model) {
        CopyOnWriteArrayList<EventModel> list = this.subscriberMap.get(tag);
        if (list == null) {
            list = new CopyOnWriteArrayList<>();
        }
        if (model.master.get() != null) {
            list.add(model);
            this.subscriberMap.put(tag, list);
        }
    }

    public void removeMaster(Object master) {
        for (Map.Entry<String, CopyOnWriteArrayList<EventModel>> entry : subscriberMap.entrySet()) {
            CopyOnWriteArrayList<EventModel> originalList = entry.getValue();
            CopyOnWriteArrayList<EventModel> removeList = new CopyOnWriteArrayList<>();
            if (originalList == null) {
                return;
            }
            for (EventModel model : originalList) {
                //查找所属master的EventModel记录下来，然后移除
                if (model.master.get() == null || model.master.get().equals(master)) {
                    removeList.add(model);
                }
            }
            originalList.removeAll(removeList);
        }
    }
}