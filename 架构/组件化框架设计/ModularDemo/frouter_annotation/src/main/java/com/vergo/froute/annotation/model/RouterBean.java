package com.vergo.froute.annotation.model;

import com.vergo.froute.annotation.enums.RouterType;

import javax.lang.model.element.Element;

/**
 * 路由路径Path的最终实体封装类
 * 比如：app分组中的MainActivity对象，这个对象有更多的属性
 * <p>Created by Fenghj on 2019/7/26.</p>
 */
public class RouterBean {
    // 枚举类型
    private RouterType type;
    // 类节点
    private Element element;
    // 被@FRouter注解的类对象
    private Class<?> clazz;
    // 路由的组名
    private String group;
    // 路由的地址
    private String path;

    private RouterBean(Builder builder) {
        this.element = builder.element;
        this.path = builder.path;
        this.group = builder.group;
    }

    private RouterBean(RouterType type, Class<?> clazz, String path, String group) {
        this.type = type;
        this.clazz = clazz;
        this.path = path;
        this.group = group;
    }

    // 对外提供简易版构造方法，主要是为了方便APT生成代码
    public static RouterBean create(RouterType type, Class<?> clazz, String path, String group) {
        return new RouterBean(type, clazz, path, group);
    }

    public RouterType getType() {
        return type;
    }

    public Element getElement() {
        return element;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public String getGroup() {
        return group;
    }

    public String getPath() {
        return path;
    }

    public void setType(RouterType type) {
        this.type = type;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public static class Builder {

        private Element element;
        private String group;
        private String path;

        public Builder setElement(Element element) {
            this.element = element;
            return this;
        }

        public Builder setGroup(String group) {
            this.group = group;
            return this;
        }

        public Builder setPath(String path) {
            this.path = path;
            return this;
        }

        public RouterBean build() {
            if (path == null || path.length() == 0) {
                throw new IllegalArgumentException("path必填项为空，如：/app/MainActivity");
            }
            return new RouterBean(this);
        }
    }
}
