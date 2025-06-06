package com.example.skarte.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.skarte.entity.Grade;
import com.example.skarte.entity.StudentYear;
import com.example.skarte.form.GradeForm;
import com.example.skarte.repository.GradeRepository;
import com.example.skarte.repository.StudentYearRepository;
import com.example.skarte.specification.StudentSpecification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GradeService {

    private final GradeRepository gradeRepository;
    private final StudentYearRepository studentYearRepository;

    /** 成績全取得 */
    public List<Grade> findAll() {
        return gradeRepository.findByOrderByUpdatedAtDesc();
    }

    /** 成績1件取得 */
    public Grade findById(Long id) {
        return gradeRepository.findById(id).orElseThrow();
    }

    /** 生徒IDでリストを取得 */
    public List<Grade> findAllByStudentId(String studentId) {
        return gradeRepository.findAllByStudentId(studentId);
    }

    /** 年度でリストを取得 */
    public List<Grade> year(int year) {
        return gradeRepository.findAllByYear(year);
    }

    /** 生徒ごとの１年分の成績リストを取得 */
    public List<Grade> studentGrade(String studentId, int year) {
        List<Grade> studentGrade = new ArrayList<Grade>();
        for (int i = 0; i < 27; i++) {
            studentGrade.add(null);
        }
        List<Grade> result = gradeRepository.findAllByStudentId(studentId);
        for (int i = 0; i < result.size(); i++) {
            if (result.get(i).getYear() == year) {
                for (int j = 1; j < 4; j++) {
                    for (int k = 1; k < 10; k++) {
                        if (result.get(i).getTerm() == j && result.get(i).getSubject() == k) {
                            studentGrade.set((9 * j + k - 10), result.get(i));
                        }
                    }
                }
            }
        }
        return studentGrade;
    }

    /** 生徒ごとの3年分の成績リストを取得 */
    public ArrayList<ArrayList<Grade>> studentGradeAll(String studentId) {
        ArrayList<ArrayList<Grade>> studentGradeAll = new ArrayList<>();
        // 生徒IDからクラス登録年度を検索
        List<StudentYear> studentYear = studentYearRepository.findAllByStudentIdOrderByYearAsc(studentId);
        for (int i = 0; i < studentYear.size(); i++) {
            // 「生徒ごとの１年分の成績リスト」
            ArrayList<Grade> studentGrade = new ArrayList<Grade>();
            for (int j = 0; j < 27; j++) {
                studentGrade.add(null);
            }
            int nendo = studentYear.get(i).getYear(); // これが必要
            // 生徒IDから成績を検索
            List<Grade> result = gradeRepository.findAllByStudentId(studentId);
            for (int k = 0; k < result.size(); k++) {
                // クラス登録年度から成績をしぼりこむ
                if (result.get(k).getYear() == nendo) {
                    for (int l = 1; l < 4; l++) { // 学期term
                        for (int m = 1; m < 10; m++) { // 教科subject
                            if (result.get(k).getTerm() == l && result.get(k).getSubject() == m) {
                                // 「生徒ごとの１年分の成績リスト」にセット
                                studentGrade.set((9 * l + m - 10), result.get(k));
                            }
                        }
                    }
                }
            }
            // 配列に追加
            studentGradeAll.add(studentGrade);
        }
        return studentGradeAll;
    }

    /** 成績リスト（クラス（学年）ごとの1年分）を取得 */
    public ArrayList<ArrayList<Grade>> gradeList(int year, int nen, int kumi) {
        ArrayList<ArrayList<Grade>> gradeList = new ArrayList<>();
        // 年度、年、組からクラスの生徒リストを取得
        List<StudentYear> result;
        Specification<StudentYear> spec = (root, query, cb) -> null;
        if (year != 0) {
            spec = spec.and(StudentSpecification.year(year));
        }
        if (nen != 0) {
            spec = spec.and(StudentSpecification.nen(nen));
        }
        if (kumi != 0) {
            spec = spec.and(StudentSpecification.kumi(kumi));
        }
        Sort sort = (kumi == 0) ? Sort.by(Sort.Direction.ASC, "kumi", "ban")
                : Sort.by(Sort.Direction.ASC, "ban");
        result = studentYearRepository.findAll(spec, sort);
        for (int i = 0; i < result.size(); i++) {
            // 「生徒ごとの１年分の成績リスト」
            ArrayList<Grade> studentGrade = new ArrayList<Grade>();
            for (int j = 0; j < 27; j++) {
                studentGrade.add(null);
            }
            // 生徒IDから成績を検索
            List<Grade> resultGrade = gradeRepository.findAllByStudentId(result.get(i).getStudentId());
            for (int k = 0; k < resultGrade.size(); k++) {
                // クラス登録年度から成績をしぼりこむ
                if (resultGrade.get(k).getYear() == year) {
                    for (int l = 1; l < 4; l++) { // 学期term
                        for (int m = 1; m < 10; m++) { // 教科subject
                            if (resultGrade.get(k).getTerm() == l && resultGrade.get(k).getSubject() == m) {
                                // 「生徒ごとの１年分の成績リスト」にセット
                                studentGrade.set((9 * l + m - 10), resultGrade.get(k));
                            }
                        }
                    }
                }
            }
            // 配列に追加
            gradeList.add(studentGrade);
        }
        return gradeList;
    }

    /** 成績一括更新 */
    public void update(String userId, GradeForm gradeForm) {
        List<Long> gradeIds = gradeForm.getGradeIds();
        List<String> studentIds = gradeForm.getStudentIds();
        List<Integer> years = gradeForm.getYears();
        List<Integer> terms = gradeForm.getTerms();
        List<Integer> subjects = gradeForm.getSubjects();
        List<Integer> ratings = gradeForm.getRatings();
        for (int i = 0; i < studentIds.size(); i++) {
            // 新規登録
            if ((gradeIds.size() != 0 && ratings.size() != 0 && gradeIds.get(i) == null && ratings.get(i) != null)
                    || (gradeIds.size() == 0 && ratings.size() != 0)) {
                Grade grade = new Grade();
                grade.setStudentId(studentIds.get(i));
                grade.setYear(years.get(i));
                grade.setTerm(terms.get(i));
                grade.setSubject(subjects.get(i));
                grade.setRating(ratings.get(i));
                grade.setCreatedBy(userId);
                grade.setUpdatedBy(userId);
                gradeRepository.save(grade);
            }
            // 更新
            if (gradeIds.size() != 0 && ratings.size() != 0 && gradeIds.get(i) != null && ratings.get(i) != null) {
                Grade updateGrade = gradeRepository.findById(gradeIds.get(i)).orElseThrow();
                updateGrade.setRating(ratings.get(i));
                updateGrade.setUpdatedBy(userId);
                gradeRepository.save(updateGrade);
            }
            // 削除
            if ((gradeIds.size() != 0 && ratings.size() != 0 && gradeIds.get(i) != null && ratings.get(i) == null)
                    || (gradeIds.size() != 0 && ratings.size() == 0)) {
                Grade deleteGrade = gradeRepository.findById(gradeIds.get(i)).orElseThrow();
                gradeRepository.delete(deleteGrade);
            }
        }
    }

}
