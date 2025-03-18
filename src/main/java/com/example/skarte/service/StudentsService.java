package com.example.skarte.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.skarte.entity.Karte;
import com.example.skarte.entity.Student;
import com.example.skarte.form.StudentForm;
import com.example.skarte.repository.StudentRepository;
import com.example.skarte.repository.StudentYearRepository;

@Service
public class StudentsService {

    @Autowired
    StudentRepository studentRepository;
    StudentYearRepository studentYearRepository;

    // 生徒全取得
    public List<Student> findAll() {
        return studentRepository.findByOrderByUpdatedAtDesc();
    }
   
    // 生徒１件取得
    public Student findById(Long id) {
        return studentRepository.findById(id).orElseThrow();
    }
    
    // 生徒追加
    public void addStudent(Long userId, StudentForm form) {
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
    public Student updateStudent(Long studentId, StudentForm form) {
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
    public void addStudentByCSV(Long userId, Student student) {
//      // 入力値に対するバリデーション処理
//       List<String> errorMessage = validation(student);
//       if (errorMessage.size() != 0) {
//           return errorMessage;
//       }
         student.setCreatedBy(userId);
         student.setUpdatedBy(userId);
         studentRepository.save(student);
     }
     
//     private List<String> validation(Student student) {
//       List<String> errorMessage = new ArrayList<>();
//       // LastNameに対する必須項目チェック
//       if (StringUtils.isEmpty(student.getLastName())) {
//           errorMessage.add("LastNameが未入力です");
//       }
//       
//       // tel1に対する必須項目チェック
//       if (Objects.isNull(student.getTel1())) {
//           
//       }
//       
//       
//       return errorMessage;
//   }
//    
    // 生徒削除
    public void deleteStudent(Long id) {
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