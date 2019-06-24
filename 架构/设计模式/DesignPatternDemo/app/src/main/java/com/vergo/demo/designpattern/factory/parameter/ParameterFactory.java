package com.vergo.demo.designpattern.factory.parameter;

import com.vergo.demo.designpattern.factory.impl.IPerson;
import com.vergo.demo.designpattern.factory.impl.StudentImpl;
import com.vergo.demo.designpattern.factory.impl.TeacherImpl;

/**
 * <p>Created by Fenghj on 2019/6/24.</p>
 */
public class ParameterFactory {
    /**
     * 根据人的类型获取不同的实现
     * @param personType 人的类型
     */
    public static IPerson getPerson(int personType) {
        switch (personType) {
            case 1:
                return new TeacherImpl();
            case 2:
                return new StudentImpl();
        }
        return null;
    }
}
