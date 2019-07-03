package com.vergo.demo.mvp.bean;

/**
 * <p>Created by Fenghj on 2019/7/2.</p>
 */
public class UserInfoBean extends BaseBean {
    private String name;
    private String phone;

    public UserInfoBean(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "UserInfoBean{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
