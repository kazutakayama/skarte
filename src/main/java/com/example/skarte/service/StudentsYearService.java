//package com.example.skarte.service;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.example.skarte.entity.Student;
//import com.example.skarte.entity.StudentYear;
//import com.example.skarte.form.StudentForm;
//import com.example.skarte.repository.StudentRepository;
//import com.example.skarte.repository.StudentYearRepository;
//
//@Service
//public class StudentsYearService {
//
//    @Autowired
//    StudentRepository studentRepository;
//    StudentYearRepository studentYearRepository;
//
//    // クラス全取得
//    public List<StudentYear> findAll() {
//        return studentYearRepository.findByOrderByUpdatedAtDesc();
//    }
//
////    /**
////     * 生徒IDでリストを取得
////     * 
////     * @return
////     */
//    public List<StudentYear> findAllByStudentId(Long studentId) {
//        return studentYearRepository.findAllByStudentId(studentId);
//    }
//
//    // クラス１件取得
//    public StudentYear findById(Long id) {
//        return studentYearRepository.findById(id).orElseThrow();
//    }
//
//    /**
//     * クラス追加
//     * 
//     * @param userId
//     * @param student
//     * @return
//     */
//    public void addClass(Long userId, StudentYear studentYear) {
//        studentYear.setCreatedBy(userId);
//        studentYear.setUpdatedBy(userId);
//        studentYearRepository.save(studentYear);
//    }
//
//    /**
//     * クラス編集
//     * 
//     * @param student
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
//    }
//
//    /**
//     * クラスCSVダウンロード用
//     * 
//     * @param student
//     */
//    public void insert(StudentYear studentYear) {
//        studentYearRepository.save(studentYear);
//    }
//
////    /**
////     * クラス削除（理論削除）
////     * 
////     * @param student
////     */
////    public void deleteClass(Long studentYearId, StudentYear studentYear);
//
//}

//package com.example.skarte.service;
//
//import java.util.List;
//
//import com.example.skarte.entity.StudentYear;
//
////インターフェイス
//public interface StudentsYearService {
//
//    /**
//     * クラス全取得
//     * 
//     * @return
//     */
//    public List<StudentYear> findAll();
//    
//    /**
//     * 生徒IDでリストを取得
//     * 
//     * @return
//     */
//    public List<StudentYear> findAllByStudentId(Long studentId);
//
//
//    /**
//     * クラス1件取得
//     * 
//     * @param id
//     * @return
//     */
//    public StudentYear findById(Long id);
//
//    
//    /**
//     * クラス追加
//     * 
//     * @param userId
//     * @param student
//     * @return
//     */
//    public void addClass(Long userId, StudentYear studentYear);
//
//    /**
//     * クラス編集
//     * 
//     * @param student
//     * @return
//     */
//    public StudentYear updateClass(Long studentYearId, StudentYear studentYear);
//
//    /**
//     * クラスCSVダウンロード用
//     * 
//     * @param student
//     */
//    public void insert(StudentYear studentYear);
//
////    /**
////     * クラス削除（理論削除）
////     * 
////     * @param student
////     */
////    public void deleteClass(Long studentYearId, StudentYear studentYear);
//
//}

