package com.vergo.demo.mvp;

import com.vergo.demo.mvp.base.BaseModel;
import com.vergo.demo.mvp.bean.UserInfoBean;

/**
 * <p>Created by Fenghj on 2019/7/3.</p>
 */
public class LoginModel extends BaseModel<LoginPresenter, LoginContract.IModel> {
    public LoginModel(LoginPresenter loginPresenter) {
        super(loginPresenter);
    }

    @Override
    public LoginContract.IModel getContract() {
        return new LoginContract.IModel() {
            @Override
            public void doLogin(String name, String password) throws Exception {
                if ("fhj".equalsIgnoreCase(name) && "123456".equals(password)) {
                    p.getContract().responseResult(new UserInfoBean("冯慧君", "13655161543"));
                } else {
                    p.getContract().responseResult(null);
                }
            }
        };
    }
}
