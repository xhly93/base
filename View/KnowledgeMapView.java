package com.ssj.user.Parent.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.ssj.user.Parent.Data.PAtlasData;
import com.ssj.user.R;
import com.ssj.user.Utils.DensityUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 17258 on 2018/5/10.
 */

public class KnowledgeMapView extends View {

    private static final String TAG = "KnowledgeMapView";
    private Paint mPaint, mPaintInside, mPaintPoint, mTextPaint, mNumberPaint;//定义画笔
    private int viewWidth, viewHeight;
    private int mFirstLineVerHeight, mOtherLineVerHeight, mLineHorHeight;//垂直线条高度，横向线条高度 mLineVerHeight = mRectHeight+mFirstRectGap
    private int mRectWidth, mRectHeight;//方块高度、宽度
    private int mFirstRectGap;//第一列、其他列方块之间垂直距离
    private int mMarginTop;
    private Point mStartPoint;//左上角线条第一个点
    private Map<Integer, List<PAtlasData>> mMap = new HashMap<>();
    private Map<Integer, List<RectF>> mRectMap = new HashMap<>();
    // private Map<Integer, List<PAtlasData>> mPatlasMap = new HashMap<>();
    private List<PAtlasData> mDataList = new ArrayList<>();
    private boolean isMeasure = true;
    private int mRadius, mPointRadius;
    private int mInsideRadius;
    private float mScrollX, mScrollY;
    private Canvas mCanvas;
    private int mFirstX, mFirstY;
    private List<DrawClickItem> mDrawItemList = new ArrayList<>();
    private Gson mGson = new Gson();
    private int mStrokeWidth;

    static class DrawClickItem {
        public int row;
        public int column;

