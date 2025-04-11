package com.example.skarte.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import com.example.skarte.entity.Student;
import com.example.skarte.entity.StudentYear;
import com.example.skarte.form.StudentForm;
import com.example.skarte.repository.StudentRepository;
import com.example.skarte.repository.StudentYearRepository;
import com.example.skarte.specification.StudentSpecification;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
//import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentsService {
    
    private final StudentRepository studentRepository;
    private final StudentYearRepository studentYearRepository;
    private final StudentSpecification studentSpecification;

//    @Autowired
//    StudentRepository studentRepository;
//
//    StudentYearRepository studentYearRepository;
//
//    StudentSpecification studentSpecification;

    // 生徒全取得
    public List<Student> findAll() {
        return studentRepository.findByOrderByUpdatedAtDesc();
//        return studentRepository.findAll();
    }

    // 生徒１件取得
    public Student findById(String id) {
        return studentRepository.findById(id).orElseThrow();
    }

    // 生徒名検索
    public List<Student> search(String name) {
        List<Student> result;
        if ("".equals(name)) {
//            result = studentRepository.findByOrderByUpdatedAtDesc();
            result = studentRepository.findAll(Sort.by(Sort.Direction.ASC, "studentId"));
        } else {
            result = studentRepository.findAll(Specification.where(studentSpecification.search(name)), Sort.by(Sort.Direction.ASC, "studentId"));
        }
        return result;
    }

    // 登録年度検索
    public List<Student> year(String year) {
         return studentRepository.findAll(Specification.where(studentSpecification.year(year)), Sort.by(Sort.Direction.ASC, "studentId"));
    }

//    public List<Student> search(String name) {
//        // 前後の全角半角スペースを削除
//        String trimedkeyWords = name.strip();
//
//        // 全角スペースと半角スペースで区切る
//        String[] keyWordArray = trimedkeyWords.split("[　 ]", 0);
//
//        // 「Select * From students」 + 「Where last_name LIKE '%keyWordArray[0]%'」
//        return StudentRepository.findAll(Specification.where(studentSpecification.search(keyWordArray[0])));
//    }

//        List<Student> result;
//        if ("".equals(lastName)) {
//            result = studentRepository.findByOrderByUpdatedAtDesc();
//        } else {
//            result = studentRepositoryCustom.search(lastName);
//        }
//        return result;

//    // 生徒ID検索
//    public Student getStudent(Long id) {
//        Optional<Student> map = studentRepository.findById(id); // findById(Long id)の戻り値"student"を取得
//        // Mapから値を取得
//        Long studentId = (Long) map.get("studentId");
//        String lastName = (String) map.get("lastName");
//        String firstName = (String) map.get("firstName");
//        String lastNameKana = (String) map.get("lastNameKana");
//        String firstNameKana = (String) map.get("firstNameKana");
//        Date birth = (Date) map.get("birth");
//        int gender = (Integer) map.get("gender");
//
////        String family1 = (String) map.get("family1");
////        String family2 = (String) map.get("family2");
////        Long tel1 = (Long) map.get("tel1");
////        Long tel2 = (Long) map.get("tel2");
////        Long tel3 = (Long) map.get("tel3");
////        Long tel4 = (Long) map.get("tel4");
////        Long postalCode = (Long) map.get("postalCode");
////        String adress = (String) map.get("adress");
////        String memo = (String) map.get("memo");
////        boolean transferred = (Boolean) map.get("transferred");
//
//        // Studentクラスに値をセット
//        Student student = new Student();
//        student.setStudentId(studentId);
//        student.setLastName(lastName);
//        student.setFirstName(firstName);
//        student.setLastNameKana(lastNameKana);
//        student.setFirstName(firstNameKana);
//        student.setBirth(birth);
//        student.setGender(gender);
//        return student;
//    }

//    // 生徒名検索
//    public List<Student> findByLastName(Long id) {
//        return studentRepository.findByLastName(id);
//    }

//    // 生徒名検索
//    public List<Student> findAllByLastName(String lastName) {
//        return studentRepository.findAllByLastName(lastName);
//    }

