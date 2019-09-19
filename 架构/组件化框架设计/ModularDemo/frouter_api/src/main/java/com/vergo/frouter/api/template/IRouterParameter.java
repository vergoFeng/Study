package com.vergo.frouter.api.template;

/**
 * 参数Parameter加载接口
 * <p>Created by Fenghj on 2019/9/19.</p>
 */
public interface IRouterParameter {
    /**
     * 目标对象.属性名 = getIntent().属性类型("注解值or属性名");完成赋值
     *
     * @param target 目标对象，如：MainActivity（中的某些属性）
     */
    void loadParameter(Object target);
}