        public DrawClickItem(int row, int column) {
            this.row = row;
            this.column = column;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        //Log.i("LineChartView", "onSizeChanged: ");
        if (isMeasure) {
            this.viewHeight = getHeight();
            this.viewWidth = getWidth();
            mFirstLineVerHeight = DensityUtil.dip2px(getContext(), 132);
            mOtherLineVerHeight = mFirstLineVerHeight / 2;
            mLineHorHeight = DensityUtil.dip2px(getContext(), 23);
            mRectWidth = DensityUtil.dip2px(getContext(), 138);
            mRectHeight = DensityUtil.dip2px(getContext(), 66);
            mFirstRectGap = DensityUtil.dip2px(getContext(), 67);
            mMarginTop = DensityUtil.dip2px(getContext(), 15);
            mRadius = DensityUtil.dip2px(getContext(), 8);
            mPointRadius = DensityUtil.dip2px(getContext(), 2);
            mInsideRadius = DensityUtil.dip2px(getContext(), 5);
            mStrokeWidth = DensityUtil.dip2px(getContext(), 2);
            isMeasure = false;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //Log.i(TAG, "onDraw: ");
        mCanvas = canvas;
        if (null == mMap || mMap.size() <= 0) {
            return;
        }
        drawFirstRow(mCanvas, R.color.color_727a8f, R.color.color_66727a8f);
        for (int i = 0; i < mDrawItemList.size(); i++) {
            drawOtherRect(mDrawItemList.get(i).row - 1, mDrawItemList.get(i).column, R.color.color_727a8f, R.color.color_66727a8f);
        }

    }

    public void setData(List<PAtlasData> list) {
        this.mDataList = list;
        mScrollX = 0;
        mScrollY = 0;
        setData(testData());
        invalidate();
        //drawFirstRow(mCanvas, R.color.color_727a8f, R.color.color_80727a8f);
    }

    private void drawFirstRow(Canvas canvas, int outcolor, int insidecolor) {
        if (null == mDataList || mDataList.size() == 0) {
            return;
        }
        mStartPoint = getFirstLinePoint(mDataList.size());
        mFirstX = (int) (mStartPoint.x + mScrollX);
        mFirstY = (int) (mStartPoint.y + mScrollY);
        //first row
        drawLine(mDataList.size(), mFirstX, mFirstY, canvas);
        drawRect(0, mDataList, mFirstX + mLineHorHeight, mFirstY - mRectHeight / 2, canvas, outcolor, insidecolor);
    }


    private void drawOtherRect(int row, int column, int outcolor, int insidecolor) {
        //Log.i(TAG, "drawOtherRect: " + row + " column:" + column);
        //sencond row
        int connectLineStartX = mFirstX + (row + 1) * (mLineHorHeight + mRectWidth) + mLineHorHeight * row;
        //Log.i(TAG, "drawOtherRect: connectLineStartX = " + connectLineStartX);
        RectF clickRectF = mRectMap.get(row).get(column);
        PAtlasData pAtlasData = mMap.get(row).get(column);
        mPaint.setColor(getResources().getColor(R.color.color_56d8a3));
        mPaintInside.setColor(getResources().getColor(R.color.color_6656d8a3));
        mCanvas.drawRoundRect(clickRectF, mRadius, mRadius, mPaint);
        mCanvas.drawRoundRect(new RectF(clickRectF.left + 15, clickRectF.top + 15, clickRectF.right - 15, clickRectF.bottom - 15), mInsideRadius, mInsideRadius, mPaintInside);
        if (getAtlas(pAtlasData).size() <= 0) {
            return;
        }
        int connectLineStartY = (int) (clickRectF.centerY()/* + mRectHeight / 2 */);
        //连线
        if (null == mCanvas) {
            //Log.i(TAG, "drawOtherRect:null==mCanvas ");
        }
        mPaint.setColor(getResources().getColor(outcolor));
        mCanvas.drawLine(connectLineStartX + mStrokeWidth, connectLineStartY, connectLineStartX + mLineHorHeight, connectLineStartY, mPaint);
        if (getAtlas(pAtlasData).size() != 1) {
            mCanvas.drawCircle(connectLineStartX + mLineHorHeight, connectLineStartY, mPointRadius, mPaintPoint);
        }
        if (mMap.size() < (row + 1)) {
            return;
        }
        List<PAtlasData> nextList = mMap.get(row + 1);
        if (null == nextList || nextList.size() == 0) {
            return;
        }
        int size = nextList.size();
        int startX = connectLineStartX + mLineHorHeight;
        int totalHeight = size * mRectHeight + (size - 1) * mFirstRectGap;
        int startY = connectLineStartY - totalHeight / 2 + mRectHeight / 2;
        drawLine(size, startX, startY, mCanvas);
        drawRect(row + 1, getAtlas(pAtlasData), startX + mLineHorHeight, startY - mRectHeight / 2, mCanvas, outcolor, insidecolor);
    }

    private void drawRect(int row, List<PAtlasData> list, int startX, int startY, Canvas canvas, int outsideColor, int insideColor) {
        mPaint.setColor(getResources().getColor(outsideColor));
        mPaintInside.setColor(getResources().getColor(insideColor));
        mPaint.setStrokeWidth(DensityUtil.dip2px(getContext(), 2));
        int mStartY;
        //List<RectF> rectFList = mRectMap.get(row);
        String str = null;
        String number = null;
        RectF rectF, inSideRectF;
        for (int i = 0; i < list.size(); i++) {
            str = list.get(i).getName();
            number = list.get(i).getMasterNum() + "/" + list.get(i).getCountNum();
            mStartY = startY + (mRectHeight + mFirstRectGap) * i;
            rectF = new RectF(startX, mStartY, startX + mRectWidth, mStartY + mRectHeight);
            //rectFList.set(i, rectF);
            mRectMap.get(row).set(i, rectF);
            //mPatlasMap.get(row).set(i, list.get(i));
            canvas.drawRoundRect(rectF, mRadius, mRadius, mPaint);
            inSideRectF = new RectF(rectF.left + 15, rectF.top + 15, rectF.right - 15, rectF.bottom - 15);
            canvas.drawRoundRect(inSideRectF, mInsideRadius, mInsideRadius, mPaintInside);

            drawText(str, number, startX, mStartY, canvas);
        }
    }

    private Rect textRect, numberRect;
    private float textWidth, textHeight, singleCharWidth, numberHeight, numberWidth;
    private List<String> mDrawTextList = new ArrayList<>();

    private void drawText(String str, String number, int rectStartX, int rectStatY, Canvas canvas) {
        mDrawTextList.clear();
        textRect = new Rect();
        mTextPaint.getTextBounds(str, 0, str.length(), textRect);
        textWidth = textRect.width();//字符串的宽度
        textHeight = textRect.height();//字符串的宽度

        numberRect = new Rect();
        mTextPaint.getTextBounds(number, 0, number.length(), numberRect);
        numberHeight = numberRect.height();//字符串的宽度
        numberWidth = numberRect.width();//字符串的宽度


        String ss = "好";
        numberRect = new Rect();
        mTextPaint.getTextBounds(ss, 0, 1, numberRect);
        singleCharWidth = numberRect.width();//字符串的宽度
        Log.i(TAG, "drawText: singleCharWidth = " + singleCharWidth);


        float textStartX/*, textStartY*/;

        int singleLineWidth = mRectWidth - 30 - DensityUtil.dip2px(getContext(), 4);
        int textTotalHeight = mRectHeight - 30 - mStrokeWidth;
        int maxShowLineNum;
        if (textWidth > singleLineWidth) {
            maxShowLineNum = (int) ((textTotalHeight-numberHeight)/textHeight);
            float textlineNum = textWidth / singleLineWidth;
            if (textlineNum > (int) textlineNum) {
                textlineNum = (int) (textlineNum) + 1;
            }
            int lineCharNum = (int) (str.length() / textlineNum);
            int maxLineCharNum = (int) (singleLineWidth / singleCharWidth) - 1;
            if (textlineNum < maxShowLineNum) {
                for (int i = 0; i < textlineNum; ++i) {
                    String lineStr;
                    //判断是否可以一行展示
                    if (str.length() < maxLineCharNum) {
                        lineStr = str.substring(0, str.length());
                    } else {
                        lineStr = str.substring(0, /*lineCharNum*/maxLineCharNum);
                    }
                    mDrawTextList.add(lineStr);
                    if (!TextUtils.isEmpty(str)) {
                        if (str.length() < /*lineCharNum*/maxLineCharNum) {
                            str = str.substring(0, str.length());
                        } else {
                            str = str.substring(/*lineCharNum*/maxLineCharNum, str.length());
                        }
                    } else {
                        break;
                    }
                }
            } else {
                textlineNum = maxShowLineNum;
                for (int i = 0; i < textlineNum; ++i) {
                    String lineStr;
                    //判断是否可以一行展示
                    if (str.length() < /*lineCharNum*/maxLineCharNum) {
                        lineStr = str.substring(0, str.length());
                    } else {
                        if (textlineNum - 1 == i) {
                            lineStr = str.substring(0, lineCharNum - 3);
                            mDrawTextList.add(lineStr + "...");
                            break;
                        } else {
                            lineStr = str.substring(0, /*lineCharNum*/maxLineCharNum);
                        }
                    }
                    mDrawTextList.add(lineStr);
                    if (!TextUtils.isEmpty(str)) {
                        if (str.length() < /*lineCharNum*/maxLineCharNum) {
                            str = str.substring(0, str.length());
                        } else {
                            str = str.substring(/*lineCharNum*/maxLineCharNum, str.length());
                        }
                    } else {
                        break;
                    }
                }
            }
        } else {
            mDrawTextList.add(str);
        }
        //text
        textStartX = rectStartX + (mRectWidth / 2) - textWidth / 2;
//        textStartY = rectStatY + (mRectHeight / 2) + textHeight / 2;
        int size = mDrawTextList.size();
        float startY = 0;
        float startX = 0;
//        int strokeWidth = mStrokeWidth;
        for (int i = 0; i < size; ++i) {
            startY = rectStatY + 15 + /*strokeWidth + */(textTotalHeight - size * textHeight - numberHeight) / 2 + (i + 1) * textHeight /*- numberHeight/2*/;
            if (size > 1) {
                startX = rectStartX + 15;
            } else {
                startX = textStartX;
            }
            canvas.drawText(mDrawTextList.get(i), startX, startY, mTextPaint);
        }
        canvas.drawText(number, rectStartX + (mRectWidth / 2) - numberWidth / 2, startY + numberHeight, mNumberPaint);
    }

    private void drawLine(int size, int startX, int startY, Canvas canvas) {
        mPaint.setColor(getResources().getColor(R.color.color_727a8f));
        mPaint.setStrokeWidth(mStrokeWidth);
        int mStartY, mEndY;
        //vertival line
//        for (int i = 0; i < size - 1; ++i) {
//            mStartY = startY + mFirstLineVerHeight * i;
//            mEndY = mStartY + mFirstLineVerHeight;
//            canvas.drawLine(startX, mStartY, startX, mEndY, mPaint);
//        }
        canvas.drawLine(startX, startY, startX, startY+(size-1)*(mRectHeight+mFirstRectGap), mPaint);
        //horizontal line
        for (int i = 0; i < size; ++i) {
//            mStartY = startY + mFirstLineVerHeight * i;
            mStartY = startY + i * (mFirstRectGap + mRectHeight );
            canvas.drawLine(startX, mStartY, startX + mLineHorHeight - DensityUtil.dip2px(getContext(), 5), mStartY, mPaint);
            canvas.drawCircle(startX + mLineHorHeight - DensityUtil.dip2px(getContext(), 5), mStartY, mPointRadius, mPaintPoint);
        }

    }

    private Point getFirstLinePoint(int size) {
        int startX = viewWidth / 2 - mRectWidth / 2 - mLineHorHeight;
        int startY = (viewHeight - mFirstLineVerHeight * (size - 1) - mRectHeight) / 2 + mMarginTop;
        Point point = new Point();
        point.x = startX;
        point.y = startY;
        return point;
    }


    private void performOnClick(int x, int y) {
        Log.i(TAG, "performOnClick: ");
        int size = mRectMap.size();
        int row, column;
        for (int i = 0; i < size; ++i) {
            List<RectF> rectFList = mRectMap.get(i);
            //List<PAtlasData> dataList = mPatlasMap.get(i);
            int listSize = rectFList.size();
            RectF rectF;
            PAtlasData pd;
            for (int j = 0; j < listSize; ++j) {
                rectF = rectFList.get(j);
                //pd = dataList.get(j);
                if (x > rectF.left && x < rectF.right && y > rectF.top && y < rectF.bottom) {
                    row = i;
                    column = j;
                    Log.i(TAG, "performOnClick: row = " + row + "column = " + column);
                    dealShowRect(row, column);
                    break;
                }
            }
        }
    }

    private void dealShowRect(int row, int column) {

        int size = mDrawItemList.size();
        boolean hasDrawed = false;

        DrawClickItem drawClickItem;
        int index;
        for (index = 0; index < size; ++index) {
            drawClickItem = mDrawItemList.get(index);
            if (drawClickItem.row > row) {
                //Log.i(TAG, "dealShowRect: drawClickItem" + drawClickItem);
                //Log.i(TAG, "dealShowRect: hasDrawed = true");
                hasDrawed = true;
                break;
            }
        }

        if (hasDrawed) {
            //if (getAtlas(mMap.get(row).get(column)).size() <= 0) {
            //  return;
            // } else {
            boolean hasShow = false;
            DrawClickItem drawClickItem1;
            for (int i = 0; i < size; ++i) {
                drawClickItem1 = mDrawItemList.get(i);
                if (drawClickItem1.row == row + 1 && column == drawClickItem1.column) {
                    hasShow = true;
                    break;
                }
            }
            for (int j = size - 1; j >= index; --j) {
                mDrawItemList.remove(j);
            }
            //Log.i(TAG, "dealShowRect: hasShow = " + hasShow);
            if (hasShow) {

            } else {
                mDrawItemList.add(new DrawClickItem(row + 1, column));
                mMap.put(row + 1, getAtlas(mMap.get(row).get(column)));
                mRectMap.put(row + 1, getRecfList(mMap.get(row).get(column)));
                //mPatlasMap.put(row+1,getAtlas(pAtlasData));
            }
            //}
        } else {
//            if (getAtlas(mMap.get(row).get(column)).size() <= 0) {
//                return;
//            }
            DrawClickItem drawClickItem2 = new DrawClickItem(row + 1, column);
            mDrawItemList.add(drawClickItem2);
            mMap.put(row + 1, getAtlas(mMap.get(row).get(column)));
            mRectMap.put(row + 1, getRecfList(mMap.get(row).get(column)));
        }
        invalidate();
    }

    private List<RectF> getRecfList(PAtlasData pAtlasData) {
        List<RectF> list = new ArrayList<>();
        RectF rectF = new RectF();
        for (int i = 0; i < getAtlas(pAtlasData).size(); i++) {
            list.add(rectF);
        }
        return list;
    }

    private float mLastDownX, mLastDownY;
    float offsetX, offsetY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastDownX = event.getX();
                mLastDownY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                offsetX = event.getX() - mLastDownX;
                offsetY = event.getY() - mLastDownY;
                mScrollX += offsetX;
                mScrollY += offsetY;
                invalidate();
                mLastDownX = event.getX();
                mLastDownY = event.getY();
//                Log.i(TAG, "onTouchEvent: ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                if (Math.abs(offsetX) < 0.0001 && Math.abs(offsetY) < 0.0001) {
                    performOnClick((int) mLastDownX, (int) mLastDownY);
                }
                break;
        }
        return true;
    }


    public KnowledgeMapView(Context context) {
        this(context, null);
    }


    public KnowledgeMapView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public KnowledgeMapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mPaint = new Paint();
        mPaintInside = new Paint();
        mPaintPoint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);//设置画圆弧的画笔的属性为描边(空心)，个人喜欢叫它描边，叫空心有点会引起歧义
        mPaintInside.setStyle(Paint.Style.FILL);
        mPaintPoint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);//取消锯齿
        mPaintInside.setAntiAlias(true);
        mPaintPoint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaintInside.setDither(true);
        mPaintPoint.setDither(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaintInside.setStrokeCap(Paint.Cap.ROUND);
        mPaintPoint.setStrokeWidth(DensityUtil.dip2px(getContext(), 2));
        mPaintPoint.setColor(getResources().getColor(R.color.color_727a8f));

        mTextPaint = new Paint();
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setTextSize(DensityUtil.sp2px(context, 12));
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setAntiAlias(true);

        mNumberPaint = new Paint();
        mNumberPaint.setStyle(Paint.Style.FILL);
        mNumberPaint.setTextSize(DensityUtil.sp2px(context, 10));
        mNumberPaint.setColor(Color.parseColor("#9196A1"));
        mNumberPaint.setAntiAlias(true);
        //setData(testData());
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        viewHeight = MeasureSpec.getSize(heightMeasureSpec);
    }

    private Map<Integer, List<PAtlasData>> testData() {
        Map<Integer, List<PAtlasData>> map = new HashMap<>();
        mRectMap.clear();
        RectF rect = new RectF();
        List<RectF> rectList = new ArrayList<>();
        for (int i = 0; i < mDataList.size(); i++) {
            rectList.add(rect);
        }
        mRectMap.put(0, rectList);
        map.put(0, mDataList);
        return map;
    }

    public void setData(Map<Integer, List<PAtlasData>> map) {
        mMap.clear();
        mDrawItemList.clear();
        mMap = map;
//        if (null == mMap) {
//            return;
//        }
        invalidate();
//        List<PAtlasData> list;
//        int mapSize = map.size();
//        mMaxRow = mapSize;
//        for (int i = 0; i < mapSize; ++i) {
//            list = map.get(i);
//            if (null == list) {
//                continue;
//            }
//            int listSize = list.size();
//            for (int j = 0; j < listSize; ++j) {
//                mTotalRectNum++;
//            }
//        }
        //Log.i(TAG, "setData: mTotalRectNum = " + mTotalRectNum);
    }


    private List<PAtlasData> getAtlas(PAtlasData pAtlasData) {
        List<PAtlasData> list = new ArrayList<>();
        if (null != pAtlasData.getSubModel() && pAtlasData.getSubModel().isJsonArray()) {
            for (JsonElement je : pAtlasData.getSubModel().getAsJsonArray()) {
                list.add(mGson.fromJson(je, PAtlasData.class));
            }
        }
        return list;
    }
}
