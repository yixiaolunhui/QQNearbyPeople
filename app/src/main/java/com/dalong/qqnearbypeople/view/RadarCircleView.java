package com.dalong.qqnearbypeople.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * 扫描出现的圆形人   可能是图片也可能是纯色
 * Created by dalong on 2016/12/20.
 */

public class RadarCircleView extends View {
    //画笔
    private Paint mPaint;
    //图片
    private Bitmap mBitmap;
    //半径
    private float radius = dp2px(getContext(),7);
    //位置X
    private float disX;
    //位置Y
    private float disY;
    //旋转的角度
    private float angle;
    //根据远近距离的不同计算得到的应该占的半径比例
    private float proportion;
    public RadarCircleView(Context context) {
        this(context,null);
    }

    public RadarCircleView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RadarCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.parseColor("#FF90A2"));
        mPaint.setAntiAlias(true);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measureSize(widthMeasureSpec), measureSize(heightMeasureSpec));
    }

    private int measureSize(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = dp2px(getContext(),18);
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    /**
     *  将dp值转换为px值
     * @param context
     * @param dpValue
     * @return
     */
    public  int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //画圆
        canvas.drawCircle(radius, radius, radius, mPaint);
        //如果mBitmap不为空再画小人图
        if (mBitmap != null) {
            canvas.drawBitmap(mBitmap, null, new Rect(0, 0, 2 * (int) radius, 2 * (int) radius), mPaint);
        }
    }

    /**
     * 设置画笔的颜色
     * @param resId
     */
    public void setPaintColor(int resId) {
        mPaint.setColor(resId);
        invalidate();
    }

    /**
     * 设置真实小人icon id
     * @param resId
     */
    public void setPortraitIcon(int resId) {
        mBitmap = BitmapFactory.decodeResource(getResources(), resId);
        invalidate();
    }

    /**
     * 清楚真实小人icon
     */
    public void clearPortaitIcon(){
        mBitmap = null;
        invalidate();
    }



    public float getProportion() {
        return proportion;
    }

    public void setProportion(float proportion) {
        this.proportion = proportion;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public float getDisX() {
        return disX;
    }

    public void setDisX(float disX) {
        this.disX = disX;
    }

    public float getDisY() {
        return disY;
    }

    public void setDisY(float disY) {
        this.disY = disY;
    }
}
