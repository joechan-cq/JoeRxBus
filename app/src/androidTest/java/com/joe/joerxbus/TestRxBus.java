package com.joe.joerxbus;

import android.test.InstrumentationTestCase;

import com.joe.rxbus.RxBus;
import com.joe.rxbus.ThreadMode;
import com.joe.rxbus.annotation.Subscriber;

import rx.functions.Action1;

/**
 * Description
 * Created by chenqiao on 2016/1/15.
 */
public class TestRxBus extends InstrumentationTestCase {

    public void testBus() throws Throwable {
        RxBus.getInstance().register(this);
        RxBus.getInstance().post("123", "test");
        RxBus.getInstance().post("abc", "test2");
    }

    @Subscriber(tag = "test", mode = ThreadMode.MAIN)
    public Action1 action = new Action1() {
        @Override
        public void call(Object o) {
          //  assertEquals(o.toString(), "123");
        }
    };

    @Subscriber(tag = "test2", mode = ThreadMode.NEW_THREAD)
    public Action1 action2 = new Action1() {
        @Override
        public void call(Object o) {
            assertEquals(o.toString(), "abc");
        }
    };
}
