package com.martin.district.common;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

/**
 * Created by martin on 16/6/8.
 */
public abstract class BasePopupWindow extends PopupWindow {

    private Context mContext;
    private View contentView;

    public BasePopupWindow(Context context, int layoutId) {
        super(context);

        mContext = context;
        contentView = LayoutInflater.from(context).inflate(layoutId, null);
        setContentView(contentView);

        //window width,height
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        setBackgroundDrawable(new ColorDrawable());
        setFocusable(true);
    }

    protected Context getContext() {
        return mContext;
    }

    public void setUp() {
        setUpMenuView(mContext, contentView);
    }

    protected abstract void setUpMenuView(Context mContext, View contentView);

}
