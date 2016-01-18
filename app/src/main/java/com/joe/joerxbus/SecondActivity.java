package com.joe.joerxbus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.joe.rxbus.RxBus;
import com.joe.rxbus.ThreadMode;
import com.joe.rxbus.annotation.Subscriber;

import rx.functions.Action1;

/**
 * Description
 * Created by chenqiao on 2016/1/16.
 */
public class SecondActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxBus.getInstance().registerSticky(this);
        setContentView(R.layout.second);
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxBus.getInstance().post("123", "test");
                RxBus.getInstance().post("abc", "test2");
            }
        });
    }

    @Subscriber(mode = ThreadMode.MAIN)
    private Action1 action1 = new Action1() {
        @Override
        public void call(Object o) {
            Log.d("Demo", "sticky event receive:  " + o.toString());
        }
    };
}
