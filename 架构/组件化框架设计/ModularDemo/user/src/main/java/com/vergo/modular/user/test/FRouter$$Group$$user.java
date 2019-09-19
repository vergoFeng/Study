package com.vergo.modular.user.test;

import com.vergo.frouter.api.template.FRouterLoadGroup;
import com.vergo.frouter.api.template.FRouterLoadPath;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>Created by Fenghj on 2019/9/18.</p>
 */
public class FRouter$$Group$$user implements FRouterLoadGroup {
    @Override
    public Map<String, Class<? extends FRouterLoadPath>> loadGroup() {
        Map<String, Class<? extends FRouterLoadPath>> groupMap = new HashMap<>();
        groupMap.put("user", FRouter$$Path$$user.class);
        return groupMap;
    }
}
