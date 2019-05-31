package com.vergo.demo.exam.animator;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.vergo.demo.exam.R;

public class AnimatorActivity extends AppCompatActivity {

    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animator_activity);
        mImageView = findViewById(R.id.imageview);
    }

    public void translation(View view) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mImageView, "translationY", 0, 500, 0);
        objectAnimator.setDuration(3000);
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.start();
    }

    public void rotation(View view) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mImageView, "rotation", 0f, 360f);
        objectAnimator.setDuration(3000);
        objectAnimator.start();
    }

    public void scale(View view) {
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(mImageView, "scaleX", 1f, 0, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(mImageView, "scaleY", 1f, 0, 1f);

        animatorSet.setDuration(3000);
        animatorSet.play(scaleX).with(scaleY);
        animatorSet.start();
    }
}
