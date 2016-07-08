package com.martin.district.common;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.amap.api.services.district.DistrictItem;
import com.amap.api.services.district.DistrictResult;
import com.amap.api.services.district.DistrictSearch;
import com.amap.api.services.district.DistrictSearchQuery;
import com.martin.district.DistrictApplication;
import com.martin.district.db.DistrictInfo;
import com.martin.district.job.PostAddressJob;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @author: Created by martin on 16-7-7.
 */
public class BaseActivity extends AppCompatActivity implements DistrictSearch.OnDistrictSearchListener {

    private static final String TAG = BaseActivity.class.getSimpleName();

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // copy 高德地图行政区域数据到数据库
//        initData();
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
        DistrictSearch districtSearch = new DistrictSearch(this);
        districtSearch.setOnDistrictSearchListener(this);
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
