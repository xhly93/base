package com.ssj.user.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.ssj.user.Mode.network.ImageLoader;
import com.ssj.user.R;
import com.ssj.user.BaseUtil.RoundImageView;

import java.util.ArrayList;

/**
 * Created by 王矩龙 on 2018/3/1.
 */

public class HomeWorkListViewAdapter extends BaseAdapter {

    private ArrayList<String> mImageKeylist = new ArrayList<>();
    private Context mContext;
    private LayoutInflater mInflater;
    private int selectIndex = -1;

    public HomeWorkListViewAdapter(Context context, ArrayList<String> list) {
        this.mContext = context;
        this.mImageKeylist = list;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        if (null == mImageKeylist) {
            return 0;
        }
        return mImageKeylist.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.homework_list_item, null);
            holder.mImage = (RoundImageView) convertView.findViewById(R.id.img_list_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position == selectIndex) {
            convertView.setSelected(true);
        } else {
            convertView.setSelected(false);
        }
        ImageLoader.getInstance().loadImage(mImageKeylist.get(position), holder.mImage, mContext);
        return convertView;
    }

    private static class ViewHolder {
        private RoundImageView mImage;
    }


    public void setSelectIndex(int i) {
        selectIndex = i;
    }
}