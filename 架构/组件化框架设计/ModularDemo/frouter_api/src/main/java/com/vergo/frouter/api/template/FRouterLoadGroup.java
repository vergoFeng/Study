package com.vergo.frouter.api.template;

import java.util.Map;

/**
 * 路由组Group对外提供加载数据接口
 * <p>Created by Fenghj on 2019/7/26.</p>
 */
public interface FRouterLoadGroup {
    /**
     * 加载路由组Group数据
     * 比如："app", ARouter$$Path$$app.class（实现了ARouterLoadPath接口）
     *
     * @return key:"app", value:"app"分组对应的路由详细对象类
     */
    Map<String, Class<? extends FRouterLoadPath>> loadGroup();
}
