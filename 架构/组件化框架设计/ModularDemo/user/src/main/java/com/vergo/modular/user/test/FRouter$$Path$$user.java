package com.vergo.modular.user.test;

import com.vergo.froute.annotation.enums.RouterType;
import com.vergo.froute.annotation.model.RouterBean;
import com.vergo.frouter.api.template.FRouterLoadPath;
import com.vergo.modular.user.UserActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * 模拟FRouter路由器的组文件，对应的路径文件
 */
public class FRouter$$Path$$user implements FRouterLoadPath {

    @Override
    public Map<String, RouterBean> loadPath() {
        Map<String, RouterBean> pathMap = new HashMap<>();
        pathMap.put("/user/UserActivity",
                RouterBean.create(RouterType.ACTIVITY, UserActivity.class,
                        "/user/UserActivity", "user"));
        return pathMap;
    }
}