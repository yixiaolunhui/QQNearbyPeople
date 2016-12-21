package com.dalong.qqnearbypeople.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

import com.dalong.qqnearbypeople.R;

/**
 * 扫描view
 * Created by dalong on 2016/12/20.
 */

public class RadarView extends View {

    //圆环颜色
    public int mRingColor;
    //扫描背景色
    public int mScanBgColor;
    //圆环的看度
    public float mRingWidth;
    //圆环数量
    public int mRingNum;
    //扫描速度 越小越快  毫秒值
    public int mScanSpeed;
    //扫描角度
    private  int mScanAngle;
    //圆环画笔
    private  Paint mRingPaint;
    //中间图片
    private  Paint mCicleIconPaint;
    //扫描画笔
    private  Paint mScanPaint;
    //中间图片
    private Bitmap mCenterIcon;
    //宽
    private int mWidth;
    //高
    private int mHeight;
    //圆环比例
    private float mRingScale=1/13f;

    private SweepGradient mScanShader;
    //旋转需要的矩阵
    private Matrix matrix = new Matrix();

    private OnScanningListener mOnScanningListener;
    //是否开始回调
    private boolean startScan;
    //当前扫描的次数
    private int currentScanningCount;
    //当前扫描显示的item
    private int currentScanningItem;
    //最大扫描次数
    private int maxScanItemCount;
    private float currentScanAngle;
    public RadarView(Context context) {
        this(context,null);
    }

    public RadarView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RadarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray=context.obtainStyledAttributes(attrs, R.styleable.RadarView);
        mRingColor=typedArray.getColor(R.styleable.RadarView_mRingColor, Color.parseColor("#3C8EAE"));
        mScanBgColor=typedArray.getColor(R.styleable.RadarView_mScanBgColor, Color.parseColor("#84B5CA"));
        mRingWidth=typedArray.getDimensionPixelSize(R.styleable.RadarView_mRingWidth, 1);
        mRingNum=typedArray.getInteger(R.styleable.RadarView_mRingNum, 6);
        mScanSpeed=typedArray.getColor(R.styleable.RadarView_mScanSpeed, 20);
        mScanAngle=typedArray.getColor(R.styleable.RadarView_mScanAngle, 5);
        typedArray.recycle();

