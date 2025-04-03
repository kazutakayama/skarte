package com.example.skarte.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.skarte.entity.Attendance;
import com.example.skarte.entity.Karte;
import com.example.skarte.entity.StudentYear;
import com.example.skarte.repository.AttendanceRepository;
import com.example.skarte.repository.KarteRepository;

@Service
public class AttendanceService {

    @Autowired
    AttendanceRepository attendanceRepository;

    /**
     * 出欠全取得
     * 
     * @return
     */
    public List<Attendance> findAll() {
        return attendanceRepository.findByOrderByUpdatedAtDesc();
    }

    /**
     * 生徒IDでリストを取得
     * 
     * @return
     */
    public List<Attendance> findAllByStudentId(String studentId) {
        return attendanceRepository.findAllByStudentId(studentId);
    }

    /**
     * 出欠1件取得
     * 
     * @param id
     * @return
     */
    public Attendance findById(Long id) {
        return attendanceRepository.findById(id).orElseThrow();
    }

    /**
     * 出欠追加
     * 
     * @param attendance
     * @return
     */
    public void addAttendance(String userId, String studentId, Attendance attendance) {
        attendance.setStudentId(attendance.getStudentId());
        attendance.setCreatedBy(userId);
        attendance.setUpdatedBy(userId);
        attendanceRepository.save(attendance);
    }

    /**
     * 出欠更新
     * 
     * @param attendance
     * @return
     */
    public Attendance updateAttendance(Long attendanceId, String studentId, Attendance attendance) {
        attendance.setStudentId(attendance.getStudentId());
        Attendance targetAttendance = attendanceRepository.findById(attendanceId).orElseThrow();
        targetAttendance.setDate(attendance.getDate());
        targetAttendance.setChikoku(attendance.getChikoku());
        targetAttendance.setSoutai(attendance.getSoutai());
        targetAttendance.setKesseki(attendance.getKesseki());
        targetAttendance.setSyuttei(attendance.getSyuttei());
        targetAttendance.setKibiki(attendance.getKibiki());
        targetAttendance.setUpdatedBy(attendance.getUpdatedBy());
        attendanceRepository.save(targetAttendance);
        return targetAttendance;
    }
    
    /**
     * 出欠削除
     * 
     * @param karte
     */
    public Attendance deleteAttendance(Long attendanceId, String studentId, Attendance attendance) {
        attendance.setStudentId(attendance.getStudentId());
        Attendance deleteAttendance = attendanceRepository.findById(attendanceId).orElseThrow();
        attendanceRepository.delete(deleteAttendance);
        return deleteAttendance;
    }


//    /**
//     * 出欠CSVダウンロード用
//     * 
//     * @param attendance
//     */
//    public void insertAttendance(Attendance attendance) {
//        attendanceRepository.save(attendance);
//    }

//    /**
//     * 出欠削除（理論削除）
//     * 
//     * @param attendance
//     */
//  public void deleteAttendance(Long attendanceId, Attendance attendance) {
//  Attendance targetAttendance = attendanceRepository.findById(attendanceId).orElseThrow();
//  targetAttendance.setDeleted(Boolean.TRUE);
//  attendanceRepository.save(targetAttendance);
//}

}

//package com.example.skarte.service;
//
//import java.util.List;
//
//import com.example.skarte.entity.Attendance;
//import com.example.skarte.entity.StudentYear;
//
////インターフェイス
//public interface AttendanceService {
//
//    /**
//     * 出欠全取得
//     * 
//     * @return
//     */
//    public List<Attendance> findAll();
//    
//    /**
//     * 生徒IDでリストを取得
//     * 
//     * @return
//     */
//    public List<Attendance> findAllByStudentId(Long studentId);
//
//    /**
//     * 出欠1件取得
//     * 
//     * @param id
//     * @return
//     */
//    public Attendance findById(Long id);
//
//    /**
//     * 出欠追加
//     * 
//     * @param attendance
//     * @return
//     */
//    public void addAttendance(Attendance attendance);
//
//    /**
//     * 出欠編集
//     * 
//     * @param attendance
//     * @return
//     */
//    public Attendance updateAttendance(Long attendanceId, Attendance attendance);
//
//    /**
//     * 出欠CSVダウンロード用
//     * 
//     * @param attendance
//     */
//    public void insertAttendance(Attendance attendance);
//
////    /**
////     * 出欠削除（理論削除）
////     * 
////     * @param attendance
////     */
////    public void deleteAttendance(Long attendanceId, Attendance attendance);
//
//}
