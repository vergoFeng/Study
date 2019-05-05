package com.vergo.demo.paint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.LightingColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

/**
 * <p>Created by Fenghj on 2019/5/5.</p>
 */
public class PaintView extends View {
    private Paint mPaint;
    public PaintView(Context context) {
        this(context, null);
    }

    public PaintView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PaintView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(); //初始化
//        mPaint.setColor(Color.RED);// 设置颜色
//        mPaint.setARGB(255, 255, 0, 0); // 设置 Paint对象颜色,范围为0~255
//        mPaint.setAlpha(200); // 设置alpha不透明度,范围为0~255
//        mPaint.setAntiAlias(true); // 设置是否抗锯齿效果
//
//        // 画笔样式
//        // Paint.Style.FILL：填充效果
//        // Paint.Style.STROKE：描边效果
//        // Paint.Style.FILL_AND_STROKE：两种效果同时存在
//        mPaint.setStyle(Paint.Style.FILL);
//        mPaint.setStrokeWidth(4); //画笔宽度
//
//        // 画笔线冒样式
//        // Paint.Cap.BUTT：无线冒，默认样式
//        // Paint.Cap.ROUND：圆形线冒
//        // Paint.Cap.SQUARE：方形线冒
//        mPaint.setStrokeCap(Paint.Cap.ROUND);
//
//        // 设置拐角的形状
//        // Paint.Join.MITER：尖角效果，默认效果
//        // Paint.Join.ROUND：圆角效果
//        // Paint.Join.BEVEL：平角效果
//        mPaint.setStrokeJoin(Paint.Join.MITER);

//         // 线性渐变
        Shader linearShader = new LinearGradient(200, 200, 400, 400, Color.RED, Color.BLUE,
                Shader.TileMode.CLAMP);

//        // 辐射渐变
//        Shader shader = new RadialGradient(300, 300, 200, Color.RED, Color.BLUE,
//                Shader.TileMode.CLAMP);

//        // 扫描渐变
//        Shader shader = new SweepGradient(300, 300, Color.RED, Color.BLUE);

        // bitmap着色器
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.avatar);
        Shader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        // 组合着色器
        Shader shader = new ComposeShader(linearShader, bitmapShader, PorterDuff.Mode.MULTIPLY);
        mPaint.setShader(shader);

//        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DARKEN)); //设置图层混合模式
//        mPaint.setColorFilter(new LightingColorFilter(0x00ffff, 0x000000)); //设置颜色过滤器
//        mPaint.setFilterBitmap(true); //设置双线性过滤
//        mPaint.setMaskFilter(new BlurMaskFilter(10, BlurMaskFilter.Blur.NORMAL));//设置画笔遮罩滤镜 ,传入度数和样式
//        mPaint.setTextScaleX(2);// 设置文本缩放倍数
//        mPaint.setTextSize(38);// 设置字体大小
//        mPaint.setTextAlign(Paint.Align.LEFT);//对其方式
//        mPaint.setUnderlineText(true);// 设置下划线
//
//        String str = "Paint画笔高级应用";
//        Rect rect = new Rect();
//        mPaint.getTextBounds(str, 0, str.length(), rect); //测量文本大小，将文本大小信息存放在rect中
//        mPaint.measureText(str); //获取文本的宽
//        mPaint.getFontMetrics(); //获取字体度量对象

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(50, 50, 600, 600, mPaint);
//        canvas.drawCircle(300, 300, 300, mPaint);
    }
}
