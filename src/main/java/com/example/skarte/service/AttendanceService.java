package com.example.skarte.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.skarte.entity.Attendance;
import com.example.skarte.entity.Grade;
import com.example.skarte.entity.Karte;
import com.example.skarte.entity.StudentYear;
import com.example.skarte.form.AttendanceForm;
import com.example.skarte.repository.AttendanceRepository;
import com.example.skarte.repository.GradeRepository;
import com.example.skarte.repository.KarteRepository;
import com.example.skarte.repository.StudentYearRepository;
import com.example.skarte.specification.StudentSpecification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final StudentYearRepository studentYearRepository;
    private final StudentSpecification studentSpecification;

    private final ScheduleService scheduleService;
    private final StudentsYearService studentsYearService;

//    @Autowired
//    AttendanceRepository attendanceRepository;

    // 月ごとのカレンダー
    public List<Calendar> monthCalendar(Long year, Long month) {
        List<Calendar> monthCalendar = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        int nendo = Integer.valueOf(year.toString());
        int tsuki = Integer.valueOf(month.toString());
        if (tsuki >= 3) {
            cal.set(nendo, tsuki, 1);
        } else {
            cal.set(nendo + 1, tsuki, 1);
        }
        int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 0; i < days; i++) {
            Calendar calendar = Calendar.getInstance();
            if (tsuki >= 3) {
                calendar.set(nendo, tsuki, 1 + i);
            } else {
                calendar.set(nendo + 1, tsuki, 1 + i);
            }
            monthCalendar.add(calendar);
        }
        return monthCalendar;
    }

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
     * 生徒ごとの1か月分の出欠リストを取得
     * 
     * @return
     */
    public List<Attendance> studentAttendanceMonth(String studentId, Long year, Long month) {
        List<Attendance> studentAttendanceMonth = new ArrayList<>();
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
            studentAttendanceMonth.add(null);
        }
        List<Attendance> result = attendanceRepository.findAllByStudentId(studentId);
        for (int i = 0; i < result.size(); i++) {
            Calendar cl = Calendar.getInstance();
            cl.setTime(result.get(i).getDate());
//            if (tsuki <= 3) {
//                nendo = nendo + 1;
//            }
            if (cl.get(Calendar.YEAR) == nendo && cl.get(Calendar.MONTH) == tsuki) {
                for (int j = 0; j < days; j++) {
                    if (cl.get(Calendar.DATE) == j + 1) {
                        studentAttendanceMonth.set(j, result.get(i));
                    }
                }
            }
        }
        return studentAttendanceMonth;
    }

    /**
     * 生徒ごとの1か月分の出欠まとめを取得 [0]登校日数,[1]出席数,[2]欠席数,[3]遅刻数,[4]早退数,[5]出停/忌引数
     * 
     * @return
     */
    public List<Integer> studentAttendanceMonthSummary(String studentId, Long year, Long month) {
        List<Integer> studentAttendanceMonthSummary = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            studentAttendanceMonthSummary.add(0);
        }

        int nendo = Integer.valueOf(year.toString());
        int tsuki = Integer.valueOf(month.toString());
        // 年度の調整
        if (tsuki <= 2) {
            nendo = nendo + 1;
        }
        List<Attendance> result = attendanceRepository.findAllByStudentId(studentId);
        List<Attendance> kesseki = new ArrayList<>();
        List<Attendance> chikoku = new ArrayList<>();
        List<Attendance> soutai = new ArrayList<>();
        List<Attendance> syuttei = new ArrayList<>();
        for (int i = 0; i < result.size(); i++) {
            Calendar cl = Calendar.getInstance();
            cl.setTime(result.get(i).getDate());
            if (cl.get(Calendar.YEAR) == nendo && cl.get(Calendar.MONTH) == tsuki) {
                // 欠席
                if (result.get(i).getKiroku() == 1) {
                    kesseki.add(result.get(i));
                }
                // 遅刻
                if (result.get(i).getKiroku() == 2) {
                    chikoku.add(result.get(i));
                }
                // 早退
                if (result.get(i).getKiroku() == 3) {
                    soutai.add(result.get(i));
                }
                // 遅刻/早退
                if (result.get(i).getKiroku() == 4) {
                    chikoku.add(result.get(i));
                    soutai.add(result.get(i));
                }
                // 出停または忌引
                if (result.get(i).getKiroku() == 5 || result.get(i).getKiroku() == 6) {
                    syuttei.add(result.get(i));
                }
            }
        }

        // 0に登校日数をset
        int monthScheduleSize = scheduleService.monthScheduleSize(year, month);
        studentAttendanceMonthSummary.set(0, monthScheduleSize);
        // 1に出席日数(登校日数-(欠席数+出停忌引数))をset
        studentAttendanceMonthSummary.set(1, monthScheduleSize - (kesseki.size() + syuttei.size()));
        studentAttendanceMonthSummary.set(2, kesseki.size());
        studentAttendanceMonthSummary.set(3, chikoku.size());
        studentAttendanceMonthSummary.set(4, soutai.size());
        studentAttendanceMonthSummary.set(5, syuttei.size());
        return studentAttendanceMonthSummary;
    }

    /**
     * 生徒ごとの3年分の出欠まとめを取得 [0]登校日数,[1]出席数,[2]欠席数,[3]遅刻数,[4]早退数,[5]出停/忌引数
     * 
     * @return
     */
    public ArrayList<ArrayList<ArrayList<Integer>>> studentAttendanceSummary(String studentId) {
        ArrayList<ArrayList<ArrayList<Integer>>> studentAttendanceSummary = new ArrayList<>();
        List<StudentYear> resultYear = studentYearRepository.findAllByStudentIdOrderByYearAsc(studentId);
        List<Attendance> resultAttendance = attendanceRepository.findAllByStudentId(studentId);

        // 年
        for (int i = 0; i < resultYear.size(); i++) {
            // お試し
            studentAttendanceSummary.add(new ArrayList<>());

            int toukouSum = 0;
            int syussekiSum = 0;
            int kessekiSum = 0;
            int chikokuSum = 0;
            int soutaiSum = 0;
            int syutteiSum = 0;

//            ArrayList<Integer> yearSummary = new ArrayList<>();
//            int nendo = Integer.valueOf(resultYear.get(i).getYear().toString());
//            int tsuki;

            // 月
//            for (int j = 3; j < 15; j++) {
////            for (int j = 0; j < 12; j++) {
////                if (j <= 2) {
//                if (j >= 12) {
//                    nendo = nendo + 1;                    
//                }
//                if (j >= 11) {
//                    tsuki = j;
//                } else {
//                    tsuki = j-12;
//                }
            for (int j = 0; j < 12; j++) {
                int nendo = Integer.valueOf(resultYear.get(i).getYear().toString());
                int tsuki;
                if (j >= 9) {
                    tsuki = j - 9;
                    nendo = nendo + 1;
                } else {
                    tsuki = j + 3;
                }

                // お試し
                studentAttendanceSummary.get(i).add(new ArrayList<>());
//                ArrayList<Integer> monthSummary = new ArrayList<>();
                for (int l = 0; l < 6; l++) {
//                    monthSummary.add(null);
                    studentAttendanceSummary.get(i).get(j).add(0);
                }
                List<Attendance> kesseki = new ArrayList<>();
                List<Attendance> chikoku = new ArrayList<>();
                List<Attendance> soutai = new ArrayList<>();
                List<Attendance> syuttei = new ArrayList<>();

                for (int k = 0; k < resultAttendance.size(); k++) {
//                    // お試し
//                    studentAttendanceSummary.get(i).get(j).add();

                    Calendar cl = Calendar.getInstance();
                    cl.setTime(resultAttendance.get(k).getDate());
                    if (cl.get(Calendar.YEAR) == nendo && cl.get(Calendar.MONTH) == tsuki) {

                        // 欠席
                        if (resultAttendance.get(k).getKiroku() == 1) {
                            kesseki.add(resultAttendance.get(k));
                        }
                        // 遅刻
                        if (resultAttendance.get(k).getKiroku() == 2) {
                            chikoku.add(resultAttendance.get(k));
                        }
                        // 早退
                        if (resultAttendance.get(k).getKiroku() == 3) {
                            soutai.add(resultAttendance.get(k));
                        }
                        // 遅刻/早退
                        if (resultAttendance.get(k).getKiroku() == 4) {
                            chikoku.add(resultAttendance.get(k));
                            soutai.add(resultAttendance.get(k));
                        }
                        // 出停または忌引
                        if (resultAttendance.get(k).getKiroku() == 5 || resultAttendance.get(k).getKiroku() == 6) {
                            syuttei.add(resultAttendance.get(k));
                        }
                    }
                }

                // 0に登校日数をset
                Long month;
                if (j >= 9) {
                    month = (long) (j - 9);
                } else {
                    month = (long) (j + 3);
                }
                int monthScheduleSize = scheduleService.monthScheduleSize(resultYear.get(i).getYear(), month);
                studentAttendanceSummary.get(i).get(j).set(0, monthScheduleSize);
                // 1に出席日数(登校日数-(欠席数+出停忌引数))をset
                studentAttendanceSummary.get(i).get(j).set(1, monthScheduleSize - (kesseki.size() + syuttei.size()));

                studentAttendanceSummary.get(i).get(j).set(2, kesseki.size());
                studentAttendanceSummary.get(i).get(j).set(3, chikoku.size());
                studentAttendanceSummary.get(i).get(j).set(4, soutai.size());
                studentAttendanceSummary.get(i).get(j).set(5, syuttei.size());

                toukouSum = toukouSum + monthScheduleSize;
                syussekiSum = syussekiSum + monthScheduleSize - (kesseki.size() + syuttei.size());
                kessekiSum = kessekiSum + kesseki.size();
                chikokuSum = chikokuSum + chikoku.size();
                soutaiSum = soutaiSum + soutai.size();
                syutteiSum = syutteiSum + syuttei.size();

//                monthSummary.set(1, kesseki.size());
//                monthSummary.set(2, chikoku.size());
//                monthSummary.set(3, soutai.size());
//                monthSummary.set(4, syuttei.size());
//                yearSummary.add(monthSummary.get(j));
            } // 12回の繰り返し終了

            // 年度の合計
            ArrayList<Integer> monthSummary = new ArrayList<>();
            for (int l = 0; l < 6; l++) {
                monthSummary.add(0);
            }
            monthSummary.set(0, toukouSum);
            monthSummary.set(1, syussekiSum);
            monthSummary.set(2, kessekiSum);
            monthSummary.set(3, chikokuSum);
            monthSummary.set(4, soutaiSum);
            monthSummary.set(5, syutteiSum);
            studentAttendanceSummary.get(i).add(monthSummary);
//            studentAttendanceSummary.add(yearSummary.get(i));
        } // （最大）3年分の繰り返し終了

        return studentAttendanceSummary;
    }

    /**
     * 生徒ごとの3年分合計を取得 [0]登校日数,[1]出席数,[2]欠席数,[3]遅刻数,[4]早退数,[5]出停/忌引数
     * 
     * @return
     */
    public List<Integer> studentAttendanceTotal(String studentId) {
        List<Integer> studentAttendanceTotal = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            studentAttendanceTotal.add(0);
        }
        int toukouTotal = 0;
        int syussekiTotal = 0;
        int kessekiTotal = 0;
        int chikokuTotal = 0;
        int soutaiTotal = 0;
        int syutteiTotal = 0;
        List<Attendance> result = attendanceRepository.findAllByStudentId(studentId);
        for (int j = 0; j < result.size(); j++) {
            // 欠席
            if (result.get(j).getKiroku() == 1) {
                kessekiTotal = kessekiTotal + 1;
            }
            // 遅刻
            if (result.get(j).getKiroku() == 2) {
                chikokuTotal = chikokuTotal + 1;
            }
            // 早退
            if (result.get(j).getKiroku() == 3) {
                soutaiTotal = soutaiTotal + 1;
            }
            // 遅刻/早退
            if (result.get(j).getKiroku() == 4) {
                chikokuTotal = chikokuTotal + 1;
                soutaiTotal = soutaiTotal + 1;
            }
            // 出停または忌引
            if (result.get(j).getKiroku() == 5 || result.get(j).getKiroku() == 6) {
                syutteiTotal = syutteiTotal + 1;
            }
        }

        List<StudentYear> resultYear = studentsYearService.findAllByStudentId(studentId);
        for (int k = 0; k < resultYear.size(); k++) {
            int yearScheduleCount = scheduleService.yearScheduleCount(resultYear.get(k).getYear());
            toukouTotal = toukouTotal + yearScheduleCount;
        }
        syussekiTotal = toukouTotal - (kessekiTotal + syutteiTotal);
        
        studentAttendanceTotal.set(0, toukouTotal);
        studentAttendanceTotal.set(1, syussekiTotal);
        studentAttendanceTotal.set(2, kessekiTotal);
        studentAttendanceTotal.set(3, chikokuTotal);
        studentAttendanceTotal.set(4, soutaiTotal);
        studentAttendanceTotal.set(5, syutteiTotal);

        return studentAttendanceTotal;
    }

    /**
     * クラス（学年）全員の1か月分の出欠リストを取得
     * 
     * @return
     */
    public ArrayList<ArrayList<Attendance>> attendanceMonth(Long year, Long nen, Long kumi, Long month) {
        ArrayList<ArrayList<Attendance>> attendanceMonth = new ArrayList<>();

        // 年・月
        Calendar cal = Calendar.getInstance();
        int nendo = Integer.valueOf(year.toString());
        int tsuki = Integer.valueOf(month.toString());
        // 年度の調整
        if (tsuki <= 2) {
            nendo = nendo + 1;
        }
        cal.set(nendo, tsuki, 1);
        int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        // 年度、年、組からクラスの生徒リストを取得
        List<StudentYear> result;
        if (kumi == 0) {
            result = studentYearRepository.findAll(
                    Specification.where(studentSpecification.year(year)).and(studentSpecification.nen(nen)),
                    Sort.by(Sort.Direction.ASC, "kumi", "ban"));
        } else {
            result = studentYearRepository.findAll(Specification.where(studentSpecification.year(year))
                    .and(studentSpecification.nen(nen)).and(studentSpecification.kumi(kumi)),
                    Sort.by(Sort.Direction.ASC, "ban"));
        }
        for (int i = 0; i < result.size(); i++) {
            // 「生徒ごとの月の出欠リスト」
            ArrayList<Attendance> studentAttendanceMonth = new ArrayList<>();
            for (int j = 0; j < days; j++) {
                studentAttendanceMonth.add(null);
            }
            List<Attendance> resultAttendance = attendanceRepository.findAllByStudentId(result.get(i).getStudentId());
            for (int k = 0; k < resultAttendance.size(); k++) {
                Calendar cl = Calendar.getInstance();
                cl.setTime(resultAttendance.get(k).getDate());
                if (cl.get(Calendar.YEAR) == nendo && cl.get(Calendar.MONTH) == tsuki) {
                    for (int l = 0; l < days; l++) {
                        if (cl.get(Calendar.DATE) == l + 1) {
                            studentAttendanceMonth.set(l, resultAttendance.get(k));
                        }
                    }
                }
            }
            // 配列に追加
            attendanceMonth.add(studentAttendanceMonth);
        }
        return attendanceMonth;
    }

    /**
     * クラス（学年）全員の1か月分の出欠まとめを取得 [0]登校日数,[1]出席数,[2]欠席数,[3]遅刻数,[4]早退数,[5]出停/忌引数
     * 
     * @return
     */
    public ArrayList<ArrayList<Integer>> attendanceMonthSummary(Long year, Long nen, Long kumi, Long month) {
        ArrayList<ArrayList<Integer>> attendanceMonthSummary = new ArrayList<>();
//        for (int i = 0; i < 5; i++) {
//            attendanceMonthSummary.add(null);
//        }
        int nendo = Integer.valueOf(year.toString());
        int tsuki = Integer.valueOf(month.toString());
        // 年度の調整
        if (tsuki <= 2) {
            nendo = nendo + 1;
        }
        // 年度、年、組からクラスの生徒リストを取得
        List<StudentYear> result;
        if (kumi == 0) {
            result = studentYearRepository.findAll(
                    Specification.where(studentSpecification.year(year)).and(studentSpecification.nen(nen)),
                    Sort.by(Sort.Direction.ASC, "kumi", "ban"));
        } else {
            result = studentYearRepository.findAll(Specification.where(studentSpecification.year(year))
                    .and(studentSpecification.nen(nen)).and(studentSpecification.kumi(kumi)),
                    Sort.by(Sort.Direction.ASC, "ban"));
        }
        for (int i = 0; i < result.size(); i++) {
            // 「生徒ごとの月の出欠リスト」
            ArrayList<Integer> studentAttendanceMonthSummary = new ArrayList<>();
            for (int k = 0; k < 6; k++) {
                studentAttendanceMonthSummary.add(0);
            }
            List<Attendance> resultAttendance = attendanceRepository.findAllByStudentId(result.get(i).getStudentId());
            List<Attendance> kesseki = new ArrayList<>();
            List<Attendance> chikoku = new ArrayList<>();
            List<Attendance> soutai = new ArrayList<>();
            List<Attendance> syuttei = new ArrayList<>();
            for (int j = 0; j < resultAttendance.size(); j++) {
                Calendar cl = Calendar.getInstance();
                cl.setTime(resultAttendance.get(j).getDate());
                if (cl.get(Calendar.YEAR) == nendo && cl.get(Calendar.MONTH) == tsuki) {
                    // 欠席
                    if (resultAttendance.get(j).getKiroku() == 1) {
                        kesseki.add(resultAttendance.get(j));
                    }
                    // 遅刻
                    if (resultAttendance.get(j).getKiroku() == 2) {
                        chikoku.add(resultAttendance.get(j));
                    }
                    // 早退
                    if (resultAttendance.get(j).getKiroku() == 3) {
                        soutai.add(resultAttendance.get(j));
                    }
                    // 遅刻/早退
                    if (resultAttendance.get(j).getKiroku() == 4) {
                        chikoku.add(resultAttendance.get(j));
                        soutai.add(resultAttendance.get(j));
                    }
                    // 出停または忌引
                    if (resultAttendance.get(j).getKiroku() == 5 || resultAttendance.get(j).getKiroku() == 6) {
                        syuttei.add(resultAttendance.get(j));
                    }
                }

                studentAttendanceMonthSummary.set(2, kesseki.size());
                studentAttendanceMonthSummary.set(3, chikoku.size());
                studentAttendanceMonthSummary.set(4, soutai.size());
                studentAttendanceMonthSummary.set(5, syuttei.size());
            }
            // 0に登校日数をset
            int monthScheduleSize = scheduleService.monthScheduleSize(year, month);
            studentAttendanceMonthSummary.set(0, monthScheduleSize);
            // 1に出席日数(登校日数-(欠席数+出停忌引数))をset
            studentAttendanceMonthSummary.set(1, monthScheduleSize - (kesseki.size() + syuttei.size()));

            attendanceMonthSummary.add(studentAttendanceMonthSummary);
        }
        return attendanceMonthSummary;
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
     * 出欠一括更新
     * 
     * @return
     */
    public void update(String userId, AttendanceForm attendanceForm) {
        List<Long> attendanceIds = attendanceForm.getAttendanceIds();
        List<String> studentIds = attendanceForm.getStudentIds();
        List<Date> dates = attendanceForm.getDates();
        List<Integer> kirokus = attendanceForm.getKirokus();

        for (int i = 0; i < studentIds.size(); i++) {
            // 新規登録
            if ((attendanceIds.size() != 0 && kirokus.size() != 0 && attendanceIds.get(i) == null
                    && kirokus.get(i) != null) || (attendanceIds.size() == 0 && kirokus.size() != 0)) {
                Attendance attendance = new Attendance();
                attendance.setStudentId(studentIds.get(i));
                attendance.setDate(dates.get(i));
                attendance.setKiroku(kirokus.get(i));
                attendance.setCreatedBy(userId);
                attendance.setUpdatedBy(userId);
                attendanceRepository.save(attendance);
            }
            // 更新
            if (attendanceIds.size() != 0 && kirokus.size() != 0 && attendanceIds.get(i) != null
                    && kirokus.get(i) != null) {
                Attendance updateAttendance = attendanceRepository.findById(attendanceIds.get(i)).orElseThrow();
                updateAttendance.setKiroku(kirokus.get(i));
                updateAttendance.setUpdatedBy(userId);
                attendanceRepository.save(updateAttendance);
            }
            // 削除
            if ((attendanceIds.size() != 0 && kirokus.size() != 0 && attendanceIds.get(i) != null
                    && kirokus.get(i) == null) || (attendanceIds.size() != 0 && kirokus.size() == 0)) {
                Attendance deleteAttendance = attendanceRepository.findById(attendanceIds.get(i)).orElseThrow();
                attendanceRepository.delete(deleteAttendance);
            }
        }

    }

//        for (int i = 0; i < studentIds.size(); i++) {
//            if (attendanceIds.size() != 0 && kirokus.size() != 0) { // リストが１つだけのとき用
////            // 新規登録
//                if (attendanceIds.get(i) == null && kirokus.get(i) != null) {
//                    Attendance attendance = new Attendance();
//                    attendance.setStudentId(studentIds.get(i));
//                    attendance.setDate(dates.get(i));
//                    attendance.setKiroku(kirokus.get(i));
//                    attendance.setCreatedBy(userId);
//                    attendance.setUpdatedBy(userId);
//                    attendanceRepository.save(attendance);
//                }
//                // 更新
//                if (attendanceIds.get(i) != null && kirokus.get(i) != null) {
//                    Attendance updateAttendance = attendanceRepository.findById(attendanceIds.get(i)).orElseThrow();
//                    updateAttendance.setKiroku(kirokus.get(i));
//                    updateAttendance.setUpdatedBy(userId);
//                    attendanceRepository.save(updateAttendance);
//                }
//                // 削除
//                if (attendanceIds.get(i) != null && kirokus.get(i) == null) {
//                    Attendance deleteAttendance = attendanceRepository.findById(attendanceIds.get(i)).orElseThrow();
//                    attendanceRepository.delete(deleteAttendance);
//                }
//            } // リストが１つだけのとき用
//
//            if (attendanceIds.size() == 0 && kirokus.size() != 0) { // リストが１つだけのとき用
//                // 新規登録
//                Attendance attendance = new Attendance();
//                attendance.setStudentId(studentIds.get(i));
//                attendance.setDate(dates.get(i));
//                attendance.setKiroku(kirokus.get(i));
//                attendance.setCreatedBy(userId);
//                attendance.setUpdatedBy(userId);
//                attendanceRepository.save(attendance);
//            } // リストが１つだけのとき用
//
//            if (attendanceIds.size() != 0 && kirokus.size() == 0) { // リストが１つだけのとき用
//                Attendance deleteAttendance = attendanceRepository.findById(attendanceIds.get(i)).orElseThrow();
//                attendanceRepository.delete(deleteAttendance);
//            } // リストが１つだけのとき用
//
//        }
//
//    }

//    /**
//     * 出欠更新
//     * 
//     * @param attendance
//     * @return
//     */
//    public Attendance updateAttendance(Long attendanceId, String studentId, Attendance attendance) {
//        attendance.setStudentId(attendance.getStudentId());
//        Attendance targetAttendance = attendanceRepository.findById(attendanceId).orElseThrow();
//        targetAttendance.setDate(attendance.getDate());
//        targetAttendance.setChikoku(attendance.getChikoku());
//        targetAttendance.setSoutai(attendance.getSoutai());
//        targetAttendance.setKesseki(attendance.getKesseki());
//        targetAttendance.setSyuttei(attendance.getSyuttei());
//        targetAttendance.setKibiki(attendance.getKibiki());
//        targetAttendance.setUpdatedBy(attendance.getUpdatedBy());
//        attendanceRepository.save(targetAttendance);
//        return targetAttendance;
//    }

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
