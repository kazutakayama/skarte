package com.example.skarte.form;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.example.skarte.entity.EntityBase;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)

public class AttendanceForm extends EntityBase {

    /** ID */
    private Long attendanceId = null;

    private String studentId = null;

    /** 日付 */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date = null;

    /** 記録 */
    private Integer kiroku = null;

    
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
    private List<LocalDate> dates;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public List<LocalDate> getDates() {
        return dates;
    }

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public void setDates(List<LocalDate> dates) {
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
