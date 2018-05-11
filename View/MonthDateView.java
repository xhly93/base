package com.ssj.user.Parent.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.ssj.user.Utils.DataTime;
import com.ssj.user.Utils.DensityUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by 17258 on 2018/5/9.
 */

public class MonthDateView extends View {

    private static final String TAG = "MonthDateView";
    private static final int NUM_COLUMNS = 7;
    private static final int NUM_ROWS = 6;
    private Paint mPaint;
    private int mDayColor = Color.WHITE;
    private int mSelectDayColor = Color.parseColor("#46C390");
    private int mSelectBGColor = Color.WHITE;
    private int mCurrentColor = Color.parseColor("#B3FFFFFF");
    private int mHomeWorkColor = Color.parseColor("#ECA50D");
    private int mProblemColor = Color.parseColor("#E45C58");
    private int mCurrYear, mCurrMonth, mCurrDay;
    private int mSelYear, mSelMonth, mSelDay;
    private int mColumnSize, mRowSize;
    private DisplayMetrics mDisplayMetrics;
    private int[][] daysString;
    private DateClick dateClick;
    private List<Integer> mHomeWorkList = new ArrayList<>();
    private List<Integer> mProblemList = new ArrayList<>();
    private TextView mMonthTextView;
    private TextView mYearTextView;

