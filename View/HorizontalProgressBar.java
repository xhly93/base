package com.ssj.user.Parent.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.ssj.user.R;

public class HorizontalProgressBar extends View {
    private Paint mPaint;
    private int mProgressColor = Color.parseColor("#ECA50D");
    private int mBackGroundColor = Color.parseColor("#F2F2F2");
    private int mProgressHeight;
    private int mProgress;
    private int viewWidth;
    private int mBackRadius;
    private int mHeadRadius;

    public HorizontalProgressBar(Context context) {
        this(context, null);
    }

    public HorizontalProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initDefaultAttrs(context);
    }

    private void initDefaultAttrs(Context context) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mProgressHeight = HorizontalProgressBar.dp2px(context, 8);
        mBackRadius = HorizontalProgressBar.dp2px(context, 4);
        mHeadRadius = HorizontalProgressBar.dp2px(context, 3);
    }


    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewWidth = MeasureSpec.getSize(widthMeasureSpec);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        onDrawCircle(canvas);
    }

    public void setProgress(int progress) {
        mProgress = progress;
        invalidate();
    }

    public void setProgressColor(int color) {
        this.mProgressColor = color;
        invalidate();
    }

    public int getProgress() {
        return mProgress;
    }


    private int progressX;

    private void onDrawCircle(Canvas canvas) {
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mBackGroundColor);
        canvas.drawRoundRect(0, 0, viewWidth, mProgressHeight, mBackRadius,mBackRadius,mPaint);
        progressX = mProgress * viewWidth / 100;
        mPaint.setColor(mProgressColor);
        canvas.drawRoundRect(0, 0, progressX, mProgressHeight, mHeadRadius,mHeadRadius,mPaint);
        mPaint.setColor(Color.WHITE);
        canvas.drawCircle(progressX - mBackRadius, mBackRadius, mHeadRadius, mPaint);
    }


    public static int dp2px(Context context, float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics());
    }


}
