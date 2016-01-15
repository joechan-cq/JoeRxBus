package com.joe.joerxbus;

import android.test.InstrumentationTestCase;

import com.joe.rxbus.RxBus;

/**
 * Description
 * Created by chenqiao on 2016/1/15.
 */
public class TestRxBus extends InstrumentationTestCase {

    public void testBus() throws Throwable {
        RxBus.getInstance().post("123", "123");
    }
}
