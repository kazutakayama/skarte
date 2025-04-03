package com.example.skarte.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.skarte.entity.Attendance;
import com.example.skarte.entity.Grade;
import com.example.skarte.entity.Karte;
import com.example.skarte.entity.StudentYear;
import com.example.skarte.form.GradeForm;
import com.example.skarte.form.StudentYearForm;
import com.example.skarte.repository.AttendanceRepository;
import com.example.skarte.repository.GradeRepository;
import com.example.skarte.repository.StudentYearRepository;
import com.example.skarte.specification.StudentSpecification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GradeService {

    private final GradeRepository gradeRepository;
    private final StudentYearRepository studentYearRepository;
    private final StudentSpecification studentSpecification;

//    @Autowired
//    GradeRepository gradeRepository;
//
//    StudentYearRepository studentYearRepository;
//
//    StudentSpecification studentSpecification;

    /**
     * 成績全取得
     * 
     * @return
     */
    public List<Grade> findAll() {
        return gradeRepository.findByOrderByUpdatedAtDesc();
    }

    /**
     * 成績1件取得
     * 
     * @param id
     * @return
     */
    public Grade findById(Long id) {
        return gradeRepository.findById(id).orElseThrow();
    }

    /**
     * 生徒IDでリストを取得
     * 
     * @return
     */
    public List<Grade> findAllByStudentId(String studentId) {
        return gradeRepository.findAllByStudentId(studentId);
    }

//    /**
//     * 生徒IDでリストを取得お試し！！！！！
//     * 
//     * @return
//     */
//    public List<StudentYear> findByStudentId(String studentId) {
//        return studentYearRepository.findAllByStudentIdOrderByYearAsc(studentId);
//    }

//    // クラス検索
//    public List<StudentYear> search(Long year, Long nen, Long kumi) {
//        List<StudentYear> result;
//        if (kumi == 0) {
//            result = studentYearRepository.findAll(
//                    Specification.where(studentSpecification.year(year)).and(studentSpecification.nen(nen)),
//                    Sort.by(Sort.Direction.ASC, "kumi", "ban"));
//        } else {
//            result = studentYearRepository.findAll(Specification.where(studentSpecification.year(year))
//                    .and(studentSpecification.nen(nen)).and(studentSpecification.kumi(kumi)),
//                    Sort.by(Sort.Direction.ASC, "ban"));
//        }
//        return result;
//    }

    /**
     * 年度でリストを取得
     * 
     * @return
     */
    public List<Grade> year(Long year) {
        return gradeRepository.findAllByYear(year);
    }

//    /**
//     * 成績一覧用（一年分の生徒全員の成績）　書き換える！！
//     * 
//     * @return
//     */
//    public List<Grade> allGrade(Long year, GradeForm gradeForm, String studentId, Long year, Long nen, Long kumi) {
//        ArrayList<ArrayList<Grade>> allGrade = new ArrayList<>();
//        List<StudentYear> yearResult;
//        if (kumi == 0) {
//            yearResult = studentYearRepository.findAll(
//                    Specification.where(studentSpecification.year(year)).and(studentSpecification.nen(nen)),
//                    Sort.by(Sort.Direction.ASC, "kumi", "ban"));
//        } else {
//            yearResult = studentYearRepository.findAll(Specification.where(studentSpecification.year(year))
//                    .and(studentSpecification.nen(nen)).and(studentSpecification.kumi(kumi)),
//                    Sort.by(Sort.Direction.ASC, "ban"));
//        }
//        for (int i = 0; i < yearResult.size(); i++) {
//            gradeRepository.findAllByStudentId(yearResult.get(i).getStudentId());
//            
//
//        }
//
//        return allGrade;
//    }

    /**
     * 生徒ごとの１年分の成績リストを取得
     * 
     * @return
     */
    public List<Grade> studentGrade(String studentId, Long year) {
        List<Grade> studentGrade = new ArrayList<Grade>();
        for (int i = 0; i < 27; i++) {
            studentGrade.add(null);
        }
        long nendo = year; // これが必要
        List<Grade> result = gradeRepository.findAllByStudentId(studentId);
        for (int i = 0; i < result.size(); i++) {
            if (result.get(i).getYear() == nendo) {
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

    /**
     * 生徒ごとの3年分の成績リストを取得
     * 
     * @return
     */
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
            long nendo = studentYear.get(i).getYear(); // これが必要
            // 生徒IDから成績を検索
            List<Grade> result = gradeRepository.findAllByStudentId(studentId);
            for (int k = 0; k < result.size(); k++) {
                // クラス登録年度から成績をしぼりこむ
//                if (result.get(k).getYear() == studentYear.get(i).getYear()) {
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

    /**
     * 成績リスト（クラス（学年）ごとの1年分）を取得
     * 
     * @return
     */
    public ArrayList<ArrayList<Grade>> gradeList(Long year, Long nen, Long kumi) {
        ArrayList<ArrayList<Grade>> gradeList = new ArrayList<>();

        // 年度、年、組からクラスの生徒リストを取得
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
        for (int i = 0; i < result.size(); i++) {
            // 「生徒ごとの１年分の成績リスト」
            ArrayList<Grade> studentGrade = new ArrayList<Grade>();
            for (int j = 0; j < 27; j++) {
                studentGrade.add(null);
            }
            long nendo = year; // これが必要
            // 生徒IDから成績を検索
            List<Grade> resultGrade = gradeRepository.findAllByStudentId(result.get(i).getStudentId());
            for (int k = 0; k < resultGrade.size(); k++) {
                // クラス登録年度から成績をしぼりこむ
//                if (result.get(k).getYear() == studentYear.get(i).getYear()) {
                if (resultGrade.get(k).getYear() == nendo) {
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

    /**
     * 成績追加
     * 
     * @param grade
     * @return
     */
    public void addGrade(String userId, String studentId, Grade grade) {
        grade.setStudentId(grade.getStudentId());
        grade.setCreatedBy(userId);
        grade.setUpdatedBy(userId);
        gradeRepository.save(grade);
    }

    /**
     * 成績更新
     * 
     * @param grade
     * @return
     */
    public Grade updateGradeOne(Long gradeId, String studentId, Grade grade) {
        grade.setStudentId(grade.getStudentId());
        Grade targetGrade = gradeRepository.findById(gradeId).orElseThrow();
        targetGrade.setYear(grade.getYear());
        targetGrade.setTerm(grade.getTerm());
        targetGrade.setSubject(grade.getSubject());
        targetGrade.setRating(grade.getRating());
        targetGrade.setUpdatedBy(grade.getUpdatedBy());
        gradeRepository.save(targetGrade);
        return targetGrade;
    }

    /**
     * 成績一括更新
     * 
     * @param grade
     * @return
     */
    public void updateGrade(String userId, GradeForm gradeForm) {
        List<Long> gradeIds = gradeForm.getGradeIds();
        List<String> studentIds = gradeForm.getStudentIds();
        List<Long> years = gradeForm.getYears();
        List<Long> terms = gradeForm.getTerms();
        List<Long> subjects = gradeForm.getSubjects();
        List<Long> ratings = gradeForm.getRatings();
        for (int i = 0; i < studentIds.size(); i++) {
            // 新規登録
            if (gradeIds.get(i) == null && ratings.get(i) != null) {
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
            if (gradeIds.get(i) != null && ratings.get(i) != null) {
                Grade updateGrade = gradeRepository.findById(gradeIds.get(i)).orElseThrow();
                updateGrade.setRating(ratings.get(i));
                updateGrade.setUpdatedBy(userId);
                gradeRepository.save(updateGrade);
            }
            // 削除
            if (gradeIds.get(i) != null && ratings.get(i) == null) {
                Grade deleteGrade = gradeRepository.findById(gradeIds.get(i)).orElseThrow();
                gradeRepository.delete(deleteGrade);

            }
        }
    }

    /**
     * 成績削除
     * 
     * @param grade
     */
    public Grade deleteGrade(Long gradeId, String studentId, Grade grade) {
        grade.setStudentId(grade.getStudentId());
        Grade deleteGrade = gradeRepository.findById(gradeId).orElseThrow();
        gradeRepository.delete(deleteGrade);
        return deleteGrade;
    }

//    /**
//     * 成績CSVダウンロード用
//     * 
//     * @param grade
//     */
//    public void insertGrade(Grade grade) {
//        gradeRepository.save(grade);
//    }

//  /**
//   * 成績削除（理論削除）
//   * 
//   * @param grade
//   */
//  public void deleteGrade(Long gradeId, Grade g);

}

//package com.example.skarte.service;
//
//import java.util.List;
//
//import com.example.skarte.entity.Grade;
//
////インターフェイス
//public interface GradeService {
//
//  /**
//   * 成績全取得
//   * 
//   * @return
//   */
//  public List<Grade> findAll();
//  
//  /**
//   * 生徒IDでリストを取得
//   * 
//   * @return
//   */
//  public List<Grade> findAllByStudentId(Long studentId);
//
//  /**
//   * 成績1件取得
//   * 
//   * @param id
//   * @return
//   */
//  public Grade findById(Long id);
//
//  /**
//   * 成績追加
//   * 
//   * @param grade
//   * @return
//   */
//  public void addGrade(Grade grade);
//
//  /**
//   * 成績編集
//   * 
//   * @param grade
//   * @return
//   */
//  public Grade updateGrade(Long gradeId, Grade grade);
//
//  /**
//   * 成績CSVダウンロード用
//   * 
//   * @param grade
//   */
//  public void insertGrade(Grade grade);
//
////  /**
////   * 成績削除（理論削除）
////   * 
////   * @param grade
////   */
////  public void deleteGrade(Long gradeId, Grade g);
//
//}
