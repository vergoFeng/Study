package com.vergo.modular.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.vergo.froute.annotation.FRouter;

/**
 * <p>Created by Fenghj on 2019/7/24.</p>
 */
@FRouter(path = "/user/UserActivity")
public class UserActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
    }
}
