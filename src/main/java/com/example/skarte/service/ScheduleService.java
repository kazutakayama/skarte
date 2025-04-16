package com.example.skarte.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.skarte.entity.Attendance;
import com.example.skarte.entity.Schedule;
import com.example.skarte.form.ScheduleForm;
import com.example.skarte.form.StudentForm;
import com.example.skarte.repository.ScheduleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    // 年ごとのカレンダー
    public ArrayList<ArrayList<Calendar>> yearCalendar(Long year) {
        ArrayList<ArrayList<Calendar>> yearCalendar = new ArrayList<>();

        Calendar cal = Calendar.getInstance();
        int nendo = Integer.valueOf(year.toString());
        for (int i = 0; i < 12; i++) {
            ArrayList<Calendar> monthCalendar = new ArrayList<>();
            if (i <= 8) {
                cal.set(nendo, i + 3, 1);
            } else {
                cal.set(nendo + 1, i - 9, 1);
            }
            int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
            for (int j = 0; j < days; j++) {
                Calendar calendar = Calendar.getInstance();
                if (i <= 8) {
                    calendar.set(nendo, i + 3, j + 1);
                } else {
                    calendar.set(nendo + 1, i - 9, j + 1);
                }
                monthCalendar.add(calendar);
            }
            yearCalendar.add(monthCalendar);
        }
        return yearCalendar;
    }

    // すべてのスケジュールを取得
    public List<Schedule> findAll() {
        return scheduleRepository.findByOrderByUpdatedAtDesc();
    }

    // 年度・月で検索し、１か月のスケジュールリストを取得
    public List<Schedule> monthSchedule(Long year, Long month) {
        List<Schedule> monthSchedule = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        int nendo = Integer.valueOf(year.toString());
        int tsuki = Integer.valueOf(month.toString());
        // 年度の調整
        if (tsuki <= 2) {
            nendo = nendo + 1;
        }
        cal.set(nendo, tsuki, 1);
        int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int i = 0; i < days; i++) {
            monthSchedule.add(null);
        }
        List<Schedule> result = scheduleRepository.findByOrderByUpdatedAtDesc();
        for (int i = 0; i < result.size(); i++) {
            Calendar cl = Calendar.getInstance();
            cl.setTime(result.get(i).getDate());
            if (cl.get(Calendar.YEAR) == nendo && cl.get(Calendar.MONTH) == tsuki) {
                for (int j = 0; j < days; j++) {
                    if (cl.get(Calendar.DATE) == j + 1) {
                        monthSchedule.set(j, result.get(i));
                    }
                }
            }
        }
        return monthSchedule;
    }

    // 年度・月でスケジュールリストを取得し、月ごとの登校日数(holiday == false)を数える
    public int monthScheduleSize(Long year, Long month) {
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


    // 年度で検索し、１年のスケジュールリストを取得
    public ArrayList<ArrayList<Schedule>> yearSchedule(Long year) {
        ArrayList<ArrayList<Schedule>> yearSchedule = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            ArrayList<Schedule> monthSchedule = new ArrayList<>();
            Calendar cal = Calendar.getInstance();
            int nendo = Integer.valueOf(year.toString());
            if (i <= 8) {
                cal.set(nendo, i + 3, 1);
            } else {
                nendo = nendo + 1;
                cal.set(nendo, i - 9, 1);
            }
            int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
            for (int l = 0; l < days; l++) {
                monthSchedule.add(null);
            }
            // すべてのスケジュールから年度・月(i)に合致するものを探す
            List<Schedule> result = scheduleRepository.findByOrderByUpdatedAtDesc();
            for (int m = 0; m < result.size(); m++) {
                Calendar cl = Calendar.getInstance();
                cl.setTime(result.get(m).getDate());
                // i=0～8 4月から12月の場合
                if (i <= 8 && cl.get(Calendar.YEAR) == nendo && cl.get(Calendar.MONTH) == i + 3) {
                    for (int j = 0; j < days; j++) {
                        if (cl.get(Calendar.DATE) == j + 1) {
                            monthSchedule.set(j, result.get(m));
                        }
                    }
                }
                // i=9～11 1月から3月の場合
                if (i >= 9 && cl.get(Calendar.YEAR) == nendo && cl.get(Calendar.MONTH) == i - 9) {
                    for (int j = 0; j < days; j++) {
                        if (cl.get(Calendar.DATE) == j + 1) {
                            monthSchedule.set(j, result.get(m));
                        }
                    }
                }
            }
            yearSchedule.add(monthSchedule);
        }
        return yearSchedule;
    }

    // 年度でスケジュールリストを取得し、月ごとの登校日数(holiday == false)を数え、年度のリストをつくる
    public List<Integer> yearScheduleSize(Long year) {
        ArrayList<ArrayList<Schedule>> yearSchedule = yearSchedule(year);
        List<Integer> yearScheduleSize = new ArrayList<>();
//        if (yearSchedule != null) {
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
//        }
        return yearScheduleSize;

    }

    // 年度の合計登校日数をカウント
    public int yearScheduleCount(Long year) {
        List<Schedule> count = new ArrayList<>();
        int nendo = Integer.valueOf(year.toString());
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.set(nendo, 3, 1, 00, 00, 00);
        cal1.set(Calendar.MILLISECOND, 000);
        cal2.set(nendo + 1, 2, 31, 23, 59, 59);
        cal2.set(Calendar.MILLISECOND, 999);
        List<Schedule> result = scheduleRepository.findByOrderByUpdatedAtDesc();
        for (int i = 0; i < result.size(); i++) {
            if (result.get(i).isHoliday() == false) {
                Calendar cl = Calendar.getInstance();
                cl.setTime(result.get(i).getDate());
                if ((cl.compareTo(cal1) == 1 || cl.compareTo(cal1) == 0)
                        && (cl.compareTo(cal2) == -1 || cl.compareTo(cal2) == 0)) {
                    count.add(result.get(i));
                }
            }
        }
        int yearScheduleCount = count.size();
        return yearScheduleCount;
    }

    //

    // スケジュール1件登録
    public void add(String userId, ScheduleForm scheduleForm) {
        if (scheduleForm.getHoliday() == true) {
            Schedule schedule = new Schedule();
            schedule.setDate(scheduleForm.getDate());
            schedule.setHoliday(scheduleForm.getHoliday());
            schedule.setCreatedBy(userId);
            schedule.setUpdatedBy(userId);
            scheduleRepository.save(schedule);
        }
    }

    // ※初回 １年分のスケジュールを自動作成 土日はholiday==true
    public void newSchedule(Long year, String userId) {
        int yearScheduleCount = yearScheduleCount(year);
        if (yearScheduleCount == 0) {
            for (int i = 0; i < 12; i++) {
                Calendar cal = Calendar.getInstance();
                int nendo = Integer.valueOf(year.toString());
                if (i <= 8) {
                    cal.set(nendo, i + 3, 1);
                } else {
                    nendo = nendo + 1;
                    cal.set(nendo, i - 9, 1);
                }
                int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
                for (int j = 0; j < days; j++) {
                    cal.set(Calendar.DAY_OF_MONTH, j + 1);
                    // 1:日曜または7:土曜のときholiday==true
                    if (cal.get(Calendar.DAY_OF_WEEK) == 1 || cal.get(Calendar.DAY_OF_WEEK) == 7) {
                        Date date = new Date();
                        date = cal.getTime();
                        Schedule schedule = new Schedule();
                        schedule.setDate(date);
                        schedule.setHoliday(true);
                        schedule.setCreatedBy(userId);
                        schedule.setUpdatedBy(userId);
                        scheduleRepository.save(schedule);
                    } else { // 平日のときholiday==false
                        Date date = new Date();
                        date = cal.getTime();
                        Schedule schedule = new Schedule();
                        schedule.setDate(date);
                        schedule.setHoliday(false);
                        schedule.setCreatedBy(userId);
                        schedule.setUpdatedBy(userId);
                        scheduleRepository.save(schedule);
                    }
                }
            }
        }

    }

    // スケジュール一括更新
    public void update(String userId, ScheduleForm scheduleForm) {
        List<Long> scheduleIds = scheduleForm.getScheduleIds();
//        List<Date> dates = scheduleForm.getDates();
        List<Boolean> holidays = scheduleForm.getHolidays();
        for (int i = 0; i < scheduleIds.size(); i++) {
            Schedule updateSchedule = scheduleRepository.findById(scheduleIds.get(i)).orElseThrow();
            updateSchedule.setHoliday(holidays.get(i));
            updateSchedule.setUpdatedBy(userId);
            scheduleRepository.save(updateSchedule);
//            if (holidays != null) {
//            // 新規登録
//            if ((holidays.size() != 0 && scheduleIds.get(i) == null && holidays.get(i) != null)) {
//                Schedule schedule = new Schedule();
//                schedule.setDate(dates.get(i));
//                schedule.setHoliday(holidays.get(i));
//                schedule.setCreatedBy(userId);
//                schedule.setUpdatedBy(userId);
//                scheduleRepository.save(schedule);
//            }
//                if ((holidays.size() != 0 && scheduleIds.get(i) == null && holidays.get(i) != 1)) {
//                    Schedule schedule = new Schedule();
//                    schedule.setDate(dates.get(i));
//                    schedule.setHoliday(false);
//                    schedule.setCreatedBy(userId);
//                    schedule.setUpdatedBy(userId);
//                    scheduleRepository.save(schedule);
//                }

//                // 削除
//                if ((holidays.size() != 0 && scheduleIds.get(i) != null)) {
//                    Schedule deleteSchedule = scheduleRepository.findById(scheduleIds.get(i)).orElseThrow();
//                    scheduleRepository.delete(deleteSchedule);
//                }

//            }
        }
    }

}
