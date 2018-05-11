package com.ssj.user.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ssj.user.Mode.Data.ProblemData;
import com.ssj.user.Mode.network.ImageLoader;
import com.ssj.user.R;
import com.ssj.user.BaseUtil.RoundImageView;

import java.util.ArrayList;

/**
 * Created by 王矩龙 on 2018/3/7.
 */

public class ProblemListViewAdapter extends BaseAdapter {
    private ArrayList<ProblemData> mHomeWorkDataList = new ArrayList<>();
    private Context mContext;
    private LayoutInflater mInflater;
    private int selectIndex = -1;

    public ProblemListViewAdapter(Context context, ArrayList<ProblemData> list) {
        this.mContext = context;
        this.mHomeWorkDataList = list;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(mContext);
    }

    public void setDatas(ArrayList<ProblemData> list){
        this.mHomeWorkDataList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return  mHomeWorkDataList.size();
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

        ProblemListViewAdapter.ViewHolder holder;
        if (convertView == null) {
            holder = new ProblemListViewAdapter.ViewHolder();
            convertView = mInflater.inflate(R.layout.problem_list_item, null);
            holder.mWorkImage = (RoundImageView) convertView.findViewById(R.id.item_problem_image);
            holder.mWeekText = (TextView) convertView.findViewById(R.id.item_week);
            holder.mDataText = (TextView) convertView.findViewById(R.id.item_data);
            holder.mTimeText = (TextView) convertView.findViewById(R.id.item_time);
            holder.mFeedBackImage = (ImageView) convertView.findViewById(R.id.item_feedback_image);
            convertView.setTag(holder);
        } else {
            holder = (ProblemListViewAdapter.ViewHolder) convertView.getTag();
        }
        if (position == selectIndex) {
            convertView.setSelected(true);
        } else {
            convertView.setSelected(false);
        }
        ImageLoader.getInstance().loadImage(mHomeWorkDataList.get(position).getFileKey(), holder.mWorkImage, mContext);
        holder.mWeekText.setText(mHomeWorkDataList.get(position).getWeekday());
        holder.mDataText.setText(DataTime.getDate(mHomeWorkDataList.get(position).getCreateTime()));
        holder.mTimeText.setText(DataTime.getTime(mHomeWorkDataList.get(position).getCreateTime()));
        if (mHomeWorkDataList.get(position).getIsFeedback() == 0) {
            holder.mFeedBackImage.setImageResource(R.drawable.wei_fan_kui);
        } else {
            holder.mFeedBackImage.setImageResource(R.drawable.yi_fan_kui);
        }
        return convertView;
    }

    private static class ViewHolder {
        //private TextView mTitle;
        private RoundImageView mWorkImage;
        private TextView mWeekText;
        private TextView mDataText;
        private TextView mTimeText;
        private ImageView mFeedBackImage;
    }

    public void setSelectIndex(int i) {
        selectIndex = i;
    }
}
