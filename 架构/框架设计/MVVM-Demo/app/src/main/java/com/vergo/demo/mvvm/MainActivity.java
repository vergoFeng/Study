package com.vergo.demo.mvvm;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.vergo.demo.mvvm.databinding.ActivityMainBinding;
import com.vergo.demo.mvvm.model.UserBean;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        UserBean userBean = new UserBean("冯慧君", 18);
        activityMainBinding.setUser(userBean);

        userBean.setAge(20);
    }

    public void login(View view) {
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
    }
}
