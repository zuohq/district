package com.martin.district;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.martin.district.adapter.ListWheelAdapter;
import com.martin.district.common.BasePopupWindow;
import com.martin.district.db.DBInterface;
import com.martin.district.db.DistrictInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    // 下级行政区划集合
    private Map<Long, List<DistrictInfo>> subDistrictMap = new HashMap<>();

    private WheelView mProvoiceWheelView;
    private WheelView mCityWheelView;
    private WheelView mDistrictWheelView;

    private List<DistrictInfo> provinceList = new ArrayList<>();
    private List<DistrictInfo> cityList = new ArrayList<>();
    private List<DistrictInfo> districtList = new ArrayList<>();

    private OptionListener listener;

    public DistrictPopupWindow(Context context, int layoutId, Map<String, List<DistrictInfo>> map) {
        super(context, layoutId);
        this.dbInterface = DBInterface.instance();
        provinceList.addAll(map.get(MainActivity.PROVINCE));
        cityList.addAll(map.get(MainActivity.CITY));
        districtList.addAll(map.get(MainActivity.DISTRICT));
    }

    @Override
    protected void setUpMenuView(Context mContext, View contentView) {
        initView(contentView);

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
                int option1 = mProvoiceWheelView.getCurrentItem();
                String province = provinceList.get(option1).getName();
                int option2 = mCityWheelView.getCurrentItem();
                String city = (cityList.size() > option2) ? cityList.get(option2).getName() : null;
                int option3 = mDistrictWheelView.getCurrentItem();
                String district = (districtList.size() > option3) ? districtList.get(option3).getName() : null;

                if (listener != null) {
                    listener.selectedOption(province, city, district);
                }
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
                //城市列表
                DistrictInfo districtInfo = provinceList.get(newValue);
                setSelectedView(districtInfo, cityList, mCityWheelView);

                //区 列表
                districtInfo = !cityList.isEmpty() ? cityList.get(0) : null;
                setSelectedView(districtInfo, districtList, mDistrictWheelView);
            }
        });

        mCityWheelView.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                DistrictInfo districtInfo = cityList.get(newValue);
                setSelectedView(districtInfo, districtList, mDistrictWheelView);
            }
        });

    }

    private void setSelectedView(DistrictInfo districtInfo, List<DistrictInfo> subDistrictList, WheelView wheelView) {
        List<DistrictInfo> districtInfos = null;

        if (districtInfo != null) {
            if (subDistrictMap.containsKey(districtInfo.getAdCode())) {
                districtInfos = subDistrictMap.get(districtInfo.getAdCode());
            } else {
                districtInfos = DBInterface.instance().queryDistrict(districtInfo.getAdCode());
                subDistrictMap.put(districtInfo.getAdCode(), districtInfos);
            }
        }

        if (null == districtInfos || null == districtInfo) {
            districtInfos = new ArrayList<>();
        }

        subDistrictList.clear();
        subDistrictList.addAll(districtInfos);

        wheelView.setViewAdapter(new ListWheelAdapter<>(getContext(), subDistrictList));

        if (districtInfos.size() > 0)
            wheelView.setCurrentItem(0);
    }

    public void setUpView() {
        mProvoiceWheelView.setViewAdapter(new ListWheelAdapter<>(getContext(), provinceList));
        //选中第一条
        mProvoiceWheelView.setCurrentItem(0);

        mCityWheelView.setViewAdapter(new ListWheelAdapter<>(getContext(), cityList));
        //选中第一条
        mCityWheelView.setCurrentItem(0);

        mDistrictWheelView.setViewAdapter(new ListWheelAdapter<>(getContext(), districtList));
        //选中第一条
        mDistrictWheelView.setCurrentItem(0);

    }

    public void setOptionListener(OptionListener listener) {
        this.listener = listener;
    }

    public interface OptionListener {

        /***
         * 选中项
         *
         * @param province
         * @param city
         * @param district
         */
        void selectedOption(String province, String city, String district);
    }

}
