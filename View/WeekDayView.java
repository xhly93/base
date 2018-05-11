package com.ssj.user.Parent.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import com.ssj.user.R;
import com.ssj.user.Utils.DensityUtil;

/**
 * Created by 17258 on 2018/5/9.
 */

public class WeekDayView extends View {

    private int mWeedayColor = Color.WHITE;
    private int mWeekSize = 15;
    private Paint paint;
    private String[] weekString = new String[7];

    public WeekDayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        weekString[0] = context.getString(R.string.pmonday);
        weekString[1] = context.getString(R.string.ptuesday);
        weekString[2] = context.getString(R.string.pwednesday);
        weekString[3] = context.getString(R.string.pthursday);
        weekString[4] = context.getString(R.string.pfriday);
        weekString[5] = context.getString(R.string.psaturday);
        weekString[6] = context.getString(R.string.psunday);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();

        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(DensityUtil.sp2px(getContext(), mWeekSize));
        int columnWidth = width / 7;
        for (int i = 0; i < weekString.length; i++) {
            String text = weekString[i];
            int fontWidth = (int) paint.measureText(text);
            int startX = columnWidth * i + (columnWidth - fontWidth) / 2;
            int startY = (int) (height / 2 - (paint.ascent() + paint.descent()) / 2);
            paint.setColor(mWeedayColor);
            canvas.drawText(text, startX, startY, paint);
        }
    }


    /**
     * 设置周一-五的颜色
     *
     * @return
     */
    public void setmWeedayColor(int mWeedayColor) {
        this.mWeedayColor = mWeedayColor;
    }


    /**
     * 设置字体的大小
     *
     * @param mWeekSize
     */
    public void setmWeekSize(int mWeekSize) {
        this.mWeekSize = mWeekSize;
    }


    /**
     * 设置星期的形式
     *
     * @param weekString 默认值  "日","一","二","三","四","五","六"
     */
    public void setWeekString(String[] weekString) {
        this.weekString = weekString;
    }


}
