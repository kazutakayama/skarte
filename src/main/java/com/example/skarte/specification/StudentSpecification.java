package com.example.skarte.specification;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.example.skarte.entity.Grade;
import com.example.skarte.entity.Student;
import com.example.skarte.entity.StudentYear;

import io.micrometer.common.util.StringUtils;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Component
public class StudentSpecification {

    @SuppressWarnings("serial")
    public static Specification<Student> search(String name) {
        return StringUtils.isEmpty(name) ? null : new Specification<Student>() {
            @Override
            public Predicate toPredicate(Root<Student> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate[] predicates = { cb.like(root.get("lastName"), "%" + name + "%"),
                        cb.like(root.get("firstName"), "%" + name + "%"),
                        cb.like(root.get("lastNameKana"), "%" + name + "%"),
                        cb.like(root.get("firstNameKana"), "%" + name + "%") };
                return cb.or(predicates);
            };
        };
    }

    @SuppressWarnings("serial")
    public static Specification<Student> year(String year) {
        return StringUtils.isEmpty(year) ? null : new Specification<Student>() {
            @Override
            public Predicate toPredicate(Root<Student> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.like(root.get("studentId"), year + "%");
            }
        };
    }

    @SuppressWarnings("serial")
    public static Specification<StudentYear> year(Long year) {
        return year == null ? null : new Specification<StudentYear>() {
            @Override
            public Predicate toPredicate(Root<StudentYear> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.equal(root.get("year"), year);
            }
        };
    }

    @SuppressWarnings("serial")
    public static Specification<StudentYear> nen(Long nen) {
        return nen == null ? null : new Specification<StudentYear>() {
            @Override
            public Predicate toPredicate(Root<StudentYear> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.equal(root.get("nen"), nen);
            }
        };
    }

    @SuppressWarnings("serial")
    public static Specification<StudentYear> kumi(Long kumi) {
        return kumi == null ? null : new Specification<StudentYear>() {
            @Override
            public Predicate toPredicate(Root<StudentYear> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.equal(root.get("kumi"), kumi);
            }
        };
    }

    @SuppressWarnings("serial")
    public static Specification<Grade> gradeStudentId(String studentId) {
        return StringUtils.isEmpty(studentId) ? null : new Specification<Grade>() {
            @Override
            public Predicate toPredicate(Root<Grade> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.equal(root.get("studentId"), studentId);
            }
        };
    }

    @SuppressWarnings("serial")
    public static Specification<Grade> gradeYear(Long year) {
        return year == null ? null : new Specification<Grade>() {
            @Override
            public Predicate toPredicate(Root<Grade> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.equal(root.get("year"), year);
            }
        };
    }

}
