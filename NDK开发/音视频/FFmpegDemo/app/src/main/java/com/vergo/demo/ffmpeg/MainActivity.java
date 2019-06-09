package com.vergo.demo.ffmpeg;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    SurfaceView surfaceView;
    WangyiPlayer wangyiPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager
                .LayoutParams.FLAG_KEEP_SCREEN_ON);
        surfaceView = findViewById(R.id.surfaceView);
        wangyiPlayer = new WangyiPlayer();
        wangyiPlayer.setSurfaceView(surfaceView);
    }

    public void open(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                openLocalVideo();
            } else {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 123);
            }
        }
    }

    public void soundDecode(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                soundDecode();
            } else {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 456);
            }
        }
    }

    private void openLocalVideo() {
        File file = new File(Environment.getExternalStorageDirectory(), "input.mp4");
        wangyiPlayer.start(file.getAbsolutePath());
    }

    private void soundDecode() {
        File file = new File(Environment.getExternalStorageDirectory(), "input.mp3");
        File outputFile = new File(Environment.getExternalStorageDirectory(), "output.pcm");
        wangyiPlayer.soundDecode(file.getAbsolutePath(), outputFile.getAbsolutePath());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == 123) {
                openLocalVideo();
            } else if (requestCode == 456) {
                soundDecode();
            }
        } else {
            Toast.makeText(this, "权限被禁止，无法获取手机里视频", Toast.LENGTH_SHORT).show();
        }
    }
}
