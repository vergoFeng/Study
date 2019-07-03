package com.vergo.demo.mvvm.vm;

import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.vergo.demo.mvvm.databinding.ActivityLoginBinding;
import com.vergo.demo.mvvm.model.LoginBean;

/**
 * <p>Created by Fenghj on 2019/7/3.</p>
 */
public class LoginViewModel {
    public LoginBean loginBean;

    public LoginViewModel(ActivityLoginBinding loginBinding) {
        loginBean = new LoginBean();
        // 将ViewModel和View进行绑定，通过DataBinding工具
        loginBinding.setLoginViewModel(this);
    }

    public TextWatcher nameInputListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // View层接收到用户的输入，改变Model层的javabean属性
            loginBean.name.set(String.valueOf(s));
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public TextWatcher pwdInputListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // View层接收到用户的输入，改变Model层的javabean属性
            loginBean.password.set(String.valueOf(s));
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public View.OnClickListener loginClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // 模拟网络请求
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // Model层属性的变更，改变View层的显示
                    // userInfo.name.set("Mir Peng");
                    SystemClock.sleep(2000);

                    if ("fhj".equals(loginBean.name.get()) && "123456".equals(loginBean.password.get())) {
                        Log.e("fhj >>> ", "登录成功!");
                    } else {
                        Log.e("fhj >>> ", "登录失败!");
                    }
                }
            }).start();
        }
    };

}
