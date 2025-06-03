package com.example.skarte.specification;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.example.skarte.entity.Grade;
import com.example.skarte.entity.Student;
import com.example.skarte.entity.StudentYear;

import jakarta.persistence.criteria.Predicate;

@Component
public class StudentSpecification {

    public static Specification<Student> search(String name) {
        return (root, query, cb) -> {
            if (name == null || name.isEmpty()) {
                return cb.conjunction(); // 常にtrue
            }
            Predicate[] predicates = { cb.like(root.get("lastName"), "%" + name + "%"),
                    cb.like(root.get("firstName"), "%" + name + "%"),
                    cb.like(root.get("lastNameKana"), "%" + name + "%"),
                    cb.like(root.get("firstNameKana"), "%" + name + "%") };
            return cb.or(predicates);
        };
    }

    public static Specification<Student> year(String year) {
        return (root, query, cb) -> {
            if (year == null || year.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(root.get("studentId"), year + "%");
        };
    }

    public static Specification<StudentYear> year(Long year) {
        return (root, query, cb) -> {
            if (year == null || year == 0) {
                return cb.conjunction();
            }
            return cb.equal(root.get("year"), year);
        };
    }

    public static Specification<StudentYear> nen(Long nen) {
        return (root, query, cb) -> {
            if (nen == null || nen == 0) {
                return cb.conjunction();
            }
            return cb.equal(root.get("nen"), nen);
        };
    }

    public static Specification<StudentYear> kumi(Long kumi) {
        return (root, query, cb) -> {
            if (kumi == null || kumi == 0) {
                return cb.conjunction();
            }
            return cb.equal(root.get("kumi"), kumi);
        };
    }

    public static Specification<Grade> gradeStudentId(String studentId) {
        return (root, query, cb) -> {
            if (studentId == null || studentId.isEmpty()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("studentId"), studentId);
        };
    }

    public static Specification<Grade> gradeYear(Long year) {
        return (root, query, cb) -> {
            if (year == null || year == 0) {
                return cb.conjunction();
            }
            return cb.equal(root.get("year"), year);
        };
    }

}
