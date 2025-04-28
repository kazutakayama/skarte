package com.example.skarte.form;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.example.skarte.entity.EntityBase;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ScheduleForm extends EntityBase {

    private Long scheduleId = null;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date = null;

    private Boolean holiday = false;

    // リスト
    private List<Long> scheduleIds;

    public List<Long> getScheduleIds() {
        return scheduleIds;
    }

    public void setScheduleIds(List<Long> scheduleIds) {
        this.scheduleIds = scheduleIds;
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
    
    private List<Boolean> holidays;

    public List<Boolean> getHolidays() {
        return holidays;
    }

    public void setHolidays(List<Boolean> holidays) {
        this.holidays = holidays;
    }

}
