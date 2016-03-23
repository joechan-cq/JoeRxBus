package com.joe.joerxbus;

import android.test.InstrumentationTestCase;

/**
 * Description
 * Created by chenqiao on 2016/1/15.
 */
public class TestRxBus extends InstrumentationTestCase {

    public void testBus() throws Throwable {
        byte h = 0x01;
        byte l = (byte) 0xfe;
        int result = bytesToInt(h, l);
        System.out.println("Test:" + result);
        assertEquals(result, 510);
    }


    private int bytesToInt(byte h, byte l) {
        int result = 0;
        boolean isMinus = false;
        if ((h & 0x80) == 0b1000_0000) {
            isMinus = true;
            h = (byte) (h & 0x7f);
        }
        result = ((int) h) << 8;
        if (l < 0) {
            result = result + 256 + l;
        } else {
            result += l;
        }
        if (isMinus) {
            return -result;
        } else {
            return result;
        }
    }

}
