package com.ssj.user.Parent.View;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.ssj.user.R;
import com.ssj.user.Utils.DensityUtil;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by 17258 on 2018/5/10.
 */

public class LineChartView extends View {



    private Context mContext;
    private Paint mPaint;
    private Paint mJianBianPaint;
    private Resources res;
    private DisplayMetrics dm;
    private int lineColor = Color.parseColor("#46C390");
    private int bigCircleColor = Color.parseColor("#46C390");
    private int straightLineClolor = Color.parseColor("#4D46C390");
    private int smallCircleColor = Color.WHITE;
    private int lineWidth = 2;
    private int lineChartHeight;//view height
    private int xTextHeight;//text height
    private int xTextMarginTopHeight;//text height
    private Path jianBianPath = new Path();


    private int canvasHeight;
    private int canvasWidth;
    private int blwidh;//margin left
    private boolean isMeasure = true;
    /**
     * Y轴最大值
     */
    private double maxValue;
    /**
     * Y轴间距值
     */
    private int marginTop = 20;
    private int marginBottom = 40;

    /**
     * 曲线上总点数
     */
    private Point[] mPoints;
    /**
     * 纵坐标值
     */
    private ArrayList<Double> yRawData;
    /**
     * 横坐标值
     */
    private ArrayList<String> xRawDatas;
    private ArrayList<Integer> xList = new ArrayList<Integer>();// 记录每个x的值

    public LineChartView(Context context) {
        this(context, null);
    }

    public LineChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView();
    }

    private void initView() {
        Log.i("LineChartView", "initView: ");
        this.res = mContext.getResources();
        this.mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.mJianBianPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        Log.i("LineChartView", "onSizeChanged: ");
        if (isMeasure) {
            marginBottom = dip2px(10);
            xTextHeight = dip2px(15);
            xTextMarginTopHeight = dip2px(5);
            this.canvasHeight = getHeight();
            this.canvasWidth = getWidth();
            lineChartHeight = canvasHeight - xTextHeight - xTextMarginTopHeight;
            blwidh = dip2px(30);
            marginTop = dip2px(marginTop);
            isMeasure = false;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 点的操作设置
        mPoints = getPoints();

        mPaint.setColor(lineColor);
        mPaint.setStrokeWidth(dip2px(lineWidth));
        mPaint.setStyle(Paint.Style.STROKE);
        drawScrollLine(canvas);

        drawAllXLine(canvas);

        mPaint.setColor(bigCircleColor);
        for (int i = 0; i < mPoints.length; i++) {
            canvas.drawCircle(mPoints[i].x, mPoints[i].y, dip2px(4), mPaint);

        }
        mPaint.setColor(smallCircleColor);
        for (int i = 0; i < mPoints.length; i++) {
            canvas.drawCircle(mPoints[i].x, mPoints[i].y, dip2px(lineWidth), mPaint);
        }

        mPaint.setColor(straightLineClolor);
        mPaint.setStrokeWidth(dip2px(0.5f));
        for (int i = 0; i < mPoints.length; i++) {
            canvas.drawLine(mPoints[i].x, mPoints[i].y + dip2px(4), mPoints[i].x, lineChartHeight, mPaint);
        }

    }


    /**
     * 画X轴
     */
    private void drawAllXLine(Canvas canvas) {
        mPaint.setColor(Color.parseColor("#7E7E7E"));
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextSize(DensityUtil.sp2px(getContext(), 10));
        Rect rect = new Rect();
        mPaint.getTextBounds("04.01", 0, "04.01".length(), rect);
        int xBottomTextWidth = rect.width();
        for (int i = 0; i < yRawData.size(); i++) {
            canvas.drawText(("04.01"), blwidh + (canvasWidth - blwidh) / yRawData.size() * i - xBottomTextWidth / 2,
                    lineChartHeight + xTextMarginTopHeight + xTextHeight, mPaint);
        }

        int topTextWidth;
        String text = "";
        String textEnd = getContext().getString(R.string.fraction);
        mPaint.setColor(lineColor);
        for (int i = 0; i < yRawData.size(); i++) {
            rect = new Rect();
            text = yRawData.get(i)+textEnd;
            mPaint.getTextBounds(text, 0, text.length(), rect);
            topTextWidth = rect.width();
            canvas.drawText(text, blwidh + (canvasWidth - blwidh) / yRawData.size() * i - topTextWidth / 2,
                    mPoints[i].y-dip2px(5)-dip2px(lineWidth), mPaint);
        }
    }

    private void drawScrollLine(Canvas canvas) {
        Shader mShader = new LinearGradient(0,0,0,lineChartHeight,new int[] {lineColor,straightLineClolor,Color.TRANSPARENT},null,Shader.TileMode.REPEAT);
        mJianBianPaint.setShader(mShader);
        Point startp = new Point();
        Point endp = new Point();
        jianBianPath = new Path();
        jianBianPath.moveTo(mPoints[0].x, lineChartHeight);
        jianBianPath.lineTo(mPoints[0].x, mPoints[0].y);
        for (int i = 0; i < mPoints.length - 1; i++) {
            startp = mPoints[i];
            endp = mPoints[i + 1];
            int wt = (startp.x + endp.x) / 2;
            Point p3 = new Point();
            Point p4 = new Point();
            p3.y = startp.y;
            p3.x = wt;
            p4.y = endp.y;
            p4.x = wt;

            Path path = new Path();
            path.moveTo(startp.x,startp.y);
            path.cubicTo(p3.x, p3.y, p4.x, p4.y, endp.x, endp.y);
            canvas.drawPath(path, mPaint);

            jianBianPath.cubicTo(p3.x, p3.y, p4.x, p4.y, endp.x, endp.y);
        }
        jianBianPath.lineTo(mPoints[6].x,lineChartHeight);
        canvas.drawPath(jianBianPath, mJianBianPaint);
    }


    private Point[] getPoints() {

        for (int i = 0; i < yRawData.size(); i++) {
            xList.add(blwidh + (canvasWidth - blwidh) / yRawData.size() * i);
        }

        Point[] points = new Point[yRawData.size()];
        for (int i = 0; i < yRawData.size(); i++) {
            int ph = lineChartHeight - (int) (lineChartHeight * (yRawData.get(i) / maxValue));
            points[i] = new Point(xList.get(i), ph + marginTop);
        }
        return points;
    }

    public void setData(ArrayList<Double> yRawData, ArrayList<String> xRawData) {
        this.maxValue = Collections.max(yRawData);
        this.mPoints = new Point[yRawData.size()];
        this.xRawDatas = xRawData;
        this.yRawData = yRawData;
        invalidate();
    }



    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    private int dip2px(float dpValue) {
        return (int) (dpValue * dm.density + 0.5f);
    }

}
