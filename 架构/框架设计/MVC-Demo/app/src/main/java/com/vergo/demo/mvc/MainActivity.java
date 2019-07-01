package com.vergo.demo.mvc;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.vergo.demo.mvc.bean.ImageBean;
import com.vergo.demo.mvc.blf.ICallback;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity implements ICallback {

    private ImageView mImageView;
    private final static String PATH= "http://cmonbaby.com/content/templates/ek_auto/style/images/qrcode.png";

    private MyHandler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageView = findViewById(R.id.iv_image);
        mHandler = new MyHandler(this);
    }

    /**
     * 点击下载图片并加载
     * @param view
     */
    public void getImage(View view) {
        ImageBean imageBean = new ImageBean();
        imageBean.setRequestPath(PATH);
        new ImageDownloader().down(this, imageBean);
    }

    @Override
    public void callback(int resultCode, ImageBean imageBean) {
        // 下载回调
        Message message = mHandler.obtainMessage(resultCode);
        message.obj = imageBean.getBitmap();
        mHandler.sendMessageDelayed(message, 500);
    }

    private static class MyHandler extends Handler {
        private WeakReference<MainActivity> mActivity;

        private MyHandler(MainActivity activity) {
            mActivity = new WeakReference<>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case ImageDownloader.SUCCESS: // 成功
                    mActivity.get().mImageView.setImageBitmap((Bitmap) msg.obj);
                    break;

                case ImageDownloader.ERROR: // 失败
                    Toast.makeText(mActivity.get(), "下载失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}
