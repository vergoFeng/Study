package com.vergo.modular.user;

import com.vergo.frouter.api.template.IRouterParameter;

/**
 * <p>Created by Fenghj on 2019/9/19.</p>
 */
public class UserActivity$$Parameter implements IRouterParameter {
    @Override
    public void loadParameter(Object target) {
        UserActivity t = (UserActivity) target;

        t.userid = t.getIntent().getStringExtra("userid");
        t.username = t.getIntent().getStringExtra("username");
        t.age = t.getIntent().getIntExtra("userid", t.age);
    }
}
