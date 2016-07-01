package com.martin.district;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.amap.api.services.district.DistrictItem;
import com.amap.api.services.district.DistrictResult;
import com.amap.api.services.district.DistrictSearch;
import com.amap.api.services.district.DistrictSearchQuery;
import com.martin.district.db.DistrictInfo;
import com.martin.district.job.PostAddressJob;
import com.martin.district.util.IoUtils;
import com.martin.district.util.SimpleBackgroundTask;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DistrictSearch.OnDistrictSearchListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private Handler mHandler = new Handler();
    private DistrictPopupWindow mPopupWindow;
    private View mRootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new SimpleBackgroundTask<Void>(this) {

            @Override
            protected Void onRun() {
                if (!IoUtils.isExistsDb(MainActivity.this)) {
                    IoUtils.releaseAddressDb(MainActivity.this);
                }
                return null;
            }

            @Override
            protected void onSuccess(Void result) {
                Toast.makeText(MainActivity.this, "db release complete!", Toast.LENGTH_SHORT).show();
            }
        }.execute();


        mRootLayout = findViewById(R.id.root_layout);

        findViewById(R.id.btn_show).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow();//
            }
        });
    }


    private void showPopupWindow() {
        if (mPopupWindow == null)
            mPopupWindow = new DistrictPopupWindow(this, R.layout.district_popup);
        mPopupWindow.setUp();

        mPopupWindow.showAtLocation(mRootLayout, Gravity.BOTTOM, 0, 0);
    }

    private void initData() {
        querySubDistrict("中国", DistrictSearchQuery.KEYWORDS_COUNTRY);
    }

    /****
     * 查询下级区划
     *
     * @param name
     * @param level
     */
    public void querySubDistrict(String name, String level) {
        DistrictSearch districtSearch = new DistrictSearch(MainActivity.this);
        districtSearch.setOnDistrictSearchListener(MainActivity.this);
        // 异步查询行政区
        districtSearch.searchDistrictAnsy();
        DistrictSearchQuery query = new DistrictSearchQuery(name, level, 0);
        districtSearch.setQuery(query);
    }

    /**
     * 查询下级区划
     *
     * @param districtItem 要查询的区划对象
     */
    private void querySubDistrict(DistrictItem districtItem) {
        querySubDistrict(districtItem.getName(), districtItem.getLevel());
    }

    /****
     * 将地址批量插入数据库
     *
     * @param districtItem
     */
    private void bulkInsert(DistrictItem districtItem) {
        List<DistrictItem> subDistrict = districtItem.getSubDistrict();

        ArrayList<DistrictInfo> districtInfos = new ArrayList<>();

        for (final DistrictItem item : subDistrict) {

            DistrictInfo districtInfo = new DistrictInfo();

            districtInfo.setCityCode(Integer.valueOf(districtItem.getAdcode()));
            districtInfo.setName(item.getName());
            districtInfo.setAdCode(Integer.valueOf(item.getAdcode()));
            districtInfos.add(districtInfo);

            Log.i(TAG, "districtInfo:" + districtInfo.toString());

            if (DistrictSearchQuery.KEYWORDS_PROVINCE.equals(item.getLevel())
                    || DistrictSearchQuery.KEYWORDS_CITY.equals(item.getLevel())) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        querySubDistrict(item);
                    }
                }, 100);
            }
        }

        Log.i(TAG, districtInfos.toString());

        if (districtInfos.size() > 0) {
            DistrictApplication application = (DistrictApplication) getApplication();
            application.getJobManager().addJobInBackground(new PostAddressJob(districtInfos));
        }
    }

    /**
     * 返回District（行政区划）异步处理的结果
     */
    @Override
    public void onDistrictSearched(DistrictResult result) {
        if (result != null) {
            if (result.getAMapException().getErrorCode() == 1000) {

                ArrayList<DistrictItem> district = result.getDistrict();

                for (int i = 0; i < district.size(); i++) {
                    DistrictItem districtItem = district.get(i);
                    String level = districtItem.getLevel();

                    if (level.equals(DistrictSearchQuery.KEYWORDS_COUNTRY)) {
                        //省
                        bulkInsert(districtItem);

                    } else if (level.equals(DistrictSearchQuery.KEYWORDS_PROVINCE)) {//省
                        bulkInsert(districtItem);

                    } else if (level.equals(DistrictSearchQuery.KEYWORDS_CITY)) {//市
                        bulkInsert(districtItem);
                    }
                }
            }
        }
    }
}
