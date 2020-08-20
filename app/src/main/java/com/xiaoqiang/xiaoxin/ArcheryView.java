package com.xiaoqiang.xiaoxin;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.Nullable;

import java.util.Random;

/**
 * @author Android-小强
 * @email: 15075818555@163.com
 * @data: on 2020/8/19 10:30
 */
public class ArcheryView extends View implements IArcheryView {
    private static final String TAG = "12345";
    RectF dstTop = new RectF();
    RectF dstBottom = new RectF();
    /**
     * 弓 的画笔
     */
    private Paint mPaintBow;
    /**
     * 弓的 线条宽度
     */
    private float mBowStrokeWidth;
    /**
     * 弓 的颜色
     */
    private int mBowColor;
    /**
     * 弓的线条
     */
    private Path mBowPath, mPathArrow, mPathArrowTop;
    /**
     * 初始化 的 x y
     */
    private float startX, endX, topY, bottomY, controlPointY;
    /**
     * 弓的 起始点和结束点
     */
    private float mBowStartX, mBowStartY, mBowEndX, mBowEndY;
    /**
     * 弓的控制点
     */
    private float mBowControlPointX, mBowControlPointY;
    /**
     * 弓 和 线 两端的圆点 装饰 的画笔
     */
    private Paint mPaintBowDots;
    /**
     * 弓和 线 两端的圆点 线条宽度
     */
    private float mBowDotsStrokeWidth;
    /**
     * 弓和 线 两端的圆点 的颜色
     */
    private int mBowDotsColor;
    /**
     * 拉线 的 接触点
     */
    private float mBowDotsControlPointX, mBowDotsControlPointY, mBowDotsControlPointYDefault;
    /**
     * 箭杆 的画笔
     */
    private Paint mPaintArrow;
    /**
     * 箭杆 线条宽度
     */
    private float mArrowStrokeWidth;
    /**
     * 箭杆 的颜色
     */
    private int mArrowColor;
    /**
     * 箭杆 的 接触点
     */
    private float mArrowControlPointX, mArrowControlPointY, mArrowControlPointXDefault, mArrowControlPointYDefault;
    /**
     * 箭杆 从接触点到顶端的长度
     */
    private int mArrowLength;
    /**
     * 弓的初始宽度 聚理底部边距
     */
    private float mBowDefaultWidth, mBowDefaultBottomPadding;
    private float downX, downY;
    private ValueAnimator valueAnimator;
    /**
     * 默认 拉弓 最大距离dpTopx(40)
     */
    private float defaultDistance;
    /**
     * 画布旋转的角度
     */
    private float angle;
    private OnArcheryListener mOnArcheryListener;
    private Bitmap bitmap;
    private Fireworks mFireworks = new Fireworks();
    /**
     * 箭头
     */
    private Paint mPaintArrowTop;

    public ArcheryView(Context context) {
        this(context, null);
    }

    public ArcheryView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArcheryView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     * 初始化
     *
     * @param context
     * @param attrs
     */
    private void init(Context context, AttributeSet attrs) {

        mBowDefaultWidth = dpTopx(120);
        mBowDefaultBottomPadding = dpTopx(180);

        mBowStrokeWidth = dpTopx(2);
        mBowColor = Color.parseColor("#ffffff");


        mArrowStrokeWidth = dpTopx(1);
        mArrowColor = Color.parseColor("#ffffff");
        mArrowLength = (int) dpTopx(115);

        mBowDotsStrokeWidth = dpTopx(8);
        mBowDotsColor = Color.parseColor("#99ffffff");

        defaultDistance = dpTopx(35);

        mBowPath = new Path();
        mPathArrow = new Path();
        mPathArrowTop = new Path();


        mPaintBow = new Paint();
        mPaintBow.setAntiAlias(true);
        mPaintBow.setStrokeWidth(mBowStrokeWidth);
        mPaintBow.setStyle(Paint.Style.STROKE);
        mPaintBow.setColor(mBowColor);
        mPaintBow.setStrokeCap(Paint.Cap.ROUND);


        mPaintArrow = new Paint();
        mPaintArrow.setAntiAlias(true);
        mPaintArrow.setStrokeWidth(mArrowStrokeWidth);
        mPaintArrow.setStyle(Paint.Style.STROKE);
        mPaintArrow.setColor(mArrowColor);

        mPaintArrowTop = new Paint();
        mPaintArrowTop.setAntiAlias(true);
        mPaintArrowTop.setStrokeWidth(mArrowStrokeWidth);
        mPaintArrowTop.setStyle(Paint.Style.FILL);
        mPaintArrowTop.setColor(mArrowColor);


        mPaintBowDots = new Paint();
        mPaintBowDots.setAntiAlias(true);
        mPaintBowDots.setStrokeWidth(mBowDotsStrokeWidth);
        mPaintBowDots.setStyle(Paint.Style.FILL);
        mPaintBowDots.setColor(mBowDotsColor);
        mPaintBowDots.setStrokeCap(Paint.Cap.ROUND);


    }

