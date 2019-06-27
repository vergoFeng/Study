package com.vergo.demo.aop.db;

import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * <p>Created by Fenghj on 2019/6/27.</p>
 */
public class DbHandler implements InvocationHandler {

    private IDbOperation mDbOperation;

    public DbHandler(IDbOperation dbOperation) {
        mDbOperation = dbOperation;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if(mDbOperation != null) {
            Log.e("fhj", "操作数据库之前，进行数据备份...");
            mDbOperation.backup();
            Log.e("fhj", "数据备份完成，进行其他操作");
            return method.invoke(mDbOperation, args);
        }
        return null;
    }
}
