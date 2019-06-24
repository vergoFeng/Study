package com.vergo.demo.designpattern.factory.bean;

/**
 * <p>Created by Fenghj on 2019/6/24.</p>
 */
public class PersonBean {
    private String name;
    private int age;

    public PersonBean() {

    }

    public PersonBean(String name) {
        this.name = name;
    }

    public PersonBean(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "PersonBean{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
