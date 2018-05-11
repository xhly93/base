package com.ssj.user.Parent.View;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.ssj.user.Utils.DensityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 17258 on 2018/5/10.
 */

public class CirclePercentView extends View {

    private static final int START_ANGLE = -90;
    private static final int ROUND_WHDTH = 20;
    private int roundwidth = 20;//环形的宽度
    private Paint paint;//定义画笔
    private float starangle = -90;//开始角度
    private int viewWidth, viewHeight;
    private int mFirstColor, mSecondColor, mThirdColor;
    private List<Integer> mColors = new ArrayList<>();
    private List<Integer> mPercentDatas = new ArrayList<>();
    private RectF mRectF;


    public CirclePercentView(Context context) {


        this(context, null);

    }


    public CirclePercentView(Context context, AttributeSet attrs) {

        this(context, attrs, 0);
    }


    public CirclePercentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mFirstColor = Color.parseColor("#46C390");
        mSecondColor = Color.parseColor("#ECA50D");
        mThirdColor = Color.parseColor("#E45C58");
        mColors.add(mFirstColor);
        mColors.add(mSecondColor);
        mColors.add(mThirdColor);
        mColors.add(mSecondColor);
        roundwidth = DensityUtil.dip2px(getContext(), ROUND_WHDTH);
        starangle = START_ANGLE;
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);//设置画圆弧的画笔的属性为描边(空心)，个人喜欢叫它描边，叫空心有点会引起歧义
        paint.setAntiAlias(true);//取消锯齿
        paint.setStrokeWidth(roundwidth);
        paint.setStrokeCap(Paint.Cap.ROUND);
        mPercentDatas.add(40);
        mPercentDatas.add(40);
        mPercentDatas.add(140);
        mPercentDatas.add(140);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        viewHeight = MeasureSpec.getSize(heightMeasureSpec);
    }

    @Override

    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        starangle = START_ANGLE;
        if (mPercentDatas == null || mColors == null) {
            return;
        }
        mRectF = new RectF(roundwidth, roundwidth, viewWidth - roundwidth, viewHeight - roundwidth);

        int colorSize = mColors.size();
        int size = mPercentDatas.size();
        if (colorSize != size) {
            return;
        }
        for (int i = 0; i < size; ++i) {
            paint.setColor(mColors.get(i));
            canvas.drawArc(mRectF, starangle, mPercentDatas.get(i), false, paint);
            starangle += mPercentDatas.get(i);
        }

        starangle = mPercentDatas.get(0) + START_ANGLE - 5;
        for (int i = 0; i < size - 1; ++i) {
            paint.setColor(mColors.get(i));
            canvas.drawArc(mRectF, starangle, 1, false, paint);
            starangle = starangle + mPercentDatas.get(i + 1);
        }

    }


    private void setDatas(List<Integer> datas, List<Integer> colorDatas) {
        mPercentDatas = datas;
        mColors = colorDatas;
        invalidate();
    }

}
