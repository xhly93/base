package com.ssj.user.Parent.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.ssj.user.R;

public class RoundProgressBar extends View {
    private Paint mPaint;
    private int mReachedColor = Color.parseColor("#46c390");
    private int mReachedHeight;
    private int mUnReachedColor = Color.parseColor("#f4f4f4");
    private int mUnReachedHeight;
    private int mRadius;
    private int mMaxStrokeWidth;
    private int mTextHeight;
    private RectF mArcRectF;
    private Rect mTextRect = new Rect();

    private String mText;
    private int mProgress;

    public RoundProgressBar(Context context) {
        this(context, null);
    }

    public RoundProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.progressBarStyle);
    }

    public RoundProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initDefaultAttrs(context);
        mMaxStrokeWidth = Math.max(mReachedHeight, mUnReachedHeight);
    }

    private void initDefaultAttrs(Context context) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mReachedColor = Color.parseColor("#46c390");
        mReachedHeight = RoundProgressBar.dp2px(context, 16);
        mUnReachedColor = Color.parseColor("#f4f4f4");
        mUnReachedHeight = RoundProgressBar.dp2px(context, 16);
        mRadius = RoundProgressBar.dp2px(context, 54);
    }


    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expectSize = mRadius * 2 + mMaxStrokeWidth + getPaddingLeft() + getPaddingRight();
        int width = resolveSize(expectSize, widthMeasureSpec);
        int height = resolveSize(expectSize, heightMeasureSpec);
        expectSize = Math.min(width, height);

        mRadius = (expectSize - getPaddingLeft() - getPaddingRight() - mMaxStrokeWidth) / 2;
        if (mArcRectF == null) {
            mArcRectF = new RectF();
        }
        mArcRectF.set(0, 0, mRadius * 2, mRadius * 2);

        setMeasuredDimension(expectSize, expectSize);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        onDrawCircle(canvas);
    }

    public  void setProgress(int progress) {
        mProgress = progress;
        invalidate();
    }

    public  int getProgress() {
        return mProgress;
    }



    private void onDrawCircle(Canvas canvas) {
        canvas.translate(getPaddingLeft() + mMaxStrokeWidth / 2, getPaddingTop() + mMaxStrokeWidth / 2);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mUnReachedColor);
        mPaint.setStrokeWidth(mUnReachedHeight);
        canvas.drawCircle(mRadius, mRadius, mRadius, mPaint);

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mReachedColor);
        mPaint.setStrokeWidth(mReachedHeight);
        canvas.drawArc(mArcRectF, 0, getProgress(), false, mPaint);

        calculateTextWidthAndHeight();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setColor(getContext().getColor(R.color.color_46c390));
        mPaint.setTextSize(sp2px(getContext(), 18));
        canvas.drawText(getProgress() + "%", mRadius, mRadius, mPaint);
        mPaint.setColor(Color.parseColor("#7E7E7E"));
        mPaint.setTextSize(sp2px(getContext(), 12));
        canvas.drawText(getContext().getString(R.string.target_completion), mRadius, mRadius + mTextHeight / 2 + dp2px(getContext(), 5), mPaint);
    }

    private void calculateTextWidthAndHeight() {
        //fix by Michael 修改参数溢出问题。
        //mText = String.format("%d", getProgress() * 100 / getMax()) + "%";
        mText = String.format("%d", (int) (/*getProgress()*/getProgress() * 1.0f / 360 * 100)) + "%";
        mPaint.setTextSize(sp2px(getContext(), 18));
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.getTextBounds(mText, 0, mText.length(), mTextRect);
        mTextHeight = mTextRect.height();
    }

    public static int dp2px(Context context, float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics());
    }

    public static int sp2px(Context context, float spValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, context.getResources().getDisplayMetrics());
    }
}
