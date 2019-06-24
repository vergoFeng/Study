package com.vergo.demo.designpattern.factory.properties;

import android.content.Context;

import com.vergo.demo.designpattern.R;
import com.vergo.demo.designpattern.factory.impl.IPerson;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * <p>Created by Fenghj on 2019/6/24.</p>
 */
public class PropertiesFactory {
    public static IPerson getPerson(Context context, int personType) {

        try {
            // 加载配置文件
            Properties properties = new Properties();

            // assets下
            InputStream inputStream = context.getAssets().open("config.properties");
            // raw下
//            InputStream inputStream = context.getResources().openRawResource(R.raw.config);
            // java的写法
//            InputStream inputStream = PropertiesFactory.class.getResourceAsStream("assets/config.properties");

            properties.load(inputStream);

            Class aClass;
            if(personType == 1) {
                aClass = Class.forName(properties.getProperty("teacher_info"));
                return (IPerson) aClass.newInstance();
            } else if(personType == 2) {
                aClass = Class.forName(properties.getProperty("student_info"));
                return (IPerson) aClass.newInstance();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
