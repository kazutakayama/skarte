package com.example.skarte.service;

import java.util.List;

import com.example.skarte.entity.Attendance;

//インターフェイス
public interface AttendanceService {

    /**
     * 出欠全取得
     * 
     * @return
     */
    public List<Attendance> findAll();

    /**
     * 出欠1件取得
     * 
     * @param id
     * @return
     */
    public Attendance findById(Long id);

    /**
     * 出欠追加
     * 
     * @param attendance
     * @return
     */
    public void addAttendance(Attendance attendance);

    /**
     * 出欠編集
     * 
     * @param attendance
     * @return
     */
    public Attendance updateAttendance(Long attendanceId, Attendance attendance);

    /**
     * 出欠CSVダウンロード用
     * 
     * @param attendance
     */
    public void insertAttendance(Attendance attendance);

    /**
     * 出欠削除（理論削除）
     * 
     * @param attendance
     */
    public void deleteAttendance(Long attendanceId, Attendance attendance);

}
