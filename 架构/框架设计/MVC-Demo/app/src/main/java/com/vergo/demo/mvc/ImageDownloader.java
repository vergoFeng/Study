package com.vergo.demo.mvc;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.vergo.demo.mvc.bean.ImageBean;
import com.vergo.demo.mvc.blf.ICallback;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageDownloader {

    // 成功
    static final int SUCCESS = 200;

    // 失败
    static final int ERROR = 404;

    public void down(ICallback callback, ImageBean imageBean) {
        new Thread(new DownLoader(callback, imageBean)).start();
    }

    static final class DownLoader implements Runnable {

        private final ICallback callback;
        private final ImageBean imageBean;

        public DownLoader(ICallback callback, ImageBean imageBean) {
            this.callback = callback;
            this.imageBean = imageBean;
        }

        @Override
        public void run() {
            try {
                URL url = new URL(imageBean.getRequestPath());
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("GET");

                if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = httpURLConnection.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    showUi(SUCCESS, bitmap);
                } else {
                    showUi(ERROR, null);
                }
            } catch (Exception e) {
                e.printStackTrace();
                showUi(ERROR, null);
            }
        }

        private void showUi(int resultCode, Bitmap bitmap) {
            if (callback != null) {
                imageBean.setBitmap(bitmap);
                callback.callback(resultCode, imageBean);
            }
        }
    }
}
