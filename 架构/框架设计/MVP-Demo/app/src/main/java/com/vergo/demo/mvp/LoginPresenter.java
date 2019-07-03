package com.vergo.demo.mvp;

import com.vergo.demo.mvp.base.BasePresenter;
import com.vergo.demo.mvp.bean.BaseBean;
import com.vergo.demo.mvp.bean.UserInfoBean;
import com.vergo.demo.mvp.httplib.HttpEngine;

/**
 * <p>Created by Fenghj on 2019/7/3.</p>
 */
public class LoginPresenter extends BasePresenter<LoginActivity, LoginModel, LoginContract.IPresenter> {
    @Override
    public LoginModel getModel() {
        return new LoginModel(this);
    }

    @Override
    public LoginContract.IPresenter getContract() {
        return new LoginContract.IPresenter() {
            @Override
            public void requestLogin(String name, String password) {
                try {
                    // 三种风格（P层很极端，要么不做事只做转发，要么就是拼命一个人干活）
                    m.getContract().doLogin(name, password);

                    // 第二种，让功能模块去工作（Library：下载、请求、图片加载）
//                    HttpEngine engine = new HttpEngine<>(LoginPresenter.this);
//                    engine.post(name, password);

                    // P层自己处理（谷歌例子）
//                    if ("fhj".equalsIgnoreCase(name) && "123456".equals(password)) {
//                        responseResult(new UserInfoBean("冯慧君", "13655161543"));
//                    } else {
//                        responseResult(null);
//                    }

                    // 内存泄露测试
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            SystemClock.sleep(50000);
//                        }
//                    }).start();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void responseResult(BaseBean baseBean) {
                getView().getContract().handlerResult(baseBean);
            }
        };
    }
}
