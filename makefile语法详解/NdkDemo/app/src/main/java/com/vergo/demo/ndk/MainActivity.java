package com.vergo.demo.ndk;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    {
        System.loadLibrary("hello-jni");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = findViewById(R.id.textview);
        textView.setText("nativeTest()："+nativeTest());
    }
    native int nativeTest();
}
