package com.martin.district.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.martin.district.DistrictApplication;

import java.util.List;

/**
 * Created by martin on 16/6/7.
 */
public class DBInterface {

    private static DBInterface dbInterface = null;

    private DaoMaster.DevOpenHelper openHelper;

    public synchronized static DBInterface instance() {
        if (dbInterface == null) {
            dbInterface = new DBInterface();
        }
        return dbInterface;
    }

    public DBInterface() {
        initDbHelp();
    }

    private void initDbHelp() {
        final Context applicationContext = DistrictApplication.getContext();
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(applicationContext, "address.db", null);
        openHelper = helper;
    }

    private void isInitOk() {
        if (openHelper == null) {
            throw new RuntimeException("DBInterface#isInit not success or start,cause by openHelper is null");
        }
    }

    /**
     * Query for readable DB
     */
    private DaoSession openReadableDb() {
        isInitOk();
        SQLiteDatabase db = openHelper.getReadableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        return daoSession;
    }

    /**
     * Query for writable DB
     */
    private DaoSession openWritableDb() {
        isInitOk();
        SQLiteDatabase db = openHelper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        return daoSession;
    }

    /****
     * @param districtInfos
     */
    public void bulkInsert(List<DistrictInfo> districtInfos) {
        DistrictInfoDao dao = openWritableDb().getDistrictInfoDao();
        dao.insertOrReplaceInTx(districtInfos);
    }

    public long queryCount() {
        DistrictInfoDao dao = openReadableDb().getDistrictInfoDao();
        return dao.count();
    }

    /****
     * 查询省份
     *
     * @return
     */
    public List<DistrictInfo> queryProvinces() {
        DistrictInfoDao dao = openReadableDb().getDistrictInfoDao();
        return dao.queryBuilder().where(DistrictInfoDao.Properties.CityCode.eq(100000))
                .orderAsc(DistrictInfoDao.Properties.AdCode).list();
    }

}
