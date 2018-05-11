package com.ssj.user.BaseUtil;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ssj.user.R;
import com.ssj.user.Utils.DensityUtil;

/**
 * Created by 17258 on 2018/3/29.
 */

public class CommenToolBar extends LinearLayout {

    private boolean showbackBtn, showRightImg, showRightText;
    private String title;
    private ImageView backBtn;
    private ImageView mRightImage;
    private TextView titleView, mRightTextView;

    public CommenToolBar(Context context) {
        this(context, null);
    }

    public CommenToolBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.comen_toolbar, null);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        addView(rootView, layoutParams);
        titleView = rootView.findViewById(R.id.toolbar_main_text);
        backBtn = rootView.findViewById(R.id.toolbar_back);
        mRightImage = rootView.findViewById(R.id.toolbar_right_img);
        mRightTextView = rootView.findViewById(R.id.toolbar_right_text);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ToolaBar);
        showbackBtn = typedArray.getBoolean(R.styleable.ToolaBar_showBackBtn, true);
        showRightImg = typedArray.getBoolean(R.styleable.ToolaBar_showRightImg, false);
        showRightText = typedArray.getBoolean(R.styleable.ToolaBar_showRightText, false);
        if (showRightImg) {
            mRightImage.setVisibility(VISIBLE);
        }
        if (showRightText) {
            mRightTextView.setVisibility(VISIBLE);
        }
        title = typedArray.getString(R.styleable.ToolaBar_titleText);
        titleView.setText(title);
        typedArray.recycle();
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        backBtn.setOnClickListener(l);
    }

    public void setRightClickListener(OnClickListener l) {
        if (mRightImage.getVisibility() == VISIBLE) {
            mRightImage.setOnClickListener(l);
        } else if (mRightTextView.getVisibility() == VISIBLE) {
            mRightTextView.setOnClickListener(l);
        }
    }

    public void setRightImageVisibility(int show) {
        mRightImage.setVisibility(show);
    }

    public void setRightImageResource(int res) {
        mRightImage.setBackgroundResource(res);
    }

    public void setTitleTextSize(float size) {
        titleView.setTextSize(size);
    }

}
