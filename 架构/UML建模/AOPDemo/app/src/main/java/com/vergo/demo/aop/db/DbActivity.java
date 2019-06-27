package com.vergo.demo.aop.db;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.vergo.demo.aop.R;

import java.lang.reflect.Proxy;

public class DbActivity extends AppCompatActivity implements IDbOperation{

    private IDbOperation iDbOperation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iDbOperation = (IDbOperation) Proxy.newProxyInstance(IDbOperation.class.getClassLoader(),
                new Class[]{IDbOperation.class}, new DbHandler(this));
    }

    public void insert(View view) {
        iDbOperation.insert();
    }

    @Override
    public void insert() {
        Log.e("fhj", "新增数据");
    }

    @Override
    public void delete() {
        Log.e("fhj", "删除数据");
    }

    @Override
    public void update() {
        Log.e("fhj", "修改数据");
    }

    @Override
    public void backup() {
        Log.e("fhj", "保存数据");
    }
}