    /**
     * dp px 转换
     *
     * @param size
     * @return
     */
    private float dpTopx(int size) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, getResources().getDisplayMetrics());
    }

    public ArcheryView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        postInvalidate();
    }

    public void setOnArcheryListener(OnArcheryListener mOnArcheryListener) {
        this.mOnArcheryListener = mOnArcheryListener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {


        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (valueAnimator != null && valueAnimator.isRunning()) {
                return false;
            }
            if (event.getY() < mBowControlPointY) {
                return false;
            }
            downX = event.getX();
            downY = event.getY();
            return true;
        }
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            float y = event.getY();
            float x = event.getX();
            if (y < downY) {
                resetSize(true);
                invalidate();
                return false;
            }
            float v = y - downY;
            setBowY(v);
            setAngle(event.getX(), event.getY());
            invalidate();

        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            float y = event.getY();
            if (y <= downY) {
                resetSize(true);
                invalidate();
                return false;
            }
            resetSize(false);
            archery((y - downY) * 0.5F);
        }
        if (event.getAction() == MotionEvent.ACTION_CANCEL) {
            resetSize(true);
            invalidate();
        }


        return super.onTouchEvent(event);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        float left = (w - dpTopx(100)) * 0.5F;
        float top = dpTopx(50);
        dstTop.set(left, top, left + dpTopx(100), top + dpTopx(50));
        dstBottom.set(left, dstTop.bottom, left + dpTopx(100), dstTop.bottom + dpTopx(50));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        if (bitmap != null) {
            Rect rectF = new Rect(0, bitmap.getHeight() / 2, bitmap.getWidth(), bitmap.getHeight());
            canvas.drawBitmap(bitmap, rectF, dstBottom, null);
        }
        canvas.save();
        canvas.rotate(angle, getWidth() * 0.5F, mBowControlPointY);
        drawBow(canvas);
        canvas.restore();
        if (bitmap != null) {
            Rect rectF = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight() / 2);
            canvas.drawBitmap(bitmap, rectF, dstTop, null);
        }
    }
    public void reversionBitmap(){
    }

    /**
     * 画弓
     *
     * @param canvas
     */
    private void drawBow(Canvas canvas) {

        if (mBowStartX == 0) {
            resetSize(true);
        }
        //弓
        mBowPath.reset();
        mBowPath.moveTo(mBowStartX, mBowStartY);
        mBowPath.quadTo(mBowControlPointX, mBowControlPointY, mBowEndX, mBowEndY);
        canvas.drawPath(mBowPath, mPaintBow);

        //两端装饰
        RectF rectf = new RectF(mBowStartX - dpTopx(10), mBowStartY - dpTopx(10), mBowStartX + dpTopx(10), mBowStartY);
        canvas.drawArc(rectf, 90, 90, false, mPaintBow);
        rectf.set(mBowEndX - dpTopx(10), mBowEndY - dpTopx(10), mBowEndX + dpTopx(10), mBowEndY);
        canvas.drawArc(rectf, 0, 90, false, mPaintBow);
//        canvas.drawLine(mBowStartX, mBowStartY, mBowStartX - dpTopx(10), mBowStartY, mPaintBow);
//        canvas.drawLine(mBowEndX, mBowEndY, mBowEndX + dpTopx(10), mBowEndY, mPaintBow);

        //圆点
        mBowDotsStrokeWidth = dpTopx(8);
        mPaintBowDots.setStrokeWidth(mBowDotsStrokeWidth);
//        canvas.drawPoint(mBowStartX, mBowStartY, mPaintBowDots);
//        canvas.drawPoint(mBowEndX, mBowEndY, mPaintBowDots);
        canvas.drawPoint(mBowDotsControlPointX, mBowDotsControlPointY, mPaintBowDots);


        //拉线
        mBowDotsStrokeWidth = dpTopx(1);
        mPaintBowDots.setStrokeWidth(mBowDotsStrokeWidth);
        canvas.drawLine(mBowStartX, mBowStartY, mBowDotsControlPointX, mBowDotsControlPointY, mPaintBowDots);
        canvas.drawLine(mBowDotsControlPointX, mBowDotsControlPointY, mBowEndX, mBowEndY, mPaintBowDots);

        //箭头
        mPathArrow.reset();
        mPathArrow.moveTo(mArrowControlPointX, mArrowControlPointY);
        float tip = dpTopx(12);
        mPathArrow.rLineTo(0, tip);
        mPathArrow.rLineTo(dpTopx(5), 0);
        mPathArrow.rLineTo(-dpTopx(10), 0);
        mPathArrow.rLineTo(dpTopx(10), -dpTopx(3));
        mPathArrow.rLineTo(-dpTopx(10), 0);
        mPathArrow.rLineTo(dpTopx(10), -dpTopx(3));
        mPathArrow.rLineTo(-dpTopx(10), 0);
        mPathArrow.rLineTo(dpTopx(10), -dpTopx(3));
        mPathArrow.rLineTo(-dpTopx(10), 0);
        mPathArrow.rLineTo(dpTopx(10), -dpTopx(3));
        mPathArrow.rLineTo(-dpTopx(5), 0);
        mPathArrow.rLineTo(0, dpTopx(12));


        mPathArrow.rLineTo(0, -mArrowLength - tip);
        canvas.drawPath(mPathArrow, mPaintArrow);
        mPathArrowTop.reset();
        mPathArrowTop.moveTo(mArrowControlPointX, mArrowControlPointY - mArrowLength);
        mPathArrowTop.rLineTo(-dpTopx(3), dpTopx(10));
        mPathArrowTop.rLineTo(dpTopx(3), dpTopx(6));
        mPathArrowTop.rLineTo(dpTopx(3), -dpTopx(6));
        mPathArrowTop.rLineTo(-dpTopx(3), -dpTopx(10));
        canvas.drawPath(mPathArrowTop, mPaintArrowTop);


        if (mArrowControlPointX == mArrowControlPointXDefault && mArrowControlPointY == mArrowControlPointYDefault) {
            mFireworks.setXy(canvas, -1, -1);
        } else {
            mFireworks.setXy(canvas, mArrowControlPointX, mArrowControlPointY);
        }

    }

    @Override
    protected void onDetachedFromWindow() {
        if (valueAnimator != null) {
            valueAnimator.cancel();
            valueAnimator = null;
        }
        super.onDetachedFromWindow();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 复位 弓箭
     */
    private void resetSize(boolean arrow) {

        //弓
        mBowStartX = (getWidth() - mBowDefaultWidth) / 2;
        mBowStartY = getHeight() - mBowDefaultBottomPadding;

        mBowControlPointX = getWidth() / 2;
        mBowControlPointY = getHeight() - mBowDefaultBottomPadding - dpTopx(40);

        mBowEndX = mBowStartX + mBowDefaultWidth;
        mBowEndY = mBowStartY;

        //拉线
        mBowDotsControlPointX = mBowControlPointX;
        mBowDotsControlPointY = mBowStartY;
        mBowDotsControlPointYDefault = mBowStartY;


        //箭头 接触点
        mArrowControlPointXDefault = mBowDotsControlPointX;
        mArrowControlPointYDefault = mBowDotsControlPointY + dpTopx(10);

        if (arrow) {
            angle = 0;
            mArrowControlPointX = mArrowControlPointXDefault;
            mArrowControlPointY = mArrowControlPointYDefault;
            if (mOnArcheryListener != null) {
                mOnArcheryListener.onReset();
            }
        }

        startX = mBowStartX;
        endX = mBowEndX;
        topY = mBowStartY;
        bottomY = topY + defaultDistance;
        controlPointY = mBowControlPointY;


    }

    /**
     * 设置拉弓
     *
     * @param v
     */
    private void setBowY(float v) {
        if (v <= 0) {
            resetSize(false);
            return;
        }
        float vY = topY + v * 0.5F;
        float cY = controlPointY - v * 0.5F;
        float dcY = mBowDotsControlPointYDefault + v;

        float awY = mArrowControlPointYDefault + v;

        if (vY > bottomY) {
            float v1 = bottomY - topY;
            mBowStartY = bottomY;
            mBowControlPointY = controlPointY - v1;
            mBowDotsControlPointY = mBowDotsControlPointYDefault + v1 * 2;
            mArrowControlPointY = mArrowControlPointYDefault + v1 * 2;
        } else {
            mBowStartY = vY;
            mBowControlPointY = cY;
            mBowDotsControlPointY = dcY;
            mBowStartX = startX + v * 0.25F;
            mBowEndX = endX - v * 0.25F;
            mArrowControlPointY = awY;
        }
        mBowEndY = mBowStartY;


    }

    /**
     * 设置角度
     *
     * @param eventX
     * @param eventY
     */
    private void setAngle(float eventX, float eventY) {

//        float centerX = getWidth() * 0.5F;
//        if (eventX < centerX) {
//            double atan2 = Math.atan2(eventY - mBowControlPointY, centerX - eventX);
//            angle = (float) ((180 * atan2 / Math.PI) * 0.5F);
//        } else if (eventX > centerX) {
//            double atan2 = Math.atan2(eventY - mBowControlPointY, eventX - centerX);
//            angle = -(float) ((180 * atan2 / Math.PI) * 0.5F);
//        } else {
//            angle = 0;
//        }


    }

    /**
     * 进行射箭
     *
     * @param distance
     */
    private void archery(float distance) {
        distance = Math.min(defaultDistance, distance);
        float v2 = mArrowControlPointYDefault - dpTopx(100);
        float v = v2 / defaultDistance;
        float end = mArrowControlPointYDefault - distance * v;

        Log.d(TAG, "distance: " + distance);
        Log.d(TAG, "defaultDistance: " + defaultDistance);
        Log.d(TAG, "archeryv2: " + v2);
        Log.d(TAG, "v: " + v);
        Log.d(TAG, "mArrowControlPointYDefault: " + mArrowControlPointYDefault);
        Log.d(TAG, "distance * v: " + distance * v);
        Log.d(TAG, "end: " + end);
        float v1 = mArrowControlPointYDefault / 600;


        if (valueAnimator == null) {
            valueAnimator = ValueAnimator.ofFloat(mArrowControlPointYDefault, end);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float animatedValue = (float) animation.getAnimatedValue();
                    mArrowControlPointY = animatedValue;
                    postInvalidate();
                }
            });
            valueAnimator.setInterpolator(new DecelerateInterpolator());
            valueAnimator.addListener(new MyAnimatorListenerAdapter());
        } else {
            valueAnimator.setFloatValues(mArrowControlPointYDefault, end);
        }
        valueAnimator.setDuration(600);
        valueAnimator.start();
    }

    public void setAngle(float angle) {
        this.angle = angle;
        postInvalidate();
    }

    @Override
    public void addTarget(Target target) {

    }

    @Override
    public void addOnArcheryCompleteListener(OnArcheryCompleteListener onArcheryCompleteListener) {

    }

    public interface OnArcheryListener {
        /**
         * 复位
         */
        void onReset();

        /**
         * 射箭完成
         *
         * @param hit 是否命中目标
         */
        void onArcheryComplete(boolean hit);
    }

    private class MyAnimatorListenerAdapter extends AnimatorListenerAdapter {
        private boolean start;

        @Override
        public void onAnimationCancel(Animator animation) {
            start = false;
            super.onAnimationCancel(animation);
            resetSize(true);
            postInvalidate();
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            if (bitmap != null && start) {
                float v3 = mArrowControlPointY - mArrowLength;
                Log.d(TAG, "onAnimationEndv3: " + v3);
                Log.d(TAG, "mArrowLength: " + mArrowLength);
                Log.d(TAG, "mArrowControlPointY: " + mArrowControlPointY);
                Log.d(TAG, "dstTop: " + dstTop);
                boolean contains = dstTop.contains(mArrowControlPointX, v3);
                boolean b = v3 < dstTop.top;
                if (mOnArcheryListener != null) {
                    mOnArcheryListener.onArcheryComplete(contains || b);
                }
            }
            resetSize(true);
            postInvalidate();
            start = false;
        }

        @Override
        public void onAnimationStart(Animator animation) {
            super.onAnimationStart(animation);
            start = true;
        }
    }

    private class Fireworks {
        int i1Width = (int) dpTopx(30);
        int i1Height = (int) dpTopx(30);
        private Paint mPaint;
        private Random random;

        public Fireworks() {
            mPaint = new Paint();
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setAntiAlias(true);
            mPaint.setStrokeWidth(1);
            mPaint.setColor(Color.WHITE);

            random = new Random();
        }

        /**
         * 设置位置
         *
         * @param canvas
         * @param x
         * @param y
         */
        public void setXy(Canvas canvas, float x, float y) {

            if (valueAnimator == null || !valueAnimator.isRunning()) {
                return;
            }
            if (x < 0 || y < 0) {
                return;
            }

            for (int i = 0; i < 3; i++) {

                float x1 = x + (random.nextInt(i1Width) - i1Width / 2);
                float y1 = y + dpTopx(50) + random.nextInt(i1Height);

                Log.d(TAG, "setXy0: " + x);
                Log.d(TAG, "setXy1: " + y);
                Log.d(TAG, "setXy2: " + x1);
                Log.d(TAG, "setXy3: " + y1);

                canvas.drawLine(x1, y + dpTopx(12), x1, y1, mPaint);
            }

        }

        //分解Path为虚线
        //注意count要大于0
        private void splitPath(float startX, float startY, float endX, float endY, Path path, int count) {
            float deltaX = (endX - startX) / count;
            float deltaY = (endY - startY) / count;
            for (int i = 0; i < count; i++) {
                if (i % 3 == 0) {
                    path.moveTo(startX, startY);
                    path.lineTo(startX + deltaX, startY + deltaY);
                }
                startX += deltaX;
                startY += deltaY;
            }
        }
    }
}
