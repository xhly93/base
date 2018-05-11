package com.ssj.user.BaseUtil;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView Adapter基类
 * Created by 17258 on 2018/3/28.
 */

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseHolder> {

    private List<T> mList = new ArrayList<>();
    private int layoutId;
    private Context mContext;

    public BaseAdapter(Context context,int layoutId, List<T> list) {
        this.layoutId = layoutId;
        this.mList = list;
        this.mContext = context;
    }

    public Context getmContext() {
        return mContext;
    }

    //onCreateViewHolder用来给rv创建缓存
    @Override
    public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        BaseHolder baseHolder = new BaseHolder(view);
        return baseHolder;
    }

    //onBindViewHolder给缓存控件设置数据
    @Override
    public void onBindViewHolder(BaseHolder holder, int position) {
        T item = mList.get(position);
        setListener(holder, item,position);
        setContent(holder, item,position);

    }

    /**
     * 设置内容
     * @param holder
     * @param item
     */
    public abstract void setContent(BaseHolder holder, T item,int position);

    /**
     * 设置点击事件
     * @param holder
     * @param item
     */
    public abstract void setListener(BaseHolder holder, T item,int position);

    //获取记录数据
    @Override
    public int getItemCount() {
        return mList.size();
    }
}
