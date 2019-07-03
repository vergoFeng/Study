package com.vergo.demo.mvp.httplib;

import com.vergo.demo.mvp.LoginPresenter;
import com.vergo.demo.mvp.bean.UserInfoBean;

// 有可能是共有的Model
public class HttpEngine<P extends LoginPresenter> {

    private P p;

    public HttpEngine(P p) {
        this.p = p;
    }

    public void post(String name, String pwd) {
        if ("fhj".equalsIgnoreCase(name) && "123456".equals(pwd)) {
            p.getContract().responseResult(new UserInfoBean("冯慧君", "13655161543"));
        } else {
            p.getContract().responseResult(null);
        }
    }
}
