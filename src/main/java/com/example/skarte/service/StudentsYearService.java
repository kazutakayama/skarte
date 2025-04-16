package com.example.skarte.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.skarte.entity.Attendance;
import com.example.skarte.entity.Grade;
import com.example.skarte.entity.Student;
import com.example.skarte.entity.StudentYear;
import com.example.skarte.form.StudentYearForm;
import com.example.skarte.repository.AttendanceRepository;
import com.example.skarte.repository.GradeRepository;
import com.example.skarte.repository.StudentRepository;
import com.example.skarte.repository.StudentYearRepository;
import com.example.skarte.specification.StudentSpecification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentsYearService {

    private final StudentYearRepository studentYearRepository;
    private final StudentRepository studentRepository;
    private final AttendanceRepository attendanceRepository;
    private final GradeRepository gradeRepository;
    private final StudentSpecification studentSpecification;

    /**
     * クラス全取得
     * 
     * @return
     */
    public List<StudentYear> findAll() {
//        return studentYearRepository.findByDeletedFalseOrderByUpdatedAtDesc();
        return studentYearRepository.findByOrderByUpdatedAtDesc();
    }

    /**
     * クラス1件取得
     * 
     * @param id
     * @return
     */
    public StudentYear findById(Long id) {
        return studentYearRepository.findById(id).orElseThrow();
    }

    /**
     * 生徒IDでリストを取得
     * 
     * @return
     */
    public List<StudentYear> findAllByStudentId(String studentId) {
        return studentYearRepository.findAllByStudentIdOrderByYearAsc(studentId);
    }

    /**
     * 生徒IDでリストを取得（リスト）
     * 
     * @return
     */
    public List<StudentYear> classList(String studentId) {
        List<StudentYear> classList = new ArrayList<StudentYear>();
        for (int i = 0; i < 3; i++) {
            classList.add(null);
        }
        List<StudentYear> result = studentYearRepository.findAllByStudentIdOrderByYearAsc(studentId);

        for (int i = 0; i < result.size(); i++) {
            if (result.get(i).getNen() == 1) {
                classList.set(0, result.get(i));
            }
            if (result.get(i).getNen() == 2) {
                classList.set(1, result.get(i));
            }
            if (result.get(i).getNen() == 3) {
                classList.set(2, result.get(i));
            }
//            classList.add(result.get(i));
        }
        return classList;
    }

//    /**
//     * 生徒IDでリストを取得（配列）
//     * 
//     * @return
//     */
//    public List<StudentYear> classList(String studentId) {
//        StudentYear[] array =new StudentYear[3];
//
//        List<StudentYear> result = studentYearRepository.findAllByStudentIdOrderByYearAsc(studentId);
//
//        for (int i = 0; i < result.size(); i++) {
//            if (result.get(i).getNen() == 1) {
//                array[0] = result.get(i);
//            }
//            if (result.get(i).getNen() == 2) {
//                array[1] = result.get(i);
//            }
//            if (result.get(i).getNen() == 3) {
//                array[2] = result.get(i);
//            }
//        }
//        List<StudentYear> classList = Arrays.asList(array);
//        return classList;
//    }

    // クラス検索
    public List<StudentYear> search(Long year, Long nen, Long kumi) {
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
        return result;
    }

    /**
     * クラス追加（旧）
     * 
     * @param studentYear
     * @return
     */
    public void addClass(String userId, StudentYear studentYear) {
        studentYear.setCreatedBy(userId);
        studentYear.setUpdatedBy(userId);
        studentYearRepository.save(studentYear);
    }

    // クラスに登録できる候補の生徒のリストを取得
    public List<Student> studentsOption(Long year, Long nen) {
        // 1学年は1年分、2学年は2年分、3学年は3年分の生徒番号を一旦すべて取得
        List<Student> studentsOption = new ArrayList<>();
        String ichi = String.valueOf(year);
        String ni = String.valueOf(year - 1);
        String san = String.valueOf(year - 2);
        if (nen == 1) {
            studentsOption = studentRepository.findAll(Specification.where(studentSpecification.year(ichi)));
        }
        if (nen == 2) {
            studentsOption = studentRepository
                    .findAll(Specification.where(studentSpecification.year(ichi)).or(studentSpecification.year(ni)));
        }
        if (nen == 3) {
            studentsOption = studentRepository.findAll(Specification.where(studentSpecification.year(ichi))
                    .or(studentSpecification.year(ni)).or(studentSpecification.year(san)));
        }
        // 年度にすでに登録済みの生徒を取得する
        List<StudentYear> resultYear = studentYearRepository
                .findAll(Specification.where(studentSpecification.year(year)));
        List<Student> resultStudents = new ArrayList<>();
        for (int i = 0; i < resultYear.size(); i++) {
            Student student = studentRepository.findById(resultYear.get(i).getStudentId()).orElseThrow();
            resultStudents.add(student);
        }
        // リストの差分を取得する
        studentsOption.removeAll(resultStudents);
        // 生徒番号昇順で並び替え
        studentsOption.sort(Comparator.comparing(Student::getStudentId));
        return studentsOption;
    }

    /**
     * クラス追加
     * 
     * @param studentYear
     * @return
     */
    public void add(String userId, StudentYearForm studentYearForm, Long year, Long nen, Long kumi) {
        // クラス在籍生徒の人数を数え、一番最後の番号をセットする
        List<StudentYear> result = studentYearRepository.findAll(Specification.where(studentSpecification.year(year))
                .and(studentSpecification.nen(nen)).and(studentSpecification.kumi(kumi)),
                Sort.by(Sort.Direction.ASC, "ban"));
        Long count = (long) result.size();
        StudentYear studentYear = new StudentYear();
        studentYear.setStudentId(studentYearForm.getStudentId());
        studentYear.setYear(year);
        studentYear.setNen(nen);
        studentYear.setKumi(kumi);
        studentYear.setBan(count + 1);
        studentYear.setCreatedBy(userId);
        studentYear.setUpdatedBy(userId);
        studentYearRepository.save(studentYear);
    }

    /**
     * クラス一括追加
     * 
     * @param studentYear
     * @return
     */
    public void create(String userId, StudentYearForm studentYearForm, Long year, Long nen, Long kumi) {
        List<String> studentIds = studentYearForm.getStudentIds();
        if (studentIds != null) {
            // 一括追加生徒を番号1で一時的に登録する
            for (int i = 0; i < studentIds.size(); i++) {
                StudentYear studentYear = new StudentYear();
                studentYear.setStudentId(studentIds.get(i));
                studentYear.setYear(year);
                studentYear.setNen(nen);
                studentYear.setKumi(kumi);
                studentYear.setBan((long) 1);
                studentYear.setCreatedBy(userId);
                studentYear.setUpdatedBy(userId);
                studentYearRepository.save(studentYear);
            }
        }
    }

//    /**
//     * クラス一括追加
//     * 
//     * @param studentYear
//     * @return
//     */
//    public void addClassMulti(String userId, StudentYearForm studentYearForm) {
//        List<String> studentIds = studentYearForm.getStudentIds();
//        List<Long> years = studentYearForm.getYears();
//        List<Long> nens = studentYearForm.getNens();
//        List<Long> kumis = studentYearForm.getKumis();
//        List<Long> bans = studentYearForm.getBans();
//        for (int i = 0; i < years.size(); i++) {
//            if (bans.get(i) != null) {
//                StudentYear studentYear = new StudentYear();
//                studentYear.setStudentId(studentIds.get(i));
//                studentYear.setYear(years.get(i));
//                studentYear.setNen(nens.get(i));
//                studentYear.setKumi(kumis.get(i));
//                studentYear.setBan(bans.get(i));
//                studentYear.setCreatedBy(userId);
//                studentYear.setUpdatedBy(userId);
//                studentYearRepository.save(studentYear);
//            }
//        }
//
//    }

//    /**
//     * クラス更新
//     * 
//     * @param studentYear
//     * @return
//     */
//    public StudentYear updateClass(Long studentYearId, StudentYear studentYear) {
//        StudentYear targetStudentYear = studentYearRepository.findById(studentYearId).orElseThrow();
//        targetStudentYear.setYear(studentYear.getYear());
//        targetStudentYear.setNen(studentYear.getNen());
//        targetStudentYear.setKumi(studentYear.getKumi());
//        targetStudentYear.setBan(studentYear.getBan());
//        targetStudentYear.setUpdatedBy(studentYear.getUpdatedBy());
//        studentYearRepository.save(targetStudentYear);
//        return targetStudentYear;
////        studentRepository.save(student);
//    }
//
//    /**
//     * クラス一括更新
//     * 
//     * @param studentYear
//     * @return
//     */
//    public void updateClassMulti(String userId, StudentYearForm studentYearForm) {
//        List<Long> studentYearIds = studentYearForm.getStudentYearIds();
//        List<String> studentIds = studentYearForm.getStudentIds();
//        List<Long> years = studentYearForm.getYears();
//        List<Long> nens = studentYearForm.getNens();
//        List<Long> kumis = studentYearForm.getKumis();
//        List<Long> bans = studentYearForm.getBans();
////        for (int i = 0; i < years.size(); i++) {
//        for (int i = 0; i < 3; i++) {
//            if (bans.get(i) != null) {
//                StudentYear studentYear = new StudentYear();
//                studentYear.setStudentYearId(studentYearIds.get(i));
//                studentYear.setStudentId(studentIds.get(i));
//                studentYear.setYear(years.get(i));
//                studentYear.setNen(nens.get(i));
//                studentYear.setKumi(kumis.get(i));
//                studentYear.setBan(bans.get(i));
//                studentYear.setCreatedBy(userId);
//                studentYear.setUpdatedBy(userId);
//                studentYearRepository.save(studentYear);
//            }
//        }
//
//    }

//    /**
//     * クラスCSVダウンロード用
//     * 
//     * @param studentYear
//     */
//    public void insert(StudentYear studentYear) {
//        studentYearRepository.save(studentYear);
//    }

//    // クラス削除
//    public void deleteClass(Long id) {
//        StudentYear studentYear = studentYearRepository.findById(id).orElseThrow();
//        studentYearRepository.delete(studentYear);
//    }

//    // ★★★ 削除をこの形に変更する！！
//    // クラス削除
//    public StudentYear deleteClass(Long id) {
//        StudentYear deleteClass = studentYearRepository.findById(id).orElseThrow();
//        studentYearRepository.delete(deleteClass);
//        return deleteClass;
//    }

//    // クラス削除、その後在籍生徒をソート
//    public void deleteClass(Long id, Long year, Long nen, Long kumi, String userId) {
//        // 生徒削除
//        StudentYear deleteClass = studentYearRepository.findById(id).orElseThrow();
//        studentYearRepository.delete(deleteClass);
//        // クラス在籍生徒取得
//        List <StudentYear> result = studentYearRepository.findAll(Specification.where(studentSpecification.year(year))
//                .and(studentSpecification.nen(nen)).and(studentSpecification.kumi(kumi)));
//        // クラス在籍生徒をソート
//        result.sort(Comparator.comparing(StudentYear::getStudentId));
//        // 新しい出席番号を割り振って更新
//        for (int i = 0; i < result.size(); i++) {
//            StudentYear studentYear = studentYearRepository.findById(id).orElseThrow();
//            studentYear.setBan((long)i+1);
//            studentYear.setUpdatedBy(userId);
//            studentYearRepository.save(studentYear);
//        }

    // クラス在籍生徒を1名削除
    public void deleteClass(Long id) {
        StudentYear studentYear = studentYearRepository.findById(id).orElseThrow();
        studentYearRepository.delete(studentYear);
    }

    // クラス在籍生徒をソートし、出席番号を割り振って更新
    public void sort(Long year, Long nen, Long kumi, String userId) {
        List<StudentYear> result = studentYearRepository.findAll(Specification.where(studentSpecification.year(year))
                .and(studentSpecification.nen(nen)).and(studentSpecification.kumi(kumi)));
        result.sort(Comparator.comparing(StudentYear::getStudentId));
        for (int i = 0; i < result.size(); i++) {
            StudentYear studentYear = studentYearRepository.findById(result.get(i).getStudentYearId()).orElseThrow();
            studentYear.setBan((long) i + 1);
            studentYear.setUpdatedBy(userId);
            studentYearRepository.save(studentYear);
        }
    }

    // クラス在籍生徒が削除可能か判定
    // 紐づいた出席簿・成績のデータがないかどうか確認
    public boolean dataExists(Long id) {
        StudentYear studentYear = studentYearRepository.findById(id).orElseThrow();
        // 出席簿の確認
        List<Attendance> result = attendanceRepository.findAllByStudentId(studentYear.getStudentId());
        List<Attendance> resultAttendance = new ArrayList<>();
        int nendo = Integer.valueOf(studentYear.getYear().toString());
        for (int i = 0; i < result.size(); i++) {
            Calendar cl = Calendar.getInstance();
            cl.setTime(result.get(i).getDate());
            if ((cl.get(Calendar.YEAR) == nendo && cl.get(Calendar.MONTH) >= 3)
                    || (cl.get(Calendar.YEAR) == nendo + 1 && cl.get(Calendar.MONTH) < 3)) {
                resultAttendance.add(result.get(i));
            }
        }
        // 成績の確認
        List<Grade> resultGrade = gradeRepository
                .findAll(Specification.where(studentSpecification.gradeYear(studentYear.getYear()))
                        .and(studentSpecification.gradeStudentId(studentYear.getStudentId())));
        boolean dataExists = false;
        // データがあればtrueを返す
        if (resultAttendance.size() > 0 || resultGrade.size() > 0) {
            dataExists = true;
        }
        return dataExists;
    }

//    /**
//     * 生徒削除（理論削除）　→　転出に変更
//     * 
//     * @param studentYear
//     */
//    @Override
//    @Transactional
//    public void deleteClass(Long studentYearId, StudentYear studentYear) {
//        StudentYear targetStudentYear = studentYearRepository.findById(studentYearId).orElseThrow();
//        targetStudentYear.setUpdatedBy(studentYear.getUpdatedBy());
//        targetStudentYear.setDeleted(Boolean.TRUE);
//        studentYearRepository.save(targetStudentYear);
//    }
}