//    // 生徒名でフィルターをかける
//    public List<Student> findAllByLastName(String lastName) {
//        return studentRepository.findAllByLastName(lastName);
//    }

    // 生徒追加
    public void addStudent(String userId, StudentForm form) {
        Student student = new Student();
        student.setStudentId(form.getStudentId());
        student.setLastName(form.getLastName());
        student.setFirstName(form.getFirstName());
        student.setLastNameKana(form.getLastNameKana());
        student.setFirstNameKana(form.getFirstNameKana());
        student.setBirth(form.getBirth());
        student.setGender(form.getGender());
        student.setFamily1(form.getFamily1());
        student.setFamily2(form.getFamily2());
        student.setTel1(form.getTel1());
        student.setTel2(form.getTel2());
        student.setTel3(form.getTel3());
        student.setTel4(form.getTel4());
        student.setPostalCode(form.getPostalCode());
        student.setAdress(form.getAdress());
        student.setMemo(form.getMemo());
        student.setCreatedBy(userId);
        student.setUpdatedBy(userId);
        studentRepository.save(student);
    }

    // 生徒編集
    public Student updateStudent(String studentId, StudentForm form) {
        Student targetStudent = studentRepository.findById(studentId).orElseThrow();
        targetStudent.setLastName(form.getLastName());
        targetStudent.setFirstName(form.getFirstName());
        targetStudent.setLastNameKana(form.getLastNameKana());
        targetStudent.setFirstNameKana(form.getFirstNameKana());
        targetStudent.setBirth(form.getBirth());
        targetStudent.setGender(form.getGender());
        targetStudent.setFamily1(form.getFamily1());
        targetStudent.setFamily2(form.getFamily2());
        targetStudent.setTel1(form.getTel1());
        targetStudent.setTel2(form.getTel2());
        targetStudent.setTel3(form.getTel3());
        targetStudent.setTel4(form.getTel4());
        targetStudent.setPostalCode(form.getPostalCode());
        targetStudent.setAdress(form.getAdress());
        targetStudent.setMemo(form.getMemo());
        targetStudent.setUpdatedBy(form.getUpdatedBy());
        studentRepository.save(targetStudent);
        return targetStudent;
    }

    // 生徒CSVアップロード
    public void upload(String userId, Student student) {
//        // 入力値に対するバリデーション処理
//        List<String> errorMessage = validation(student);
//        if (errorMessage.size() != 0) {
//            return errorMessage;
//        }
        student.setStudentId(student.getStudentId());
        student.setLastName(student.getLastName());
        student.setFirstName(student.getFirstName());
        student.setLastNameKana(student.getLastNameKana());
        student.setFirstNameKana(student.getFirstNameKana());
        student.setBirth(student.getBirth());
        student.setGender(student.getGender());
        student.setFamily1(student.getFamily1());
        student.setFamily2(student.getFamily2());
        student.setTel1(student.getTel1());
        student.setTel2(student.getTel2());
        student.setTel3(student.getTel3());
        student.setTel4(student.getTel4());
        student.setPostalCode(student.getPostalCode());
        student.setAdress(student.getAdress());
        student.setMemo(student.getMemo());
//
        student.setCreatedBy(userId);
        student.setUpdatedBy(userId);
        studentRepository.save(student);
    }

//     private List<String> validation(Student student) {
//         List<String> errorMessage = new ArrayList<>();
//         // LastNameに対する必須項目チェック
//         if (StringUtils.isEmpty(student.getLastName())) {
//             errorMessage.add("LastNameが未入力です");
//         }
//
//         // tel1に対する必須項目チェック
//         if (Objects.isNull(student.getTel1())) {
//
//         }
//
//         return errorMessage;
//     } 

    // 生徒削除
    public void deleteStudent(String id) {
        Student student = studentRepository.findById(id).orElseThrow();
        studentRepository.delete(student);
    }
}

//package com.example.skarte.service;
//
//import java.util.List;
//
//import org.springframework.stereotype.Service;
//
////import org.springframework.stereotype.Service;
//
//import com.example.skarte.entity.Student;
//import com.example.skarte.form.StudentForm;
//
//@Service
//
//
////インターフェイス
//public interface StudentsService {
//
//    /**
//     * 生徒全取得
//     * 
//     * @return
//     */
//    public List<Student> findAll();
//
//    /**
//     * 生徒1件取得
//     * 
//     * @param id
//     * @return
//     */
//    public Student findById(Long id);
//
//    /**
//     * 生徒追加
//     * 
//     * @param userId
//     * @param student
//     * @return
//     */
//    public void addStudent(Long userId, StudentForm form);
//
//    /**
//     * 生徒編集
//     * 
//     * @param student
//     * @return
//     */
////    public void updateStudent(Long studentId, Student student);
//    public Student updateStudent(Long studentId, StudentForm student);
//
//    /**
//     * 生徒CSVアップロード用
//     * 
//     * @param userId
//     * @param student
//     */
//    public void addStudentByCSV(Long userId, Student student);
//
////    /**
////     * 生徒削除（理論削除）
////     * 
////     * @param student
////     */
////    public void deleteStudent(Long studentId, Student student);
//
//}
//
////    @Autowired
////    StudentRepository studentRepository;
////
////    public List<Student> findAll() {
////        return studentRepository.findAll();
////    }
////
////    public Student save(Student student) {
////        return studentRepository.save(student);
////    }
////
////    public Student findById(Long id) {
////        return studentRepository.findById(id).orElseThrow();
////    }
////    
////    public Student insert(Student student) {
////        return studentRepository.save(student);
////    }
////    
////    public void delete(Long id) {
////        Student student = studentRepository.findById(id).orElseThrow();
////        studentRepository.delete(student);
////    }
////}