package com.joe.joerxbus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.joe.rxbus.RxBus;

/**
 * Description
 * Created by chenqiao on 2016/1/16.
 */
public class SecondActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second);
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxBus.getInstance().post("123", "test");
                RxBus.getInstance().post("abc", "test2");
            }
        });
    }
}
