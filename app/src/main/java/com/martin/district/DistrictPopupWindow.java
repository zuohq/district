package com.martin.district;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.martin.district.adapter.ListWheelAdapter;
import com.martin.district.common.BasePopupWindow;
import com.martin.district.db.DBInterface;
import com.martin.district.db.DistrictInfo;

import java.util.List;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;

/**
 * Created by martin on 16/6/8.
 */
public class DistrictPopupWindow extends BasePopupWindow {

    private static final String TAG = DistrictPopupWindow.class.getSimpleName();

    private final DBInterface dbInterface;
    private Button mBtnConfirm;
    private Button mBtnCancel;

    private WheelView mProvoiceWheelView;
    private WheelView mCityWheelView;
    private WheelView mDistrictWheelView;


    private List<DistrictInfo> provinceList;

    public DistrictPopupWindow(Context context, int layoutId) {
        super(context, layoutId);

        dbInterface = DBInterface.instance();
    }

    @Override
    protected void setUpMenuView(Context mContext, View contentView) {
        initView(contentView);

        long count = DBInterface.instance().queryCount();

        Log.i(TAG, "count = " + count);

        initData();

        setUpView();
    }

    private void initData() {
        provinceList = dbInterface.queryProvinces();
    }

    private void initView(View contentView) {
        mBtnConfirm = (Button) contentView.findViewById(R.id.btnConfirm);
        mBtnCancel = (Button) contentView.findViewById(R.id.btnCancel);

        mProvoiceWheelView = (WheelView) contentView.findViewById(R.id.wheelview_province);
        mCityWheelView = (WheelView) contentView.findViewById(R.id.wheelview_city);
        mDistrictWheelView = (WheelView) contentView.findViewById(R.id.wheelview_district);

        mBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mProvoiceWheelView.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {

            }
        });

        mCityWheelView.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {

            }
        });
    }

    public void setUpView() {
        mProvoiceWheelView.setViewAdapter(new ListWheelAdapter<>(getContext(), provinceList));
        //选中第一条
        mProvoiceWheelView.setCurrentItem(0);

        mCityWheelView.setViewAdapter(new ListWheelAdapter<>(getContext(), provinceList));
        //选中第一条
        mCityWheelView.setCurrentItem(0);

        mDistrictWheelView.setViewAdapter(new ListWheelAdapter<>(getContext(), provinceList));
        //选中第一条
        mDistrictWheelView.setCurrentItem(0);
    }

}
