package com.vergo.demo.aop.db;

/**
 * <p>Created by Fenghj on 2019/6/27.</p>
 */
public interface IDbOperation {
    void insert();
    void delete();
    void update();

    // 数据备份
    void backup();
}
