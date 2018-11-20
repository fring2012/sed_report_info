package com.adupdate.sed_report_demo.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.adupdate.sed_report_demo.entity.TimeRecord;


import java.util.LinkedList;
import java.util.List;
@Dao
public interface TimetableDao {
    @Query("SELECT * FROM tb_timetable")
    List<TimeRecord> getAll();

    @Query("SELECT * FROM tb_timetable WHERE id = :id")
    TimeRecord getSingleTimetable(int id);

    /**
     * 获取制定开机时间的记录，bootTime计算有误差，所以取值在1000ms以内
     * @param bootTime
     * @return
     */
    @Query("SELECT * FROM tb_timetable WHERE  boot_time between :bootTime-1000 and :bootTime+1000")
    TimeRecord getNewestTimetable(long bootTime);

    /**
     * 获取制定开机时间的记录，bootTime计算有误差，所以取值在1000ms以内
     * @return
     */
    @Query("SELECT * FROM tb_timetable WHERE off_time = 0 order by id desc limit 0,1")
    TimeRecord getNewestTimetable();

    /**
     * 查询异常未记录关机时间的记录
     * @param bootTime
     * @return
     */
    @Query("SELECT * FROM tb_timetable WHERE off_time=0 and (boot_time<(:bootTime - 1000) or boot_time > (:bootTime + 1000))")
    List<TimeRecord> getOffTimeUnrecorded(long bootTime);


    /**
     * 获得未上报的记录
     * @return
     */
    @Query("SELECT * FROM tb_timetable WHERE is_report <> 2 and off_time <> 0")
    List<TimeRecord> getNotReportTimetable();

    @Query("SELECT * FROM tb_timetable WHERE (boot_time >= :bdt and boot_time < :et) " +
            "or (off_time >= :bdt and off_time < :et) " +
            "or (boot_time <= :bdt and off_time > :et)" +
            "or (boot_time <= :bdt and off_time = 0)")
    List<TimeRecord> getDayRunTime(long bdt,long et);

    @Query("DELETE  FROM tb_timetable WHERE off_time <> 0 and off_time < :cbd and boot_time < :cbd")
    void cleanReportTimetable(long cbd);

    @Query("DELETE FROM tb_timetable WHERE id = :id")
    void deleteById(int id);

    @Insert(onConflict = 3)
    void insert(TimeRecord timeRecord);

    @Delete
    void delete(TimeRecord timeRecord);

    @Update
    void update(TimeRecord timeRecord);

    @Query("UPDATE tb_timetable SET is_report = 1 WHERE id = :id and off_time <> 0")
    void updateIsReportById(int id);
}


