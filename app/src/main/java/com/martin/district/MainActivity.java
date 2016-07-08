package com.martin.district;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.martin.district.common.BaseActivity;
import com.martin.district.db.DBInterface;
import com.martin.district.db.DistrictInfo;
import com.martin.district.util.IoUtils;
import com.martin.district.util.SimpleBackgroundTask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends BaseActivity implements DistrictPopupWindow.OptionListener {

    public static final String PROVINCE = "province";
    public static final String CITY = "city";
    public static final String DISTRICT = "district";

    private DistrictPopupWindow mPopupWindow;
    private View mRootLayout;
    private TextView mDistrict;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        releaseDb();

        initView();

        queryDistricts();
    }

    private void initView() {
        mRootLayout = findViewById(R.id.root_layout);

        mDistrict = (TextView) findViewById(R.id.district);

        findViewById(R.id.btn_show).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow();
            }
        });
    }

    /***
     * 释放DB
     */
    private void releaseDb() {
        if (!IoUtils.isExistsDb(this)) {
            new SimpleBackgroundTask<Void>(this) {

                @Override
                protected Void onRun() {
                    IoUtils.releaseAddressDb(MainActivity.this);
                    return null;
                }

                @Override
                protected void onSuccess(Void result) {
                    Toast.makeText(MainActivity.this, "db release complete!", Toast.LENGTH_SHORT).show();
                }
            }.execute();
        }
    }

    public void queryDistricts() {
        new SimpleBackgroundTask<Map<String, List<DistrictInfo>>>(this) {

            @Override
            protected Map<String, List<DistrictInfo>> onRun() {
                HashMap<String, List<DistrictInfo>> map = new HashMap<>();

                DBInterface dbInterface = DBInterface.instance();
                List<DistrictInfo> provinceList = dbInterface.queryProvinces();

                DistrictInfo provinceInfo = provinceList.get(0);
                List<DistrictInfo> cityList = dbInterface.queryDistrict(provinceInfo.getAdCode());

                DistrictInfo cityInfo = cityList.get(0);
                List<DistrictInfo> districtInfoList = dbInterface.queryDistrict(cityInfo.getAdCode());

                map.put(PROVINCE, provinceList);
                map.put(CITY, cityList);
                map.put(DISTRICT, districtInfoList);
                return map;
            }

            @Override
            protected void onSuccess(Map<String, List<DistrictInfo>> map) {
                if (mPopupWindow == null)
                    mPopupWindow = new DistrictPopupWindow(MainActivity.this, R.layout.district_popup, map);
                mPopupWindow.setOptionListener(MainActivity.this);
            }
        }.execute();
    }

    private void showPopupWindow() {
        if (mPopupWindow != null) {
            mPopupWindow.setUp();
            mPopupWindow.showAtLocation(mRootLayout, Gravity.BOTTOM, 0, 0);
        }
    }

    @Override
    public void selectedOption(String province, String city, String district) {
        mDistrict.setText(province + city + district);
    }
}
