package com.ssj.user.Parent.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.ssj.user.R;
import com.ssj.user.Utils.DensityUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 滚轮选择器
 */
public class HorizontalWheelView extends View {

    public static final String TAG = "WheelView";

    private static final int height = 37;//单个ite高度dp

    /**
     * 自动回滚到中间的速度
     */
    public static final float SPEED = 2;

    /**
     * 除选中item外，上下各需要显示的备选项数目
     */
    public static final int SHOW_SIZE = 3;

    private Context context;

    private List<String> itemList;
    private int itemCount;

    /**
     * item高度
     */
    private int itemHeight = 37;

    /**
     * 选中的位置，这个位置是mDataList的中心位置，一直不变
     */
    private int currentItem;

    private Paint mPaint;
    // 画背景图中单独的画笔
    private Paint mCenterPaint;

    private float startY;
    private float centerX;

    private float mLastDownX;

    private int mCenterTextcolor = Color.parseColor("#7C819D");
    private int mTop2Textcolor = Color.parseColor("#DADEEB");

    private int mCenterTextSize = 12;
    private int mOtherTextSize = 12;
    /**
     * 滑动的距离
     */
    private float mMoveLen = 0;
    private boolean isInit = false;
    private SelectListener mSelectListener;
    private OnClickListener mClickListener;


    public HorizontalWheelView(Context context) {
        super(context);
        init(context);
    }

    public HorizontalWheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    //滑动选择
    public void setOnSelectListener(SelectListener listener) {
        mSelectListener = listener;
    }

    //点击选择
    public void setClickListener(OnClickListener listener) {
        mClickListener = listener;
    }

    public void setDatas(List<String> list) {
        this.itemList = list;
        setWheelStyle();
    }

    private void setWheelStyle() {
//        itemList = createYearString();
        if (itemList != null) {
            itemCount = itemList.size();
            resetCurrentSelect();
            invalidate();
        } else {
//            Log.i(TAG, "item is null");
        }
    }


    private void resetCurrentSelect() {
        if (currentItem < 0) {
            currentItem = 0;
        }
        while (currentItem >= itemCount) {
            currentItem--;
        }
        if (currentItem >= 0 && currentItem < itemCount) {
            invalidate();
        } else {
//            Log.i(TAG, "current item is invalid");
        }
    }

    public int getItemCount() {
        return itemCount;
    }

    /**
     * 选择选中的item的index
     */
    public void setCurrentItem(int selected) {
        currentItem = selected;
        resetCurrentSelect();
    }

    public int getCurrentItem() {
        return currentItem;
    }

