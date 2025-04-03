package com.example.skarte.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.skarte.entity.Student;
import com.example.skarte.entity.StudentYear;

@Repository
public interface StudentRepository extends JpaRepository<Student, String> , JpaSpecificationExecutor<Student>{

    // 更新日時の降順（削除済みは表示しない）
//    List<Student> findByDeletedFalseOrderByUpdatedAtDesc();

    List<Student> findByOrderByUpdatedAtDesc();
//    List<Student> findAll();
//    List<Student> findAllByOrderById(String id);
    
//    Page<Student> findAll(Pageable pageable);
    

//    // 生徒ID検索
//    public Optional<Student> findById(Long id) {
//        String query = "SELECT *"
//                + " FROM students"
//                + " WHERE id=?";
//        
//        // 検索実行
//        // id = 1のとき
//        // employee.put("id", "1")
//        // employee.put("name", "Kato")
//        // employee.put("age", 10)
//        Map<String, Object> student = jdbcTemplate.queryForMap(query, id);
//        return student;
//    }

//        @Query(value = "SELECT CAST(student_id AS INTEGER)\r\n"
//                + "FROM students", nativeQuery = true)
//        List<Student> findById(Specification<Student> where);

   
    
//    @Query(value = "SELECT *\r\n"
//            + "FROM students\r\n"
//            + "WHERE last_name = '朝比奈'\r\n"
//            + "or  last_name= '池田'\r\n"
//            + "or last_name_kana = '朝比奈'\r\n"
//            + "or last_name_kana = '朝比奈'", nativeQuery = true)
//    List<Student> findByName();
//    
//    @Query(value = "SELECT *\r\n"
//            + "FROM students\r\n"
//            + "WHERE last_name = ''\r\n"
//            + "or  first_name= ''\r\n"
//            + "or last_name_kana = ''\r\n"
//            + "or last_name_kana = ''", nativeQuery = true)
//    List<Student> findByLastName(@Param("name") Long id);

//    List<Student> findAllByLastName(String lastName);
}