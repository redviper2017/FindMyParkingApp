package com.tanzee.findmyparkingapp.db;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import java.util.Map;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig profileDaoConfig;
    private final DaoConfig mySpotsDaoConfig;
    private final DaoConfig mySpotsFrequencyDaoConfig;

    private final ProfileDao profileDao;
    private final MySpotsDao mySpotsDao;
    private final MySpotsFrequencyDao mySpotsFrequencyDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        profileDaoConfig = daoConfigMap.get(ProfileDao.class).clone();
        profileDaoConfig.initIdentityScope(type);

        mySpotsDaoConfig = daoConfigMap.get(MySpotsDao.class).clone();
        mySpotsDaoConfig.initIdentityScope(type);

        mySpotsFrequencyDaoConfig = daoConfigMap.get(MySpotsFrequencyDao.class).clone();
        mySpotsFrequencyDaoConfig.initIdentityScope(type);

        profileDao = new ProfileDao(profileDaoConfig, this);
        mySpotsDao = new MySpotsDao(mySpotsDaoConfig, this);
        mySpotsFrequencyDao = new MySpotsFrequencyDao(mySpotsFrequencyDaoConfig, this);

        registerDao(Profile.class, profileDao);
        registerDao(MySpots.class, mySpotsDao);
        registerDao(MySpotsFrequency.class, mySpotsFrequencyDao);
    }
    
    public void clear() {
        profileDaoConfig.clearIdentityScope();
        mySpotsDaoConfig.clearIdentityScope();
        mySpotsFrequencyDaoConfig.clearIdentityScope();
    }

    public ProfileDao getProfileDao() {
        return profileDao;
    }

    public MySpotsDao getMySpotsDao() {
        return mySpotsDao;
    }

    public MySpotsFrequencyDao getMySpotsFrequencyDao() {
        return mySpotsFrequencyDao;
    }

}
