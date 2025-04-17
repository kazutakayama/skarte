package com.example.skarte.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    @Value("${image.local:false}")
    private String imageLocal;

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
        }
        return classList;
    }

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
     * クラス個別追加
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

    // クラス在籍生徒を1名削除
    public void deleteClass(Long id) {
        StudentYear studentYear = studentYearRepository.findById(id).orElseThrow();
        studentYearRepository.delete(studentYear);
    }

//    // 画像をアップロード
//    public void upload(Long id, String userId, MultipartFile file) throws IOException {
//        StudentYear studentYear = studentYearRepository.findById(id).orElseThrow();
//        MultipartFile multipartFile = file; 
////        try {
////            byte[] bytes = multipartFile.getBytes();            
////        } catch (IOException e) {
////            e.printStackTrace();
////            byte[] bytes = null;
////        }
//        byte[] bytes = file.getBytes();
//        studentYear.setImage(bytes); 
//        studentYear.setUpdatedBy(userId);
//        studentYearRepository.save(studentYear);
//        
//    }

    // 写真をアップロード
    public void upload(Long id, String userId, StudentYearForm studentYearForm) throws IOException {
        StudentYear studentYear = studentYearRepository.findById(id).orElseThrow();
        MultipartFile multipartFile = studentYearForm.getImage();
        byte[] bytes = multipartFile.getBytes();
        studentYear.setImage(bytes);
        studentYear.setUpdatedBy(userId);
        studentYearRepository.save(studentYear);
    }

    // 写真を削除
    public void deleteImage(Long id) {
        StudentYear studentYear = studentYearRepository.findById(id).orElseThrow();
        studentYear.setImage(null);
        studentYearRepository.save(studentYear);
    }

    // 写真byte[]をbase64に変換（1年分）
    public String image(Long id) {
        StudentYear studentYear = studentYearRepository.findById(id).orElseThrow();
        String image = Base64.getEncoder().encodeToString(studentYear.getImage());
        return image;
    }

    // 写真byte[]をbase64に変換（3年分）
    public List<String> images(String studentId) {
        List<String> images = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            images.add(null);
        }
        List<StudentYear> classList = classList(studentId);
        for (int i = 0; i < classList.size(); i++) {
            if (classList.get(i) != null) {
                if (classList.get(i).getImage() != null) {
                    String image = Base64.getEncoder().encodeToString(classList.get(i).getImage());
                    images.set(i, image);
                }
            }
        }
        return images;
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

}