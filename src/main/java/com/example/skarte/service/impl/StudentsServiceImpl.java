//package com.example.skarte.service.impl;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Objects;
//
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.example.skarte.entity.Student;
//import com.example.skarte.form.StudentForm;
//import com.example.skarte.repository.StudentRepository;
//import com.example.skarte.service.StudentsService;
//
//import io.micrometer.common.util.StringUtils;
//import lombok.RequiredArgsConstructor;
//
//@Service
//@RequiredArgsConstructor
//public class StudentsServiceImpl implements StudentsService {
//
//    private final StudentRepository studentRepository;
//
//    /**
//     * 生徒全取得
//     * 
//     * @return
//     */
//    @Override
//    @Transactional(readOnly = true)
//    public List<Student> findAll() {
////        return studentRepository.findByDeletedFalseOrderByUpdatedAtDesc();
//        return studentRepository.findByOrderByUpdatedAtDesc();
//    }
//
//    /**
//     * 生徒1件取得
//     * 
//     * @param id
//     * @return
//     */
//    @Override
//    @Transactional(readOnly = true)
//    public Student findById(Long id) {
//        return studentRepository.findById(id).orElseThrow();
//    }
//
//    /**
//     * 生徒追加
//     * 
//     * @param student
//     * @return
//     */
//    @Override
//    @Transactional
//    public void addStudent(Long userId, StudentForm form) {
//        Student student = new Student();
//        student.setStudentId(form.getStudentId());
//        student.setLastName(form.getLastName());
//        student.setFirstName(form.getFirstName());
//        student.setLastNameKana(form.getLastNameKana());
//        student.setFirstNameKana(form.getFirstNameKana());
//        student.setBirth(form.getBirth());
//        student.setGender(form.getGender());
//        student.setFamily1(form.getFamily1());
//        student.setFamily2(form.getFamily2());
//        student.setTel1(form.getTel1());
//        student.setTel2(form.getTel2());
//        student.setTel3(form.getTel3());
//        student.setTel4(form.getTel4());
//        student.setPostalCode(form.getPostalCode());
//        student.setAdress(form.getAdress());
//        student.setMemo(form.getMemo());
//        
//        student.setCreatedBy(userId);
//        student.setUpdatedBy(userId);
//        studentRepository.save(student);
//    }
//    
//
//    /**
//     * 生徒編集
//     * 
//     * @param student
//     * @return
//     */
//    @Override
//    @Transactional
//    public Student updateStudent(Long studentId, StudentForm form) {       
//        Student targetStudent = studentRepository.findById(studentId).orElseThrow();
//        targetStudent.setLastName(form.getLastName());
//        targetStudent.setFirstName(form.getFirstName());
//        targetStudent.setLastNameKana(form.getLastNameKana());
//        targetStudent.setFirstNameKana(form.getFirstNameKana());
//        targetStudent.setBirth(form.getBirth());
//        targetStudent.setGender(form.getGender());
//        targetStudent.setFamily1(form.getFamily1());
//        targetStudent.setFamily2(form.getFamily2());
//        targetStudent.setTel1(form.getTel1());
//        targetStudent.setTel2(form.getTel2());
//        targetStudent.setTel3(form.getTel3());
//        targetStudent.setTel4(form.getTel4());
//        targetStudent.setPostalCode(form.getPostalCode());
//        targetStudent.setAdress(form.getAdress());
//        targetStudent.setMemo(form.getMemo());
//        targetStudent.setUpdatedBy(form.getUpdatedBy());
//        studentRepository.save(targetStudent);
//        return targetStudent;
////        studentRepository.save(student);
//    }
//    
//
//    /**
//     * 生徒CSVアップロード用
//     * 
//     * @param student
//     */
//    @Override
//    @Transactional
//    public void addStudentByCSV(Long userId, Student student) {
//     // 入力値に対するバリデーション処理
//      List<String> errorMessage = validation(student);
//      if (errorMessage.size() != 0) {
////          return errorMessage;
//      }
//        student.setCreatedBy(userId);
//        student.setUpdatedBy(userId);
//        studentRepository.save(student);
//    }
//    
//    private List<String> validation(Student student) {
//      List<String> errorMessage = new ArrayList<>();
//      // LastNameに対する必須項目チェック
//      if (StringUtils.isEmpty(student.getLastName())) {
//          errorMessage.add("LastNameが未入力です");
//      }
//      
//      // tel1に対する必須項目チェック
//      if (Objects.isNull(student.getTel1())) {
//          
//      }
//      
//      
//      return errorMessage;
//  }
//
//
////    /**
////     * 生徒削除（理論削除）　→　※転出済みに変更する！★★
////     * 
////     * @param student
////     */
////    @Override
////    @Transactional
////    public void deleteStudent(Long studentId, Student student) {
////        Student targetStudent = studentRepository.findById(studentId).orElseThrow();
////        targetStudent.setUpdatedBy(student.getUpdatedBy());
////        targetStudent.setDeleted(Boolean.TRUE);
////        studentRepository.save(targetStudent);
////    }
//}