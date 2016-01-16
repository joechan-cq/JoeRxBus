package com.joe.joerxbus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.joe.rxbus.RxBus;
import com.joe.rxbus.ThreadMode;
import com.joe.rxbus.annotation.Subscriber;

import rx.functions.Action1;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RxBus.getInstance().register(this);
        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SecondActivity.class));
            }
        });
    }

    @Subscriber(tag = "test", mode = ThreadMode.MAIN)
    public Action1 action = new Action1() {
        @Override
        public void call(Object o) {
            Log.d("Demo", "action:" + o.toString());
        }
    };

    @Subscriber(tag = "test2", mode = ThreadMode.NEW_THREAD)
    public Action1 action2 = new Action1() {
        @Override
        public void call(Object o) {
            Log.d("Demo", "action2:" + o.toString());
        }
    };

    @Override
    protected void onDestroy() {
        Log.d("demo", "ondestroy");
        super.onDestroy();
        RxBus.getInstance().unRegister(this);
    }
}