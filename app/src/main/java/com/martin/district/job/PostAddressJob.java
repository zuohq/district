package com.martin.district.job;

import com.martin.district.db.DBInterface;
import com.martin.district.db.DistrictInfo;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import java.util.List;

/**
 * Created by martin on 16/6/7.
 */
public class PostAddressJob extends Job {

    private List<DistrictInfo> districtInfos;

    public PostAddressJob(List<DistrictInfo> districtInfos) {
        super(new Params(0).requireNetwork().groupBy("post-address"));

        this.districtInfos = districtInfos;
    }

    @Override
    public void onAdded() {
        DBInterface.instance().bulkInsert(districtInfos);
        districtInfos.clear();
    }

    @Override
    public void onRun() throws Throwable {

    }

    @Override
    protected void onCancel() {

    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        return false;
    }
}