package com.example.skarte.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.skarte.entity.Grade;
import com.example.skarte.entity.Student;
import com.example.skarte.entity.StudentYear;
import com.example.skarte.form.StudentYearForm;
import com.example.skarte.repository.GradeRepository;
import com.example.skarte.repository.StudentYearRepository;
import com.example.skarte.specification.StudentSpecification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentsYearService {
    
    private final StudentYearRepository studentYearRepository;
    private final StudentSpecification studentSpecification;

//    @Autowired
//
//    StudentYearRepository studentYearRepository;
//
//    StudentSpecification studentSpecification;

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
    


//    /**
//     * 生徒IDでリストを取得（リスト）
//     * 
//     * @return
//     */
//    public List<StudentYear> classList(String studentId) {
//        List<StudentYear> classList = new ArrayList<StudentYear>();
//        classList.add(null);
//        classList.add(null);
//        classList.add(null);
//
//        List<StudentYear> result = studentYearRepository.findAllByStudentId(studentId);
//
//        for (int i = 0; i < result.size(); i++) {
//            if (result.get(i).getNen() == 1) {
//                classList.set(0, result.get(i));
//            }
//            if (result.get(i).getNen() == 2) {
//                classList.set(1, result.get(i));
//            }
//            if (result.get(i).getNen() == 3) {
//                classList.set(2, result.get(i));
//            }
////            classList.add(result.get(i));
//        }
//        return classList;
//    }
    
    /**
     * 生徒IDでリストを取得（配列）
     * 
     * @return
     */
    public List<StudentYear> classList(String studentId) {
        StudentYear[] array =new StudentYear[3];
        
        
//        List<StudentYear> classList = new ArrayList<StudentYear>();
//        classList.add(null);
//        classList.add(null);
//        classList.add(null);
//
        List<StudentYear> result = studentYearRepository.findAllByStudentIdOrderByYearAsc(studentId);

        for (int i = 0; i < result.size(); i++) {
            if (result.get(i).getNen() == 1) {
                array[0] = result.get(i);
            }
            if (result.get(i).getNen() == 2) {
                array[1] = result.get(i);
            }
            if (result.get(i).getNen() == 3) {
                array[2] = result.get(i);
            }
//            classList.add(result.get(i));
        }
        List<StudentYear> classList = Arrays.asList(array);
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

    /**
     * クラス追加
     * 
     * @param studentYear
     * @return
     */
    public void addClass(String userId, StudentYear studentYear) {
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
    public void addClassMulti(String userId, StudentYearForm studentYearForm) {
//        List<Long> studentYearIds = studentYearForm.getStudentYearIds();
//        for (Long studentYearId : studentYearIds) {
//            studentYear.setStudentYearId(studentYearForm.getStudentYearId());
//        }
        List<String> studentIds = studentYearForm.getStudentIds();
        List<Long> years = studentYearForm.getYears();
        List<Long> nens = studentYearForm.getNens();
        List<Long> kumis = studentYearForm.getKumis();
        List<Long> bans = studentYearForm.getBans();
        for (int i = 0; i < years.size(); i++) {
            if (bans.get(i) != null) {
                StudentYear studentYear = new StudentYear();
                studentYear.setStudentId(studentIds.get(i));
                studentYear.setYear(years.get(i));
                studentYear.setNen(nens.get(i));
                studentYear.setKumi(kumis.get(i));
                studentYear.setBan(bans.get(i));
                studentYear.setCreatedBy(userId);
                studentYear.setUpdatedBy(userId);
                studentYearRepository.save(studentYear);
            }
        }

    }

    /**
     * クラス更新
     * 
     * @param studentYear
     * @return
     */
    public StudentYear updateClass(Long studentYearId, StudentYear studentYear) {
        StudentYear targetStudentYear = studentYearRepository.findById(studentYearId).orElseThrow();
        targetStudentYear.setYear(studentYear.getYear());
        targetStudentYear.setNen(studentYear.getNen());
        targetStudentYear.setKumi(studentYear.getKumi());
        targetStudentYear.setBan(studentYear.getBan());
        targetStudentYear.setUpdatedBy(studentYear.getUpdatedBy());
        studentYearRepository.save(targetStudentYear);
        return targetStudentYear;
//        studentRepository.save(student);
    }

    /**
     * クラス一括更新
     * 
     * @param studentYear
     * @return
     */
    public void updateClassMulti(String userId, StudentYearForm studentYearForm) {
        List<Long> studentYearIds = studentYearForm.getStudentYearIds();
        List<String> studentIds = studentYearForm.getStudentIds();
        List<Long> years = studentYearForm.getYears();
        List<Long> nens = studentYearForm.getNens();
        List<Long> kumis = studentYearForm.getKumis();
        List<Long> bans = studentYearForm.getBans();
//        for (int i = 0; i < years.size(); i++) {
        for (int i = 0; i < 3; i++) {
            if (bans.get(i) != null) {
                StudentYear studentYear = new StudentYear();
                studentYear.setStudentYearId(studentYearIds.get(i));
                studentYear.setStudentId(studentIds.get(i));
                studentYear.setYear(years.get(i));
                studentYear.setNen(nens.get(i));
                studentYear.setKumi(kumis.get(i));
                studentYear.setBan(bans.get(i));
                studentYear.setCreatedBy(userId);
                studentYear.setUpdatedBy(userId);
                studentYearRepository.save(studentYear);
            }
        }

    }

    /**
     * クラスCSVダウンロード用
     * 
     * @param studentYear
     */
    public void insert(StudentYear studentYear) {
        studentYearRepository.save(studentYear);
    }

//    // クラス削除
//    public void deleteClass(Long id) {
//        StudentYear studentYear = studentYearRepository.findById(id).orElseThrow();
//        studentYearRepository.delete(studentYear);
//    }

    // ★★★ 削除をこの形に変更する！！
    // クラス削除
    public StudentYear deleteClass(Long id) {
        StudentYear deleteClass = studentYearRepository.findById(id).orElseThrow();
        studentYearRepository.delete(deleteClass);
        return deleteClass;
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