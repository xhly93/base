package com.ssj.user.BaseUtil;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ssj.user.Mode.network.ImageLoader;
import com.ssj.user.SSApplication;
import com.ssj.user.Utils.BitmapUtils;

import java.util.HashMap;

/**
 * RecyclerView Holder基类
 * Created by 17258 on 2018/3/28.
 */

public class BaseHolder extends RecyclerView.ViewHolder {

    //不写死控件变量，而采用Map方式
    private HashMap<Integer, View> mViews = new HashMap<>();

    public BaseHolder(View itemView) {
        super(itemView);
    }

    /**
     * 获取控件的方法
     */
    public <T> T getView(Integer viewId) {
        //根据保存变量的类型 强转为该类型
        View view = mViews.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            //缓存
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 传入文本控件id和设置的文本值，设置文本
     */
    public BaseHolder setTextViewText(Integer viewId, String value) {
        TextView textView = getView(viewId);
        if (textView != null) {
            textView.setText(value);
        }
        return this;
    }


    /**
     * 传入文本控件id和设置的文本颜色
     */
    public BaseHolder setTextViewColor(Integer viewId, int color) {
        TextView textView = getView(viewId);
        if (textView != null) {
            textView.setTextColor(color);
        }
        return this;
    }

    /**
     * 设置图片
     */
    public BaseHolder setImageResource(Integer viewId, Drawable drawable) {
        ImageView imageView = getView(viewId);
        if (imageView != null) {
            imageView.setImageDrawable(drawable);
        }
        return this;
    }

    /**
     * 传入图片控件id和资源id，设置图片
     */
    public BaseHolder setImageResource(Integer viewId, String url) {
        ImageView imageView = getView(viewId);
        if (imageView != null) {
            BitmapUtils.loadBitmapToImage( SSApplication.getInstance().getApplicationContext(),url,imageView);
        }
        return this;
    }

    public BaseHolder setViewClick(Integer viewId, View.OnClickListener listener) {
        View view = getView(viewId);
        if (view != null) {
            view.setOnClickListener(listener);
        }
        return this;
    }

    public BaseHolder setImageViewClick(Integer viewId, View.OnClickListener listener) {
        ImageView imageView = getView(viewId);
        if (imageView != null) {
            imageView.setOnClickListener(listener);
        }
        return this;
    }

}