    int mViewWidth, mViewHeight;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mViewHeight = getMeasuredHeight();
        mViewWidth = getMeasuredWidth();
        centerX = (float) (mViewWidth / 2.0);
        startY = /*(float) (mViewHeight / 2.0)*/DensityUtil.dip2px(context, height / 2);
        isInit = true;
        invalidate();
    }


    private void init(Context context) {
        this.context = context;

        itemList = new ArrayList<>();

        mOtherTextSize = DensityUtil.sp2px(getContext(), mOtherTextSize);
        mCenterTextSize = DensityUtil.sp2px(getContext(), mCenterTextSize);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        mPaint.setTextAlign(Align.CENTER);
        mPaint.setTextSize(mOtherTextSize);

        mCenterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        mCenterPaint.setTextAlign(Align.CENTER);
        mCenterPaint.setTextSize(mCenterTextSize);

        // 绘制背景
        setBackground(null);
//        setWheelStyle();

        setDatas(createYearString());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isInit) {
            drawData(canvas);
        }
    }

    private void drawData(Canvas canvas) {
        // 先绘制选中的text再往上往下绘制其余的text
        if (!itemList.isEmpty()) {
            // 绘制第一个data
//            drawCenterText(canvas);
            //绘制下方块背景
            mPaint.setColor(Color.WHITE);
            canvas.drawRect(0, 0, mViewWidth, mViewHeight, mPaint);
            // 绘制下方data
            for (int i = 0; i < SHOW_SIZE; i++) {
                drawOtherText(canvas, i, 1);
            }
        }
    }


    /**
     * @param canvas   画布
     * @param position 距离mCurrentSelected的差值
     * @param type     1表示向下绘制，-1表示向上绘制
     */
    private void drawOtherText(Canvas canvas, int position, int type) {


        int index = currentItem + type * position;
        if (index >= itemCount) {
            index = index - itemCount;
        }
        if (index < 0) {
            index = index + itemCount;
        }
        String text = itemList.get(index);
        Log.i(TAG, "drawOtherText: position = " + position + "text = " + text);

        itemHeight = DensityUtil.dip2px(context, height);
        float d = itemHeight * position + type * mMoveLen;
        float y = startY + type * d;

        FontMetricsInt fmi = mPaint.getFontMetricsInt();
//        float baseline = (float) (y - (fmi.bottom / 2.0 + fmi.top / 2.0));
        float baseline = (float) (startY - (fmi.bottom / 2.0 + fmi.top / 2.0));

        float x = (position) * mPaint.measureText(text, 0, text.length()) + position * DensityUtil.dip2px(getContext(), 25);

        if (position == 1) {
            mPaint.setColor(mCenterTextcolor);
            mPaint.setTextSize(mCenterTextSize);
        } else {
            mPaint.setColor(mTop2Textcolor);
            mPaint.setTextSize(mOtherTextSize);
        }
        if (position == 0) {
            x = 0;
        } else if (position == 1) {
            x = mViewWidth / 2 - mPaint.measureText(text, 0, text.length()) / 2;
        } else if (position == 2) {
            x = mViewWidth - mPaint.measureText(text, 0, text.length());
        }
        canvas.drawText(text, x, baseline, mPaint);
    }


    int mLastX,mLastY;
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        int x = (int) event.getX();
        int y = (int) event.getY();
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = x - mLastX;
                int deltaY = y - mLastY;
                if (Math.abs(deltaX) < Math.abs(deltaY)){                              //在这里的if中加入是否拦截的判断
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
            case MotionEvent.ACTION_UP:
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
            default:
                break;
        }
        mLastX = x;
        mLastY = y;
        return super.dispatchTouchEvent(event);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                mLastDownX = event.getX();
                break;
//            case MotionEvent.ACTION_MOVE:
////                doMove(event);
//                break;
            case MotionEvent.ACTION_UP:
//                doUp();
                Log.i(TAG, "onTouchEvent: ACTION_UP");
                if (Math.abs(event.getX() - mLastDownX) > 150) {
                    doMove(event);
                    performSelect();
                }

                break;
            default:
                break;
        }
        return true;
    }


    private void doMove(MotionEvent event) {
        mMoveLen += (event.getX() - mLastDownX);
        float lenght = mPaint.measureText(itemList.get(0), 0, itemList.get(0).length());

        int scaledTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        if (mMoveLen > scaledTouchSlop) {
            // 往下滑超过离开距离
            mMoveLen = mMoveLen - lenght;
            currentItem--;
            if (currentItem < 0) {
                currentItem = itemCount - 1;
            }
        } else if (mMoveLen < -scaledTouchSlop) {
            // 往上滑超过离开距离
            mMoveLen = mMoveLen + lenght;
            currentItem++;
            if (currentItem >= itemCount) {
                currentItem = 0;
            }
        }

        mLastDownX = 0;
        invalidate();
    }


    private void performSelect() {
        if (mSelectListener != null) {
            mSelectListener.onSelect(currentItem, itemList.get(currentItem));
        } else {
//            Log.i(TAG, "null listener");
        }
    }


    public interface SelectListener {
        void onSelect(int index, String text);
    }

    public interface OnClickListener {
        void onClick(int index, String text);
    }


    public List<String> createYearString() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        List<String> wheelString = new ArrayList<>();
        for (int i = year; i >= 1970; i--) {
            wheelString.add(Integer.toString(i));
        }
        return wheelString;
    }

    public List<String> createYearMonthString() {

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        List<String> yearDatas = new ArrayList<>();
        for (int i = 2000; i <= year + 1; i++) {
            for (int j = 1; j <= 12; j++) {
                yearDatas.add(Integer.toString(i) + context.getString(R.string.years) + String.format("%02d", j) + context.getString(R.string.months));

            }
        }

//        List<String> wheelString = new ArrayList<>();
//        for (int i = 1; i <= 12; i++) {
//            wheelString.add(String.format("%02d", i) + context.getString(R.string.months));
//        }
        return yearDatas;
    }

    public String getCurentItemStr() {
        int i = currentItem + 1;
        if (i >= itemList.size()) {
            i = i - itemList.size();
        }
        return itemList.get(i);
    }

}
