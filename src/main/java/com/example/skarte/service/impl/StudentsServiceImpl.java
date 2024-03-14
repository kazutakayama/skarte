package com.example.skarte.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.skarte.entity.Student;
import com.example.skarte.repository.StudentRepository;
import com.example.skarte.service.StudentsService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentsServiceImpl implements StudentsService {

    private final StudentRepository studentRepository;

    /**
     * 生徒全取得
     * 
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<Student> findAll() {
        return studentRepository.findByDeletedFalseOrderByUpdatedAtDesc();
    }

    /**
     * 生徒1件取得
     * 
     * @param id
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public Student findById(Long id) {
        return studentRepository.findById(id).orElseThrow();
    }

    /**
     * 生徒追加
     * 
     * @param student
     * @return
     */
    @Override
    @Transactional
    public void addStudent(Student student) {
        studentRepository.save(student);
    }

    /**
     * 生徒編集
     * 
     * @param student
     * @return
     */
    @Override
    @Transactional
    public Student updateStudent(Long studentId, Student student) {
        Student targetStudent = studentRepository.findById(studentId).orElseThrow();
        targetStudent.setLastName(student.getLastName());
        targetStudent.setFirstName(student.getFirstName());
        targetStudent.setLastNameKana(student.getLastNameKana());
        targetStudent.setFirstNameKana(student.getFirstNameKana());
        targetStudent.setBirth(student.getBirth());
        targetStudent.setGender(student.getGender());
        targetStudent.setFamily1(student.getFamily1());
        targetStudent.setFamily2(student.getFamily2());
        targetStudent.setFamily3(student.getFamily3());
        targetStudent.setFamily4(student.getFamily4());
        targetStudent.setTel1(student.getTel1());
        targetStudent.setTel2(student.getTel2());
        targetStudent.setTel3(student.getTel3());
        targetStudent.setTel4(student.getTel4());
        targetStudent.setPostalCode(student.getPostalCode());
        targetStudent.setAdress(student.getAdress());
        targetStudent.setMemo(student.getMemo());
        studentRepository.save(targetStudent);
        return targetStudent;
//        studentRepository.save(student);
    }

    /**
     * 生徒CSVダウンロード用
     * 
     * @param student
     */
    @Override
    @Transactional
    public void insertStudent(Student student) {
        studentRepository.save(student);
    }

    /**
     * 生徒削除（理論削除）
     * 
     * @param student
     */
    @Override
    @Transactional
    public void deleteStudent(Long studentId, Student student) {
        Student targetStudent = studentRepository.findById(studentId).orElseThrow();
        targetStudent.setDeleted(Boolean.TRUE);
        studentRepository.save(targetStudent);
    }
}