package com.vergo.demo.exam.dragview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.widget.Toast;

import com.vergo.demo.exam.R;

public class DragViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dragview_activity);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            Toast.makeText(this, "手指触摸在控件范围外", Toast.LENGTH_SHORT).show();
        }
        return super.onTouchEvent(event);
    }
}
