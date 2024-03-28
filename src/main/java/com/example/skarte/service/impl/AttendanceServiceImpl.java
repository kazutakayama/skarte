package com.example.skarte.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.skarte.entity.Attendance;
import com.example.skarte.entity.StudentYear;
import com.example.skarte.repository.AttendanceRepository;
import com.example.skarte.service.AttendanceService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;

    /**
     * 出欠全取得
     * 
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<Attendance> findAll() {
        return attendanceRepository.findByDeletedFalseOrderByUpdatedAtDesc();
    }
    
    /**
     * 生徒IDでリストを取得
     * 
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<Attendance> findAllByStudentId(Long studentId) {
        return attendanceRepository.findAllByStudentId(studentId);
    }


    /**
     * 出欠1件取得
     * 
     * @param id
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public Attendance findById(Long id) {
        return attendanceRepository.findById(id).orElseThrow();
    }

    /**
     * 出欠追加
     * 
     * @param attendance
     * @return
     */
    @Override
    @Transactional
    public void addAttendance(Attendance attendance) {
        attendanceRepository.save(attendance);
    }

    /**
     * 出欠編集
     * 
     * @param attendance
     * @return
     */
    @Override
    @Transactional
    public Attendance updateAttendance(Long attendanceId, Attendance attendance) {
        Attendance targetAttendance = attendanceRepository.findById(attendanceId).orElseThrow();
        targetAttendance.setDate(attendance.getDate());
        targetAttendance.setChikoku(attendance.getChikoku());
        targetAttendance.setSoutai(attendance.getSoutai());
        targetAttendance.setKesseki(attendance.getKesseki());
        targetAttendance.setSyuttei(attendance.getSyuttei());
        targetAttendance.setKibiki(attendance.getKibiki());
        return targetAttendance;
//        studentRepository.save(student);
    }

    /**
     * 出欠CSVダウンロード用
     * 
     * @param attendance
     */
    @Override
    @Transactional
    public void insertAttendance(Attendance attendance) {
        attendanceRepository.save(attendance);
    }

    /**
     * 出欠削除（理論削除）
     * 
     * @param attendance
     */
    @Override
    @Transactional
    public void deleteAttendance(Long attendanceId, Attendance attendance) {
        Attendance targetAttendance = attendanceRepository.findById(attendanceId).orElseThrow();
        targetAttendance.setDeleted(Boolean.TRUE);
        attendanceRepository.save(targetAttendance);
    }
}