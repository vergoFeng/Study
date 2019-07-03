package com.vergo.demo.mvp;

import com.vergo.demo.mvp.bean.BaseBean;

/**
 * 将Model层、View层、Presenter层协商的共同业务，封装成接口
 * 契约、合同
 * <p>Created by Fenghj on 2019/7/3.</p>
 */

public interface LoginContract {
    interface IModel {
        // Model层子类完成方法的具体实现 ----------------2
        void doLogin(String name, String password) throws Exception;
    }

    interface IView<T extends BaseBean> {
        // 真实的项目中，请求结果往往是以javabean --------------4
        void handlerResult(T t);
    }

    interface IPresenter<T extends BaseBean> {
        // 登录请求（接收到View层指令，可以自己做，也可以让Model层去执行）-----------1
        void requestLogin(String name, String password);

        // 结果响应（接收到Model层处理的结果，通知View层刷新）---------------3
        void responseResult(T t);
    }
}
