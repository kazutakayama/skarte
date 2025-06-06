package com.example.skarte.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.skarte.entity.Schedule;
import com.example.skarte.form.ScheduleForm;
import com.example.skarte.repository.ScheduleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    /** すべてのスケジュールを取得 */
    public List<Schedule> findAll() {
        return scheduleRepository.findByOrderByUpdatedAtDesc();
    }

    /** 年度・月で検索し、１か月のスケジュールリストを取得 */
    public List<Schedule> monthSchedule(int year, int month) {
        List<Schedule> monthSchedule = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        // 年度の調整
        if (month <= 2) {
            year = year + 1;
        }
        cal.set(year, month, 1);
        int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 0; i < days; i++) {
            monthSchedule.add(null);
        }
        List<Schedule> result = scheduleRepository.findByOrderByUpdatedAtDesc();
        for (int i = 0; i < result.size(); i++) {
            LocalDate localDate = result.get(i).getDate();
            if (localDate.getYear() == year && localDate.getMonthValue() == month + 1) {
                for (int j = 0; j < days; j++) {
                    if (localDate.getDayOfMonth() == j + 1) {
                        monthSchedule.set(j, result.get(i));
                    }
                }
            }
        }
        return monthSchedule;
    }

    /** 年度・月でスケジュールリストを取得し、月ごとの登校日数(holiday == false)を数える */
    public int monthScheduleSize(int year, int month) {
        int monthScheduleSize = 0;
        List<Schedule> monthSchedule = monthSchedule(year, month);
        for (int i = 0; i < monthSchedule.size(); i++) {
            if (monthSchedule.get(i) != null) { // nullチェック
                if (monthSchedule.get(i).isHoliday() == false) {
                    monthScheduleSize = monthScheduleSize + 1;
                }
            }
        }
        return monthScheduleSize;
    }

    /** 年度で検索し、１年のスケジュールリストを取得 */
    public ArrayList<ArrayList<Schedule>> yearSchedule(int year) {
        ArrayList<ArrayList<Schedule>> yearSchedule = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            ArrayList<Schedule> monthSchedule = new ArrayList<>();
            Calendar cal = Calendar.getInstance();
            if (i <= 8) {
                cal.set(year, i + 3, 1);
            } else {
                year = year + 1;
                cal.set(year, i - 9, 1);
            }
            // 月の日数をカウント
            int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
            for (int l = 0; l < days; l++) {
                monthSchedule.add(null);
            }
            // すべてのスケジュールから年度・月(i)に合致するものを探す
            List<Schedule> result = scheduleRepository.findByOrderByUpdatedAtDesc();
            for (int m = 0; m < result.size(); m++) {
                LocalDate localDate = result.get(m).getDate();
                // i=0～8 4月から12月の場合
                if (i <= 8 && localDate.getYear() == year && localDate.getMonthValue() == i + 4) {
                    for (int j = 0; j < days; j++) {
                        if (localDate.getDayOfMonth() == j + 1) {
                            monthSchedule.set(j, result.get(m));
                        }
                    }
                }
                // i=9～11 1月から3月の場合
                if (i >= 9 && localDate.getYear() == year && localDate.getMonthValue() == i - 8) {
                    for (int j = 0; j < days; j++) {
                        if (localDate.getDayOfMonth() == j + 1) {
                            monthSchedule.set(j, result.get(m));
                        }
                    }
                }
            }
            yearSchedule.add(monthSchedule);
        }
        return yearSchedule;
    }

    /** 年度でスケジュールリストを取得し、月ごとの登校日数(holiday == false)を数え、年度のリストをつくる */
    public List<Integer> yearScheduleSize(int year) {
        ArrayList<ArrayList<Schedule>> yearSchedule = yearSchedule(year);
        List<Integer> yearScheduleSize = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            int monthScheduleSize = 0;
            for (int j = 0; j < yearSchedule.get(i).size(); j++) {
                if (yearSchedule.get(i).get(j) != null) { // nullチェック
                    if (yearSchedule.get(i).get(j).isHoliday() == false) {
                        monthScheduleSize = monthScheduleSize + 1;
                    }
                }
            }
            yearScheduleSize.add(monthScheduleSize);
        }
        return yearScheduleSize;

    }

    /** 年度の合計登校日数をカウント */
    public int yearScheduleCount(int year) {
        List<Schedule> count = new ArrayList<>();
        LocalDate ld1 = LocalDate.of(year, 4, 1);
        LocalDate ld2 = LocalDate.of(year + 1, 3, 31);
        List<Schedule> result = scheduleRepository.findByOrderByUpdatedAtDesc();
        for (int i = 0; i < result.size(); i++) {
            if (result.get(i).isHoliday() == false) {
                LocalDate ld = result.get(i).getDate();
                // 4月1日から翌年の3月31日までの間の要素を取り出す
                if (ld.compareTo(ld1) >= 0 && ld.compareTo(ld2) <= 0) {
                    count.add(result.get(i));
                }
            }
        }
        int yearScheduleCount = count.size();
        return yearScheduleCount;
    }

    /** ※初回 １年分のスケジュールを自動作成 土日はholiday==true */
    public void newSchedule(int year, String userId) {
        int yearScheduleCount = yearScheduleCount(year);
        if (yearScheduleCount == 0) {
            for (int i = 0; i < 12; i++) {
                Calendar cal = Calendar.getInstance();
                if (i <= 8) {
                    cal.set(year, i + 3, 1);
                } else {
                    year = year + 1;
                    cal.set(year, i - 9, 1);
                }
                int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
                for (int j = 0; j < days; j++) {
                    cal.set(Calendar.DAY_OF_MONTH, j + 1);
                    // CalendarをLocalDateに変換
                    Instant instant = cal.toInstant();
                    ZoneId zone = ZoneId.systemDefault();
                    LocalDateTime ldt = LocalDateTime.ofInstant(instant, zone);
                    LocalDate localDate = LocalDate.of(ldt.getYear(), ldt.getMonth(), ldt.getDayOfMonth());
                    // 1:日曜または7:土曜のときholiday==true
                    if (cal.get(Calendar.DAY_OF_WEEK) == 1 || cal.get(Calendar.DAY_OF_WEEK) == 7) {
                        Schedule schedule = new Schedule();
                        schedule.setDate(localDate);
                        schedule.setHoliday(true);
                        schedule.setCreatedBy(userId);
                        schedule.setUpdatedBy(userId);
                        scheduleRepository.save(schedule);
                    } else { // 平日のときholiday==false
                        Schedule schedule = new Schedule();
                        schedule.setDate(localDate);
                        schedule.setHoliday(false);
                        schedule.setCreatedBy(userId);
                        schedule.setUpdatedBy(userId);
                        scheduleRepository.save(schedule);
                    }
                }
            }
        }
    }

    /** スケジュール一括更新 */
    public void update(String userId, ScheduleForm scheduleForm) {
        List<Long> scheduleIds = scheduleForm.getScheduleIds();
        List<Boolean> holidays = scheduleForm.getHolidays();
        for (int i = 0; i < scheduleIds.size(); i++) {
            Schedule updateSchedule = scheduleRepository.findById(scheduleIds.get(i)).orElseThrow();
            updateSchedule.setHoliday(holidays.get(i));
            updateSchedule.setUpdatedBy(userId);
            scheduleRepository.save(updateSchedule);
        }
    }

}
