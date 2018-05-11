package com.ssj.user.Utils;


import android.content.Context;

import com.ssj.user.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by 王矩龙 on 2018/3/6.
 */

public class DataTime {
    public static int getWeek(Long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        int i = cal.get(Calendar.DAY_OF_WEEK);
        switch (i) {
            case 1:
                return R.string.sunday;
            case 2:
                return R.string.monday;
            case 3:
                return R.string.tuesday;
            case 4:
                return R.string.wednesday;
            case 5:
                return R.string.friday;
            case 6:
                return R.string.friday;
            case 7:
                return R.string.starday;
            default:
                return 0;
        }
    }

    public static String getDate(Long time) {
        Date d = new Date(time);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy.MM.dd");
        return sf.format(d);
    }

    public static String getTime(Long time) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        int hour = c.get(Calendar.HOUR);
        int minuts = c.get(Calendar.MINUTE);

        return hour + ":" + minuts;
    }

    public static String getYMDHMSDate(long time) {
        Date currentTime = new Date(time);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    public static String getMonthStr(Context context,int month) {
        String result = "";
        switch (month) {
            case 1:
                result  = context.getString(R.string.January);
                break;
            case 2:
                result  = context.getString(R.string.February);
                break;
            case 3:
                result  = context.getString(R.string.March);
                break;
            case 4:
                result  = context.getString(R.string.April);
                break;
            case 5:
                result  = context.getString(R.string.May);
                break;
            case 6:
                result  = context.getString(R.string.June);
                break;
            case 7:
                result  = context.getString(R.string.July);
                break;
            case 8:
                result  = context.getString(R.string.August);
                break;
            case 9:
                result  = context.getString(R.string.September);
                break;
            case 10:
                result  = context.getString(R.string.October);
                break;
            case 11:
                result  = context.getString(R.string.November);
                break;
            case 12:
                result  = context.getString(R.string.December);
                break;
            default:
               break;
        }
        return result;
    }

}
