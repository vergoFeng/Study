package com.vergo.demo.mvp;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.vergo.demo.mvp.base.BaseView;
import com.vergo.demo.mvp.bean.BaseBean;

public class LoginActivity extends BaseView<LoginPresenter, LoginContract.IView> {
    private EditText nameEt;
    private EditText pwdEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    // 初始化控件
    private void initView() {
        nameEt = findViewById(R.id.et_name);
        pwdEt = findViewById(R.id.et_pwd);
    }

    @Override
    public LoginPresenter getPresenter() {
        return new LoginPresenter();
    }

    @Override
    public LoginContract.IView getContract() {
        return new LoginContract.IView() {
            @Override
            public void handlerResult(BaseBean baseBean) {
                if (baseBean != null) {
                    Toast.makeText(LoginActivity.this, baseBean.toString(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "登录失败！", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    public void doLoginAction(View view) {
        String name = nameEt.getText().toString();
        String pwd = pwdEt.getText().toString();

        // 发起需求，让Presenter处理
        presenter.getContract().requestLogin(name, pwd);
    }
}
