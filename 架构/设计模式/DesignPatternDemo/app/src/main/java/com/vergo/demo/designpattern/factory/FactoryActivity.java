package com.vergo.demo.designpattern.factory;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.vergo.demo.designpattern.R;
import com.vergo.demo.designpattern.factory.impl.IPerson;
import com.vergo.demo.designpattern.factory.impl.PersonImpl;
import com.vergo.demo.designpattern.factory.parameter.ParameterFactory;
import com.vergo.demo.designpattern.factory.properties.PropertiesFactory;
import com.vergo.demo.designpattern.factory.simple.SimpleFactory;

public class FactoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 常规用法
//        IPerson iPerson = new PersonImpl();
//        iPerson.getPersonInfo();

        // 简单工厂：降低了模块间的耦合度
//        IPerson iPerson = SimpleFactory.getPerson();
//        iPerson.getPersonInfo();

        // 拓展：根据参数产生不同的实现
//        IPerson teacherPerson = ParameterFactory.getPerson(1);
//        if(teacherPerson != null) teacherPerson.getPersonInfo();
//
//        IPerson studentPerson = ParameterFactory.getPerson(2);
//        if(studentPerson != null) studentPerson.getPersonInfo();

        // 根据配置文件产生不同的实现
        IPerson teacherPerson = PropertiesFactory.getPerson(this, 1);
        if(teacherPerson != null) teacherPerson.getPersonInfo();

        IPerson studentPerson = PropertiesFactory.getPerson(this, 2);
        if(studentPerson != null) studentPerson.getPersonInfo();
    }
}
