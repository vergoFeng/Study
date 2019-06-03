package com.vergo.demo.ffmpeg;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private SurfaceView mSurfaceView;
    private FFmpegPlayer fFmpegPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_main);

        mSurfaceView = findViewById(R.id.surfaceView);

        fFmpegPlayer = new FFmpegPlayer();
        fFmpegPlayer.setSurfaceView(mSurfaceView);
    }

    public void open(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                openLocalVideo();
            } else {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 123);
            }
        }
    }

    private void openLocalVideo() {
        File file = new File(Environment.getExternalStorageDirectory()+"/Movies", "input.mp4");
        fFmpegPlayer.start(file.getAbsolutePath());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 123) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openLocalVideo();
            } else {
                Toast.makeText(this, "权限被禁止，无法获取手机里视频", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
