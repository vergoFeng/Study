package com.vergo.demo.mvc.blf;

import com.vergo.demo.mvc.bean.ImageBean;

/**
 * <p>Created by Fenghj on 2019/7/1.</p>
 */
public interface ICallback {
    /**
     * @param resultCode 请求结果返回标识码
     * @param imageBean Model层数据中bitmap对象（用于C层刷新V）
     */
    void callback(int resultCode, ImageBean imageBean);

}
