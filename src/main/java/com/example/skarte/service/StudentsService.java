package com.example.skarte.service;

import java.util.List;

//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;

import com.example.skarte.entity.Student;
import com.example.skarte.form.StudentForm;

//@Service
//public class StudentsService {

//インターフェイス
public interface StudentsService {

    /**
     * 生徒全取得
     * 
     * @return
     */
    public List<Student> findAll();

    /**
     * 生徒1件取得
     * 
     * @param id
     * @return
     */
    public Student findById(Long id);

    /**
     * 生徒追加
     * 
     * @param userId
     * @param student
     * @return
     */
    public void addStudent(Long userId, StudentForm form);

    /**
     * 生徒編集
     * 
     * @param student
     * @return
     */
//    public void updateStudent(Long studentId, Student student);
    public Student updateStudent(Long studentId, StudentForm student);

    /**
     * 生徒CSVアップロード用
     * 
     * @param userId
     * @param student
     */
    public void addStudentByCSV(Long userId, Student student);

//    /**
//     * 生徒削除（理論削除）
//     * 
//     * @param student
//     */
//    public void deleteStudent(Long studentId, Student student);

}

//    @Autowired
//    StudentRepository studentRepository;
//
//    public List<Student> findAll() {
//        return studentRepository.findAll();
//    }
//
//    public Student save(Student student) {
//        return studentRepository.save(student);
//    }
//
//    public Student findById(Long id) {
//        return studentRepository.findById(id).orElseThrow();
//    }
//    
//    public Student insert(Student student) {
//        return studentRepository.save(student);
//    }
//    
//    public void delete(Long id) {
//        Student student = studentRepository.findById(id).orElseThrow();
//        studentRepository.delete(student);
//    }
//}