package com.example.skarte.form;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.example.skarte.entity.EntityBase;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)

public class AttendanceForm extends EntityBase {

    private Long attendanceId = null;

    private String studentId = null;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date = null;

    private Integer kiroku = null;

    // リスト
    private List<Long> attendanceIds;

    public List<Long> getAttendanceIds() {
        return attendanceIds;
    }

    public void setAttendanceIds(List<Long> attendanceIds) {
        this.attendanceIds = attendanceIds;
    }

    private List<String> studentIds;

    public List<String> getStudentIds() {
        return studentIds;
    }

    public void setStudentIds(List<String> studentIds) {
        this.studentIds = studentIds;
    }

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private List<Date> dates;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public List<Date> getDates() {
        return dates;
    }

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public void setDates(List<Date> dates) {
        this.dates = dates;
    }

    private List<Integer> kirokus;

    public List<Integer> getKirokus() {
        return kirokus;
    }

    public void setKirokus(List<Integer> kirokus) {
        this.kirokus = kirokus;
    }

}
