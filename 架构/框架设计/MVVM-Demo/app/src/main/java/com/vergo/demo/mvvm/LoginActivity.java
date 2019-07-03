package com.vergo.demo.mvvm;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.vergo.demo.mvvm.databinding.ActivityLoginBinding;
import com.vergo.demo.mvvm.vm.LoginViewModel;

/**
 * <p>Created by Fenghj on 2019/7/3.</p>
 */
public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 1、必须先ReBuilder，2、书写代码绑定
        ActivityLoginBinding loginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        new LoginViewModel(loginBinding);
    }
}