        //中间图片
        mCenterIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.dabai);
        //设置多个圆环画笔
        mRingPaint=new Paint();
        mRingPaint.setColor(mRingColor);
        mRingPaint.setAntiAlias(true);
        mRingPaint.setStrokeWidth(mRingWidth);
        mRingPaint.setStyle(Paint.Style.STROKE);


        //设置中间图片画笔
        mCicleIconPaint=new Paint();
        mCicleIconPaint.setColor(Color.WHITE);
        mCicleIconPaint.setAntiAlias(true);


        //扫描画笔
        mScanPaint = new Paint();
        mScanPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        //启动扫描
        post(mRunnable);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureSize(widthMeasureSpec),measureSize(heightMeasureSpec));
    }

    /**
     * 测量宽高  默认给400
     * @param measureSpec
     * @return
     */
    private int measureSize(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = 400;
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 这里设置宽高  主要思想是因为是方形 所以取最小的作为宽高值
        mWidth=getMeasuredWidth();
        mHeight=getMeasuredHeight();
        mWidth = mHeight = Math.min(mWidth, mHeight);

        // 绘制圆环
        drawRing(canvas);
        // 绘制扫描
        drawScan(canvas);
        // 绘制中间icon
        drawCenterIcon(canvas);
    }

    /**
     * 绘制中间icon
     * @param canvas
     */
    private void drawCenterIcon(Canvas canvas) {
        //这里以最中间的圆为区域设置这个中间图片 中间需要去除padding值
        float scale=0.8f;
        float radius=(mWidth-getPaddingLeft()-getPaddingRight())*mRingScale*scale;
        Rect rect=new Rect((int)(mWidth/2-radius),(int)(mHeight/2-radius),(int)(mWidth/2+radius),(int)(mHeight/2+radius));
        canvas.drawBitmap(mCenterIcon,null,rect,mCicleIconPaint);

        mCicleIconPaint.setColor(Color.WHITE);
        mCicleIconPaint.setStyle(Paint.Style.STROKE);
        mCicleIconPaint.setStrokeWidth(2);
        canvas.drawCircle(mWidth/2,mHeight/2,radius,mCicleIconPaint);
    }

    /**
     * 绘制圆环
     * @param canvas
     */
    private void drawRing(Canvas canvas) {

        /**
         * 这里根据设置的一共所有的圆环数量来进行遍历绘制  中间就是空间的中心， 绘制的半径就是中心向两边延伸，每次延伸设定的宽度（去除padding）与比例相乘
         */
        for (int i=0;i<mRingNum;i++){
            mRingPaint.setAlpha(getAlpha(mRingNum, i));
            canvas.drawCircle(mWidth / 2, mHeight / 2,
                    (mWidth-getPaddingLeft()-getPaddingRight()) * (1+i)*mRingScale, mRingPaint);     // 绘制小圆
        }
    }

    /**
     * 绘制扫描
     * @param canvas
     */
    private void drawScan(Canvas canvas) {
        /**
         *   设置扫描渲染的shader  这里需要了解下这个类
         *   SweepGradient  扫描/梯度渲染
         *   public SweepGradient(float cx, float cy, int[] colors, float[] positions)
         *   cx	渲染中心点 x 坐标
         *   cy	渲染中心 y 点坐标
         *   colors	围绕中心渲染的颜色数组，至少要有两种颜色值
         *   positions	相对位置的颜色数组,可为null,  若为null,可为null,颜色沿渐变线均匀分布
         */
        mScanShader = new SweepGradient(mWidth / 2, mHeight / 2, new int[]{Color.TRANSPARENT, mScanBgColor}, null);
        //保存画布当前的状态
        canvas.save();
        mScanPaint.setShader(mScanShader);
        //canvas.concat可以理解成对matrix的变换应用到canvas上的所有对象
        canvas.concat(matrix);
        //绘制圆
        canvas.drawCircle(mWidth / 2, mHeight / 2, (mWidth-getPaddingLeft()-getPaddingRight()) * (mRingNum-1) * mRingScale, mScanPaint);
        //取出之前保存过的状态 和save成对出现 为了不影响其他部分的绘制
        canvas.restore();
    }
    /**
     * 获取透明度  通过当前index占总共数量的count的比例来设置透明度
     * @param halfCount
     * @param index
     * @return
     */
    private int getAlpha(int halfCount, int index) {
        int MAX_ALPHA_VALUE = 255;
        int alpha= MAX_ALPHA_VALUE / halfCount * (halfCount - index);
        return index==0?0:alpha-25;
    }

    /**
     * 实现扫描
     */
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            matrix.postRotate(mScanAngle, getMeasuredWidth()/2,getMeasuredWidth()/2);
            invalidate();
            postDelayed(mRunnable, mScanSpeed);
            currentScanAngle = (currentScanAngle + mScanAngle) % 360;
            if (startScan && currentScanningCount <= (360 / mScanAngle)) {
                if (mOnScanningListener != null && currentScanningCount % mScanAngle == 0
                        && currentScanningItem < maxScanItemCount) {
                    mOnScanningListener.onScanning(currentScanningItem, currentScanAngle);
                    currentScanningItem++;
                } else if (mOnScanningListener != null && currentScanningItem == maxScanItemCount) {
                    mOnScanningListener.onScanSuccess();
                }
                currentScanningCount++;
            }
        }
    };

    /**
     * 开始扫描
     */
    public void startScan(){
        this.startScan=true;
    }

    /**
     * 实现接口回调
     * @param mOnScanningListener
     */
    public void setOnScanningListener(OnScanningListener mOnScanningListener){
        this.mOnScanningListener=mOnScanningListener;
    }

    public interface  OnScanningListener {
        /**
         * 正在扫描（此时还没有扫描完毕）时回调
         * @param position
         * @param scanAngle
         */
        void onScanning(int position, float scanAngle);

        /**
         * 扫描成功时回调
         */
        void onScanSuccess();
    }

    /**
     * 设置圆环数量
     * @param mRingNum
     */
    public void setMaxRingNum(int mRingNum) {
        this.mRingNum = mRingNum;
    }

    /**
     * 设置圆环颜色
     * @param mRingColor
     */
    public void setRingColor(int mRingColor) {
        this.mRingColor = mRingColor;
    }

    /**
     * 设置扫描颜色
     * @param mScanBgColor
     */
    public void setScanBgColor(int mScanBgColor) {
        this.mScanBgColor = mScanBgColor;
    }

    /**
     * 设置圆环宽度
     * @param mRingWidth
     */
    public void setRingWidth(float mRingWidth) {
        this.mRingWidth = mRingWidth;
    }

    /**
     * 设置圆环数量
     * @param mRingNum
     */
    public void setRingNum(int mRingNum) {
        this.mRingNum = mRingNum;
    }

    /**
     * 设置扫描速度  毫秒
     * @param mScanSpeed
     */
    public void setScanSpeed(int mScanSpeed) {
        this.mScanSpeed = mScanSpeed;
    }


    /**
     * 设置旋转角度 每次刷新旋转的角度
     * @param mScanAngle
     */
    public void setScanAngle(int mScanAngle) {
        this.mScanAngle = mScanAngle;
    }

    /**
     * 设置圆环的半径与空间的宽度的比例
     * @param mRingScale
     */
    public void setRingScale(float mRingScale) {
        this.mRingScale = mRingScale;
    }

    /**
     * 设置最大扫描item数量
     * @param maxScanItemCount
     */
    public void setMaxScanItemCount(int maxScanItemCount) {
        this.maxScanItemCount = maxScanItemCount;
    }

}
