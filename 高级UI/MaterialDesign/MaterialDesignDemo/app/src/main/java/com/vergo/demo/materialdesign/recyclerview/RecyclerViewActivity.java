package com.vergo.demo.materialdesign.recyclerview;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vergo.demo.materialdesign.R;

public class RecyclerViewActivity extends AppCompatActivity {
    private RelativeLayout mSuspensionBar;
    private TextView mSuspensionTv;
    private ImageView mSuspensionIv;

    private int mSuspensionHeight;
    private int mCurrentPosition; // 当前bar中所展现信息对应item的position
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);

        initSuspensionBar();
        initRecyclerView();
    }

    /**
     * 初始化悬浮bar
     */
    private void initSuspensionBar() {
        mSuspensionBar = findViewById(R.id.suspension_bar);
        mSuspensionTv = findViewById(R.id.tv_nickname);
        mSuspensionIv = findViewById(R.id.iv_avatar);

        // 初始默认展现第一个的信息
        updateSuspensionBar();

        // 由于在onCreate方法中无法获取控件的高度，使用这种方式来获取
        mSuspensionBar.post(new Runnable() {
            @Override
            public void run() {
                // 获取悬浮条的高度
                mSuspensionHeight = mSuspensionBar.getHeight();
            }
        });
    }

    /**
     * 初始化recyclerView
     */
    private void initRecyclerView() {
        RecyclerView mRecyclerView = findViewById(R.id.recyclerview);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        FeedAdapter mFeedAdapter = new FeedAdapter();
        mRecyclerView.setAdapter(mFeedAdapter);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // 对悬浮条的位置进行调整，有一个推出的动画效果
                // 获取下一个item view
                View view = layoutManager.findViewByPosition(mCurrentPosition + 1);
                if(view != null) {
                    if(view.getTop() <= mSuspensionHeight) {
                        // 当下一个View的距顶部的距离小于等于bar的高度时，表示下一个item已经开始在往上滑
                        // 设置悬浮条的移除效果
                        mSuspensionBar.setY(view.getTop() - mSuspensionHeight);
                    } else {
                        mSuspensionBar.setY(0);
                    }
                }

                // 判断屏幕上第一个可见的item的position是否等于mCurrentPosition
                if(mCurrentPosition != layoutManager.findFirstVisibleItemPosition()) {
                    // 如果不相等，将mCurrentPosition赋值，并且更新bar上信息
                    mCurrentPosition = layoutManager.findFirstVisibleItemPosition();
                    updateSuspensionBar();
                }
            }
        });
    }

    /**
     * 更新悬浮bar中头像和昵称
     */
    private void updateSuspensionBar() {
        //用户头像
        Glide.with(this)
                .load(getAvatarResId(mCurrentPosition))
                .centerInside()
                .into(mSuspensionIv);
        mSuspensionTv.setText("NetEase " + mCurrentPosition);
    }

    private int getAvatarResId(int position){
        switch (position % 4){
            case 0:
                return R.drawable.avatar1;
            case 1:
                return R.drawable.avatar2;
            case 2:
                return R.drawable.avatar3;
            case 3:
                return R.drawable.avatar4;
        }
        return 0;
    }
}
