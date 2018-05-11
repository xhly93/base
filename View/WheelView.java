package com.ssj.user.Parent.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetricsInt;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


import com.ssj.user.Utils.DensityUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 滚轮选择器
 */
public class WheelView extends View {

    public static final String TAG = "WheelView";

    private static final int height = 50;//单个ite高度dp

    /**
     * 自动回滚到中间的速度
     */
    public static final float SPEED = 2;

    /**
     * 除选中item外，上下各需要显示的备选项数目
     */
    public static final int SHOW_SIZE = 4;

    private Context context;

    private List<String> itemList;
    private int itemCount;

    /**
     * item高度
     */
    private int itemHeight = 50;

    /**
     * 选中的位置，这个位置是mDataList的中心位置，一直不变
     */
    private int currentItem;

    private Paint mPaint;
    // 画背景图中单独的画笔
    private Paint mFirstPaint;

    private float startY;
    private float centerX;

    private float mLastDownY;
    /**
     * 滑动的距离
     */
    private float mMoveLen = 0;
    private boolean isInit = false;
    private SelectListener mSelectListener;
    private OnClickListener mClickListener;
    private Timer timer;
    private MyTimerTask mTask;

    Handler updateHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (Math.abs(mMoveLen) < SPEED) {
                // 如果偏移量少于最少偏移量
                mMoveLen = 0;
                if (null != timer) {
                    timer.cancel();
                    timer.purge();
                    timer = null;
                }
                if (mTask != null) {
                    mTask.cancel();
                    mTask = null;
                    performSelect();
                }
            } else {
                // 这里mMoveLen / Math.abs(mMoveLen)是为了保有mMoveLen的正负号，以实现上滚或下滚
                mMoveLen = mMoveLen - mMoveLen / Math.abs(mMoveLen) * SPEED;
            }
            invalidate();
        }

    };

    public WheelView(Context context) {
        super(context);
        init(context);
    }

    public WheelView(Context context, AttributeSet attrs) {
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

        timer = new Timer();
        itemList = new ArrayList<>();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextAlign(Align.CENTER);
        mPaint.setTextSize(DensityUtil.sp2px(getContext(), 17));


        mFirstPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mFirstPaint.setTextAlign(Align.CENTER);
        mFirstPaint.setTextSize(DensityUtil.sp2px(getContext(), 17));

        // 绘制背景
        setBackground(null);
//        setWheelStyle();
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
            drawFirstText(canvas);
            //绘制下方块背景
            mPaint.setColor(Color.WHITE);
            canvas.drawRect(0, DensityUtil.dip2px(context, height), mViewWidth, mViewHeight, mPaint);
            // 绘制下方data
            for (int i = 1; i < SHOW_SIZE; i++) {
                drawOtherText(canvas, i, 1);
            }
        }
    }

    private void drawFirstText(Canvas canvas) {
        mFirstPaint.setColor(Color.parseColor("#46C390"));
        canvas.drawRect(0, 0, mViewWidth, DensityUtil.dip2px(context, height), mFirstPaint);

        // text居中绘制，注意baseline的计算才能达到居中，y值是text中心坐标
        float y = startY + mMoveLen;
        mFirstPaint.setColor(Color.WHITE);
        FontMetricsInt fmi = mFirstPaint.getFontMetricsInt();
        float baseline = (float) (y - (fmi.bottom / 2.0 + fmi.top / 2.0));
        canvas.drawText(itemList.get(currentItem), centerX, baseline, mFirstPaint);
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

        itemHeight = /*getHeight() / (SHOW_SIZE * 2 + 1)*/DensityUtil.dip2px(context, height);
        float d = itemHeight * position + type * mMoveLen;
        float y = startY + type * d;

        FontMetricsInt fmi = mPaint.getFontMetricsInt();
        float baseline = (float) (y - (fmi.bottom / 2.0 + fmi.top / 2.0));

        mPaint.setColor(Color.BLACK);
        canvas.drawText(text, centerX, baseline, mPaint);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                doDown(event);
                break;
            case MotionEvent.ACTION_MOVE:
                doMove(event);
                break;
            case MotionEvent.ACTION_UP:
                doUp();
                break;
            default:
                break;
        }
        return true;
    }

    private void doDown(MotionEvent event) {
        if (mTask != null) {
            mTask.cancel();
            mTask = null;
        }
        mLastDownY = event.getY();
    }

    private void doMove(MotionEvent event) {
        mMoveLen += (event.getY() - mLastDownY);

        if (mMoveLen > itemHeight / 2) {
            // 往下滑超过离开距离
            mMoveLen = mMoveLen - itemHeight;
            currentItem--;
            if (currentItem < 0) {
                currentItem = itemCount - 1;
            }
        } else if (mMoveLen < -itemHeight / 2) {
            // 往上滑超过离开距离
            mMoveLen = mMoveLen + itemHeight;
            currentItem++;
            if (currentItem >= itemCount) {
                currentItem = 0;
            }
        }

        mLastDownY = event.getY();
        invalidate();
    }

    private void doUp() {
        // 抬起手后mCurrentSelected的位置由当前位置move到中间选中位置
        if (Math.abs(mMoveLen) < 0.0001) {
            int y = DensityUtil.px2dip(getContext(), mLastDownY);
            if (y < height) {
//                currentItem = currentItem+1;
            } else if (y > height && y < height * 2) {
                currentItem = currentItem + 1;
            } else if (y > height * 2 && y < height * 3) {
                currentItem = currentItem + 2;
            } else if (y > height * 3 && y < height * 4) {
                currentItem = currentItem + 3;
            }
            performOnClick();
//            if (mLastDownY){
//
//            }
            mMoveLen = 0;
            return;
        }
        if (mTask != null) {
            mTask.cancel();
            mTask = null;
        }
        if (null == timer) {
            timer = new Timer();
        }
        mTask = new MyTimerTask(updateHandler);
        timer.schedule(mTask, 0, 10);
    }

    class MyTimerTask extends TimerTask {
        Handler handler;

        public MyTimerTask(Handler handler) {
            this.handler = handler;
        }

        @Override
        public void run() {
            handler.sendMessage(handler.obtainMessage());
        }

    }

    private void performSelect() {
        if (mSelectListener != null) {
            mSelectListener.onSelect(currentItem, itemList.get(currentItem));
        } else {
//            Log.i(TAG, "null listener");
        }
    }

    private void performOnClick() {
        if (mClickListener != null) {
            mClickListener.onClick(currentItem, itemList.get(currentItem));
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

}