    public MonthDateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDisplayMetrics = getResources().getDisplayMetrics();
        Calendar calendar = Calendar.getInstance();
        mPaint = new Paint();
        mCurrYear = calendar.get(Calendar.YEAR);
        mCurrMonth = calendar.get(Calendar.MONTH);
        mCurrDay = calendar.get(Calendar.DATE);
        setSelectYearMonth(mCurrYear, mCurrMonth, mCurrDay);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (heightMode == MeasureSpec.AT_MOST) {
            heightSize = mDisplayMetrics.densityDpi * 200;
        }
        if (widthMode == MeasureSpec.AT_MOST) {
            widthSize = mDisplayMetrics.densityDpi * 300;
        }
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        initSize();
        daysString = new int[6][7];
        mPaint.setTextSize(/*mDaySize*mDisplayMetrics.scaledDensity*/DensityUtil.sp2px(getContext(), 12));
        String dayString;
        int mMonthDays = getDaysOfMonth(mSelYear, mSelMonth);
        int weekNumber = getFirstDayWeek(mSelYear, mSelMonth);
        Log.d(TAG, "DateView:" + mSelMonth + "month first day of week :" + weekNumber);
        for (int day = 0; day < mMonthDays; day++) {
//            dayString = (day + 1) + "";
            dayString = String.format("%02d", (day + 1));
            int column = (day + weekNumber - 1) % 7;
            int row = (day + weekNumber - 1) / 7;
            daysString[row][column] = day + 1;
            int startX = (int) (mColumnSize * column + (mColumnSize - mPaint.measureText(dayString)) / 2);
            int startY = (int) (mRowSize * row + mRowSize / 2 - (mPaint.ascent() + mPaint.descent()) / 2);

            if (((day + 1) == mCurrDay) && mCurrMonth == mSelMonth && mSelYear == mCurrYear) {
                //today bg
                mPaint.setColor(mCurrentColor);
                int startRecX = mColumnSize * column;
                int startRecY = mRowSize * row;
                int cx = startRecX + mColumnSize / 2;
                int cy = startRecY + mRowSize / 2;
                canvas.drawCircle(cx, cy, DensityUtil.dip2px(getContext(), 13), mPaint);
            }
            boolean drawSelectCircle = (((day + 1) == mSelDay) && mCurrDay != mSelDay && mCurrMonth == mSelMonth && mSelYear == mCurrYear) ||  (((day + 1) == mSelDay) &&( mCurrMonth != mSelMonth || mSelYear != mCurrYear));
            if (drawSelectCircle) {
                //select bg
                int startRecX = mColumnSize * column;
                int startRecY = mRowSize * row;
                int cx = startRecX + mColumnSize / 2;
                int cy = startRecY + mRowSize / 2;
                mPaint.setColor(mSelectBGColor);
                canvas.drawCircle(cx, cy, DensityUtil.dip2px(getContext(), 13), mPaint);
            }

            //绘制事务线条
            drawHomeWorkLines(row, day + 1, startX, startY, canvas);
            drawProblemLines(row, day + 1, startX, startY, canvas);
            if (((day + 1) == mSelDay)) {
                Log.i(TAG, "onDraw: ((day + 1) == mSelDay)");
                mPaint.setColor(mSelectDayColor);
            } else if (((day + 1) == mCurrDay) && mCurrDay != mSelDay && mCurrMonth == mSelMonth && mCurrYear == mSelYear) {
                //正常月，选中其他日期，则今日为红色
                Log.i(TAG, "onDraw: ((day + 1) == mCurrDay) && mCurrDay != mSelDay && mCurrMonth == mSelMonth");
                mPaint.setColor(mSelectDayColor);
            } else {
                mPaint.setColor(mDayColor);
            }
            canvas.drawText(String.format("%02d", (day + 1)), startX, startY, mPaint);
        }
    }


    private void drawHomeWorkLines(int row, int day, int startX, int startY, Canvas canvas) {
        if (mHomeWorkList != null && mHomeWorkList.size() > 0) {
            if (!mHomeWorkList.contains(day)) return;
            mPaint.setColor(mHomeWorkColor);
            mPaint.setStrokeWidth(DensityUtil.dip2px(getContext(), 2));
            startY = startY + DensityUtil.dip2px(getContext(), 3);
            if ((day + "").equals(mCurrDay + "") && mCurrMonth == mSelMonth) {
                int startRecY = mRowSize * row;
                startY = startRecY + mRowSize / 2 + DensityUtil.dip2px(getContext(), 15);
            }
            if ((day + "").equals(mSelDay + "") && mCurrDay != mSelDay) {
                int startRecY = mRowSize * row;
                startY = startRecY + mRowSize / 2 + DensityUtil.dip2px(getContext(), 15);
            }
            canvas.drawLine(startX, startY, startX + mPaint.measureText("00"), startY, mPaint);
        }
    }

    private void drawProblemLines(int row, int day, int startX, int startY, Canvas canvas) {
        if (mProblemList != null && mProblemList.size() > 0) {
            if (!mProblemList.contains(day)) return;
            mPaint.setColor(mProblemColor);
            mPaint.setStrokeWidth(DensityUtil.dip2px(getContext(), 2));
            startY = startY + DensityUtil.dip2px(getContext(), 3);
            if ((day + "").equals(mCurrDay + "") && mCurrMonth == mSelMonth) {
                int startRecY = mRowSize * row;
                startY = startRecY + mRowSize / 2 + DensityUtil.dip2px(getContext(), 15);
            }
            if ((day + "").equals(mSelDay + "") && mCurrDay != mSelDay) {
                int startRecY = mRowSize * row;
                startY = startRecY + mRowSize / 2 + DensityUtil.dip2px(getContext(), 15);
            }
            if (mHomeWorkList.contains(day)) {
                startY = startY + DensityUtil.dip2px(getContext(), 4);
            }
            canvas.drawLine(startX, startY, startX + mPaint.measureText("00"), startY, mPaint);
        }
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    private int downX = 0, downY = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int eventCode = event.getAction();
        switch (eventCode) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getX();
                downY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                int upX = (int) event.getX();
                int upY = (int) event.getY();
                if (Math.abs(upX - downX) < 10 && Math.abs(upY - downY) < 10) {//点击事件
                    performClick();
                    doClickAction((upX + downX) / 2, (upY + downY) / 2);
                }
                break;
        }
        return true;
    }

    /**
     * 初始化列宽行高
     */
    private void initSize() {
        mColumnSize = getWidth() / NUM_COLUMNS;
        mRowSize = getHeight() / NUM_ROWS;
    }

    /**
     * 设置年月
     *
     * @param year
     * @param month
     */
    public void setSelectYearMonth(int year, int month, int day) {
        mSelYear = year;
        mSelMonth = month;
        mSelDay = day;
    }

    /**
     * 执行点击事件
     *
     * @param x
     * @param y
     */
    private void doClickAction(int x, int y) {
        int row = y / mRowSize;
        int column = x / mColumnSize;
        setSelectYearMonth(mSelYear, mSelMonth, daysString[row][column]);
        invalidate();
        //执行activity发送过来的点击处理事件
        if (dateClick != null) {
            dateClick.onClickOnDate(mSelYear, mSelMonth, daysString[row][column]);
        }
    }

    /**
     * 左点击，日历向后翻页
     */
    public void onLeftClick() {
        Log.i(TAG, "onLeftClick: ");
        int year = mSelYear;
        int month = mSelMonth;
        int day = mSelDay;
        if (month == 0) {//若果是1月份，则变成12月份
            year = mSelYear - 1;
            month = 11;
        } else if (getDaysOfMonth(year, month) == day) {
            //如果当前日期为该月最后一点，当向前推的时候，就需要改变选中的日期
            month = month - 1;
            day = getDaysOfMonth(year, month);
        } else {
            month = month - 1;
        }
        setSelectYearMonth(year, month, day);
        mSelDay = 0;
        mMonthTextView.setText(DataTime.getMonthStr(getContext(), month + 1));
        mYearTextView.setText(String.valueOf(year));
        invalidate();
    }

    /**
     * 右点击，日历向前翻页
     */
    public void onRightClick() {
        Log.i(TAG, "onRightClick: ");
        int year = mSelYear;
        int month = mSelMonth;
        int day = mSelDay;
        if (month == 11) {//若果是12月份，则变成1月份
            year = mSelYear + 1;
            month = 0;
        } else if (getDaysOfMonth(year, month) == day) {
            //如果当前日期为该月最后一点，当向前推的时候，就需要改变选中的日期
            month = month + 1;
            day = getDaysOfMonth(year, month);
        } else {
            month = month + 1;
        }
        setSelectYearMonth(year, month, day);
        mSelDay = 0;
        mMonthTextView.setText(DataTime.getMonthStr(getContext(), month + 1));
        mYearTextView.setText(String.valueOf(year));
        invalidate();
    }


    public void onYearClick(int selYear) {
        Log.i(TAG, "onYearClick: selYear = " + selYear + "mLastSelYear = " + mSelYear);
        if (selYear == mSelYear) {
            return;
        }
        setSelectYearMonth(selYear, mSelMonth, mSelDay);
        if (mCurrYear != selYear) {
            mSelDay = 0;
        }
        mYearTextView.setText(selYear + "");
        invalidate();
    }


    /**
     * 获取选择的年份
     *
     * @return
     */
    public int getmSelYear() {
        return mSelYear;
    }

    /**
     * 获取选择的月份
     *
     * @return
     */
    public int getmSelMonth() {
        return mSelMonth;
    }


    /**
     * 获取选择的日期
     *
     * @param
     */
    public int getmSelDay() {
        return this.mSelDay;
    }


    public int getCurrYear() {
        return mCurrYear;
    }

    public int getCurrMonth() {
        return mCurrMonth;
    }

    public int getCurrDay() {
        return mCurrDay;
    }

    /**
     * 普通日期的字体颜色，默认黑色
     *
     * @param mDayColor
     */
    public void setmDayColor(int mDayColor) {
        this.mDayColor = mDayColor;
    }

    /**
     * 选择日期的颜色，默认为白色
     *
     * @param mSelectDayColor
     */
    public void setmSelectDayColor(int mSelectDayColor) {
        this.mSelectDayColor = mSelectDayColor;
    }

    /**
     * 选中日期的背景颜色，默认蓝色
     *
     * @param mSelectBGColor
     */
    public void setmSelectBGColor(int mSelectBGColor) {
        this.mSelectBGColor = mSelectBGColor;
    }

    /**
     * 当前日期不是选中的颜色，默认红色
     *
     * @param mCurrentColor
     */
    public void setmCurrentColor(int mCurrentColor) {
        this.mCurrentColor = mCurrentColor;
    }


    public void setHomeWorkList(List<Integer> homeWorkList) {
        this.mHomeWorkList = homeWorkList;
    }


    public void setProblemList(List<Integer> problemList) {
        this.mProblemList = problemList;
    }

    /**
     * 设置日期的点击回调事件
     *
     * @author shiwei.deng
     */
    public interface DateClick {
        public void onClickOnDate(int year,int month,int day);
    }

    /**
     * 设置日期点击事件
     *
     * @param dateClick
     */
    public void setDateClick(DateClick dateClick) {
        this.dateClick = dateClick;
    }

    /**
     * 跳转至今天
     */
    public void setTodayToView() {
        setSelectYearMonth(mCurrYear, mCurrMonth, mCurrDay);
        invalidate();
    }

    public static int getDaysOfMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 1);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public static int getFirstDayWeek(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 1);
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        int dayofweek = 0;
        switch (day) {
            case 1:
                dayofweek = 7;
                break;
            case 2:
                dayofweek = 1;
                break;
            case 3:
                dayofweek = 2;
                break;
            case 4:
                dayofweek = 3;
                break;
            case 5:
                dayofweek = 4;
                break;
            case 6:
                dayofweek = 5;
                break;
            case 7:
                dayofweek = 6;
                break;
        }
        return dayofweek;
    }


    public void setMonthView(TextView textView) {
        mMonthTextView = textView;
    }

    public void setYearView(TextView textView) {
        mYearTextView = textView;
    }

}
