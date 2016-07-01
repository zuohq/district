package com.martin.district.db;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import com.martin.district.db.DistrictInfo;

import com.martin.district.db.DistrictInfoDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig districtInfoDaoConfig;

    private final DistrictInfoDao districtInfoDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        districtInfoDaoConfig = daoConfigMap.get(DistrictInfoDao.class).clone();
        districtInfoDaoConfig.initIdentityScope(type);

        districtInfoDao = new DistrictInfoDao(districtInfoDaoConfig, this);

        registerDao(DistrictInfo.class, districtInfoDao);
    }
    
    public void clear() {
        districtInfoDaoConfig.getIdentityScope().clear();
    }

    public DistrictInfoDao getDistrictInfoDao() {
        return districtInfoDao;
    }

}
