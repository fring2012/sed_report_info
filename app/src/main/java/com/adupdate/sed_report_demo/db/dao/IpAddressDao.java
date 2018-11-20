package com.adupdate.sed_report_demo.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.adupdate.sed_report_demo.entity.IpAddress;

import java.util.List;

@Dao
public interface IpAddressDao {

    @Query("SELECT * FROM ip_address")
    List<IpAddress> getAll();

    @Insert
    void insert(IpAddress ipAddress);
}
