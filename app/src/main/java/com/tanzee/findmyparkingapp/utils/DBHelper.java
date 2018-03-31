package com.tanzee.findmyparkingapp.utils;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

import com.tanzee.findmyparkingapp.db.DaoMaster;
import com.tanzee.findmyparkingapp.db.DaoSession;
import com.tanzee.findmyparkingapp.db.MySpots;
import com.tanzee.findmyparkingapp.db.MySpotsDao;
import com.tanzee.findmyparkingapp.db.MySpotsFrequency;
import com.tanzee.findmyparkingapp.db.MySpotsFrequencyDao;
import com.tanzee.findmyparkingapp.db.Profile;
import com.tanzee.findmyparkingapp.db.ProfileDao;


public class DBHelper{

    private SQLiteOpenHelper sqLiteOpenHelper;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private ProfileDao profileDao;
    private MySpotsDao mySpotsDao;
    private MySpotsFrequencyDao mySpotsFrequencyDao;

    private final String DB_NAME = "find-my-parking-db";

    public DBHelper(Context context){
        sqLiteOpenHelper = new DaoMaster.DevOpenHelper(context, DB_NAME, null);
        daoMaster = new DaoMaster(sqLiteOpenHelper.getWritableDatabase());
        daoSession = daoMaster.newSession();
        profileDao = daoSession.getProfileDao();
        mySpotsDao = daoSession.getMySpotsDao();
        mySpotsFrequencyDao = daoSession.getMySpotsFrequencyDao();
    }

    public Profile getProfileByEmail(String email){
        return profileDao.queryBuilder().where(ProfileDao.Properties.Email.eq(email)).unique();
    }

    public void updateProfile(Profile profile){
        profileDao.insertOrReplace(profile);
    }

    public void inserMySpots(MySpots mySpots){
        long count = mySpotsDao.queryBuilder().where(MySpotsDao.Properties.Destination.eq(mySpots.getDestination())).count();
//        if(spot == null){
//            mySpots.setFrequency(0);
//            mySpotsDao.insertOrReplace(mySpots);
//        }else {
//            mySpots.setFrequency(spot.getFrequency() + 1);
//            mySpotsDao.insertOrReplace(mySpots);
//        }

        mySpots.setFrequency((int)count + 1);
        mySpotsDao.insertOrReplace(mySpots);

    }

    public void insertMySpotsByFrequency(MySpotsFrequency mySpots){
        MySpotsFrequency spot = mySpotsFrequencyDao.queryBuilder().where(MySpotsFrequencyDao.Properties.Destination.eq(mySpots.getDestination())).unique();
        if(spot == null){
            mySpots.setFrequency(0);
            mySpotsFrequencyDao.insertOrReplace(mySpots);
        }else{
            mySpots.setFrequency(spot.getFrequency() + 1);
            mySpotsFrequencyDao.insertOrReplace(mySpots);
        }
    }

    public List<MySpots> getMySpotsAsList(){
        return mySpotsDao.queryBuilder().list();
    }

    public List<MySpotsFrequency> getMySpotsAsListByFrequency(){
        return mySpotsFrequencyDao.queryBuilder().orderDesc(MySpotsFrequencyDao.Properties.Frequency).list();
    }
}
