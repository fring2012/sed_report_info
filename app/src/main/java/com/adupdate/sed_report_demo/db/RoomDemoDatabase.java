package com.adupdate.sed_report_demo.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.adupdate.sed_report_demo.db.dao.IpAddressDao;
import com.adupdate.sed_report_demo.entity.IpAddress;
import com.adupdate.sed_report_demo.entity.TimeRecord;
import com.adupdate.sed_report_demo.db.dao.TimetableDao;

@Database(entities = {TimeRecord.class,IpAddress.class}, version = 1)
public abstract class RoomDemoDatabase extends RoomDatabase {
    public static final String DB_NAME = "record";

    public abstract TimetableDao timetableDao();

    public abstract IpAddressDao ipAddressDao();
}
