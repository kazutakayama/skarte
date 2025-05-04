package com.example.skarte.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.skarte.entity.Attendance;
import com.example.skarte.entity.Grade;
import com.example.skarte.entity.Karte;
import com.example.skarte.entity.Student;
import com.example.skarte.entity.StudentYear;
import com.example.skarte.form.StudentYearForm;
import com.example.skarte.repository.AttendanceRepository;
import com.example.skarte.repository.GradeRepository;
import com.example.skarte.repository.KarteRepository;
import com.example.skarte.repository.StudentRepository;
import com.example.skarte.repository.StudentYearRepository;
import com.example.skarte.specification.StudentSpecification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentsYearService {

    private final StudentYearRepository studentYearRepository;
    private final StudentRepository studentRepository;
    private final KarteRepository karteRepository;
    private final AttendanceRepository attendanceRepository;
    private final GradeRepository gradeRepository;
    private final StudentSpecification studentSpecification;

    /**
     * クラス全取得
     * 
     * @return
     */
    public List<StudentYear> findAll() {
//        return studentYearRepository.findByDeletedFalseOrderByUpdatedAtDesc();
        return studentYearRepository.findByOrderByUpdatedAtDesc();
    }

    /**
     * クラス1件取得
     * 
     * @param id
     * @return
     */
    public StudentYear findById(Long id) {
        return studentYearRepository.findById(id).orElseThrow();
    }

    /**
     * 生徒IDでリストを取得
     * 
     * @return
     */
    public List<StudentYear> findAllByStudentId(String studentId) {
        return studentYearRepository.findAllByStudentIdOrderByYearAsc(studentId);
    }

    /**
     * 生徒IDでリストを取得（リスト）
     * 
     * @return
     */
    public List<StudentYear> classList(String studentId) {
        List<StudentYear> classList = new ArrayList<StudentYear>();
        for (int i = 0; i < 3; i++) {
            classList.add(null);
        }
        List<StudentYear> result = studentYearRepository.findAllByStudentIdOrderByYearAsc(studentId);
        for (int i = 0; i < result.size(); i++) {
            if (result.get(i).getNen() == 1) {
                classList.set(0, result.get(i));
            }
            if (result.get(i).getNen() == 2) {
                classList.set(1, result.get(i));
            }
            if (result.get(i).getNen() == 3) {
                classList.set(2, result.get(i));
            }
        }
        return classList;
    }

    // 年度の各学年のクラス数
    public ArrayList<ArrayList<Long>> yearClassList(Long year) {
        ArrayList<ArrayList<Long>> yearClassList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            HashSet<Long> set = new HashSet<>();
            List<StudentYear> result = studentYearRepository.findAll(
                    Specification.where(studentSpecification.year(year)).and(studentSpecification.nen((long) i + 1)),
                    Sort.by(Sort.Direction.ASC, "kumi"));
            for (int j = 0; j < result.size(); j++) {
                set.add(result.get(j).getKumi());
            }
            ArrayList<Long> classList = new ArrayList<Long>(set);
            yearClassList.add(null);
            yearClassList.set(i, classList);
        }
        return yearClassList;
    }

    // 年度の各学年のクラスごとの登録済み生徒の人数
    public ArrayList<ArrayList<Long>> classStudentsRegistered(Long year) {
        ArrayList<ArrayList<Long>> classStudentsRegistered = new ArrayList<>();
        ArrayList<ArrayList<Long>> yearClassList = yearClassList(year);
        for (int i = 0; i < 3; i++) {
            ArrayList<Long> studentsCount = new ArrayList<>();
            ArrayList<Long> classList = yearClassList.get(i);
            for (int j = 0; j < classList.size(); j++) {
                List<StudentYear> result = studentYearRepository.findAll(
                        Specification.where(studentSpecification.year(year)).and(studentSpecification.nen((long) i + 1))
                                .and(studentSpecification.kumi(classList.get(j))),
                        Sort.by(Sort.Direction.ASC, "ban"));
                studentsCount.add((long) result.size());
            }
//            classStudentsRegistered.add(null);
//            classStudentsRegistered.set(i, studentsCount);
            classStudentsRegistered.add(studentsCount);
        }
        return classStudentsRegistered;
    }

    // 年度の各学年のクラスごとの在籍生徒（転出/卒業していない生徒）の人数
    public ArrayList<ArrayList<Long>> classStudentsExists(Long year) {
        ArrayList<ArrayList<Long>> classStudentsExists = new ArrayList<>();
        ArrayList<ArrayList<Long>> yearClassList = yearClassList(year);
        for (int i = 0; i < 3; i++) {
            ArrayList<Long> studentsCount = new ArrayList<>();
            ArrayList<Long> classList = yearClassList.get(i);
            for (int j = 0; j < classList.size(); j++) {
                List<StudentYear> result = studentYearRepository.findAll(
                        Specification.where(studentSpecification.year(year)).and(studentSpecification.nen((long) i + 1))
                                .and(studentSpecification.kumi(classList.get(j))),
                        Sort.by(Sort.Direction.ASC, "ban"));
//                for (int k = 0; k < result.size(); k++) {
//                    Student student = studentRepository.findByStudentId(result.get(k).getStudentId());
//                    if (student.isTransferred()) {
//                        result.remove(k);
//                    }
//                }
                // remove実行後、リストサイズが変わり、１つ前につめられてスキップされてしまうため、逆順ループの処理をおこなう
                for (int k = result.size() - 1; k >= 0; k--) {
                    Student student = studentRepository.findByStudentId(result.get(k).getStudentId());
                    if (student.isTransferred()) {
                        result.remove(k);
                    }
                }
                studentsCount.add((long) result.size());
            }
//            classStudentsTransferred.add(null);
//            classStudentsTransferred.set(i, studentsCount);
            classStudentsExists.add(studentsCount);
        }
        return classStudentsExists;
    }

    // 年度の各学年のクラスごとの転出/卒業済み生徒の人数
    public ArrayList<ArrayList<Long>> classStudentsTransferred(Long year) {
        ArrayList<ArrayList<Long>> classStudentsTransferred = new ArrayList<>();
        ArrayList<ArrayList<Long>> yearClassList = yearClassList(year);
        for (int i = 0; i < 3; i++) {
            ArrayList<Long> studentsCount = new ArrayList<>();
            ArrayList<Long> classList = yearClassList.get(i);
            for (int j = 0; j < classList.size(); j++) {
                List<StudentYear> result = studentYearRepository.findAll(
                        Specification.where(studentSpecification.year(year)).and(studentSpecification.nen((long) i + 1))
                                .and(studentSpecification.kumi(classList.get(j))),
                        Sort.by(Sort.Direction.ASC, "ban"));
//                for (int k = 0; k < result.size(); k++) {
//                    Student student = studentRepository.findByStudentId(result.get(k).getStudentId());
//                    if (student.isTransferred()) {
//                        result.remove(k);
//                    }
//                }
                // remove実行後、リストサイズが変わり、１つ前につめられてスキップされてしまうため、逆順ループの処理をおこなう
                for (int k = result.size() - 1; k >= 0; k--) {
                    Student student = studentRepository.findByStudentId(result.get(k).getStudentId());
                    if (!(student.isTransferred())) {
                        result.remove(k);
                    }
                }
                studentsCount.add((long) result.size());
            }
//            classStudentsTransferred.add(null);
//            classStudentsTransferred.set(i, studentsCount);
            classStudentsTransferred.add(studentsCount);
        }
        return classStudentsTransferred;
    }

    // 年度のクラスサマリー((登録1,2,3,計),(在籍1,2,3,計),(転出1,2,3,計))
    public ArrayList<ArrayList<Long>> classSummary(Long year) {
        ArrayList<ArrayList<Long>> classSummary = new ArrayList<>();
        // 登録生徒
        ArrayList<ArrayList<Long>> classStudentsRegistered = classStudentsRegistered(year);
        ArrayList<Long> rowSumsR = new ArrayList<>();
        long totalR = 0;
        for (List<Long> row : classStudentsRegistered) {
            long rowSum = 0;
            for (Long num : row) {
                rowSum += num;
            }
            rowSumsR.add(rowSum);
            totalR += rowSum;
        }
        rowSumsR.add(totalR);
        classSummary.add(rowSumsR);
        // 在籍生徒
        ArrayList<ArrayList<Long>> classStudentsExists = classStudentsExists(year);
        ArrayList<Long> rowSumsE = new ArrayList<>();
        long totalE = 0;
        for (List<Long> row : classStudentsExists) {
            long rowSum = 0;
            for (Long num : row) {
                rowSum += num;
            }
            rowSumsE.add(rowSum);
            totalE += rowSum;
        }
        rowSumsE.add(totalE);
        classSummary.add(rowSumsE);
        // 転出卒業生徒
        ArrayList<ArrayList<Long>> classStudentsTransferred = classStudentsTransferred(year);
        ArrayList<Long> rowSumsT = new ArrayList<>();
        long totalT = 0;
        for (List<Long> row : classStudentsTransferred) {
            long rowSum = 0;
            for (Long num : row) {
                rowSum += num;
            }
            rowSumsT.add(rowSum);
            totalT += rowSum;
        }
        rowSumsT.add(totalT);
        classSummary.add(rowSumsT);
        return classSummary;
    }

    // クラス検索
    public List<StudentYear> search(Long year, Long nen, Long kumi) {
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
        return result;
    }

    // クラスに登録できる候補の生徒のリストを取得
    public List<Student> studentsOption(Long year, Long nen) {
        // 1学年は1年分、2学年は2年分、3学年は3年分の生徒番号を一旦すべて取得
        List<Student> studentsOption = new ArrayList<>();
        String ichi = String.valueOf(year);
        String ni = String.valueOf(year - 1);
        String san = String.valueOf(year - 2);
        if (nen == 1) {
            studentsOption = studentRepository.findAll(Specification.where(studentSpecification.year(ichi)));
        }
        if (nen == 2) {
            studentsOption = studentRepository
                    .findAll(Specification.where(studentSpecification.year(ichi)).or(studentSpecification.year(ni)));
        }
        if (nen == 3) {
            studentsOption = studentRepository.findAll(Specification.where(studentSpecification.year(ichi))
                    .or(studentSpecification.year(ni)).or(studentSpecification.year(san)));
        }
        // 年度にすでに登録済みの生徒を取得する
        List<StudentYear> resultYear = studentYearRepository
                .findAll(Specification.where(studentSpecification.year(year)));
        List<Student> resultStudents = new ArrayList<>();
        for (int i = 0; i < resultYear.size(); i++) {
            Student student = studentRepository.findById(resultYear.get(i).getStudentId()).orElseThrow();
            resultStudents.add(student);
        }
        // リストの差分を取得する
        studentsOption.removeAll(resultStudents);
        // 転出済生徒を取り除く

        // 生徒番号昇順で並び替え
        studentsOption.sort(Comparator.comparing(Student::getStudentId));
        return studentsOption;
    }

    /**
     * クラス個別追加
     * 
     * @param studentYear
     * @return
     */
    public void add(String userId, StudentYearForm studentYearForm, Long year, Long nen, Long kumi) {
        // クラス在籍生徒の人数を数え、一番最後の番号をセットする
        List<StudentYear> result = studentYearRepository.findAll(Specification.where(studentSpecification.year(year))
                .and(studentSpecification.nen(nen)).and(studentSpecification.kumi(kumi)),
                Sort.by(Sort.Direction.ASC, "ban"));
        Long count = (long) result.size();
        StudentYear studentYear = new StudentYear();
        studentYear.setStudentId(studentYearForm.getStudentId());
        studentYear.setYear(year);
        studentYear.setNen(nen);
        studentYear.setKumi(kumi);
        studentYear.setBan(count + 1);
        studentYear.setCreatedBy(userId);
        studentYear.setUpdatedBy(userId);
        studentYearRepository.save(studentYear);
    }

    /**
     * クラス一括登録
     * 
     * @param studentYear
     * @return
     */
    public List<String> create(String userId, StudentYearForm studentYearForm, Long year, Long nen, Long kumi) {
        List<String> studentIds = studentYearForm.getStudentIds();
        if (studentIds != null) {
            // 一括追加生徒を番号1で一時的に登録する
            for (int i = 0; i < studentIds.size(); i++) {
                StudentYear studentYear = new StudentYear();
                studentYear.setStudentId(studentIds.get(i));
                studentYear.setYear(year);
                studentYear.setNen(nen);
                studentYear.setKumi(kumi);
                studentYear.setBan((long) 1);
                studentYear.setCreatedBy(userId);
                studentYear.setUpdatedBy(userId);
                studentYearRepository.save(studentYear);
            }
        }
        return studentIds;
    }

    // クラス在籍生徒を名前の順にソートし、出席番号を割り振って更新
    public void sort(Long year, Long nen, Long kumi, String userId) {
        int intYear = Integer.valueOf(year.toString());
        int intNen = Integer.valueOf(nen.toString());
        int intKumi = Integer.valueOf(kumi.toString());
        // クラスの在籍生徒<StudentYear>を取得
        List<StudentYear> studentsYear = studentYearRepository
                .findAll(Specification.where(studentSpecification.year(year)).and(studentSpecification.nen(nen))
                        .and(studentSpecification.kumi(kumi)));
        // クラスの在籍生徒<Student>を取得
        List<Student> students = new ArrayList<>();
        for (int i = 0; i < studentsYear.size(); i++) {
            Student student = studentRepository.findById(studentsYear.get(i).getStudentId()).orElseThrow();
            students.add(student);
        }
        // 名前の順 lastName → firstName の順にソート
        students.sort(Comparator.comparing(Student::getLastNameKana)
                .thenComparing(Comparator.comparing(Student::getFirstNameKana)));
        // studentIdから<StudentYear>をさがす
        for (int i = 0; i < students.size(); i++) {
            List<StudentYear> result = findAllByStudentId(students.get(i).getStudentId());
            for (int j = 0; j < result.size(); j++) {
                if (result.get(j).getYear() == intYear && result.get(j).getNen() == intNen
                        && result.get(j).getKumi() == intKumi) {
                    // 順番に出席番号を割り振って更新
                    StudentYear studentYear = result.get(j);
                    studentYear.setBan((long) i + 1);
                    studentYear.setUpdatedBy(userId);
                    studentYearRepository.save(studentYear);
                }
            }
        }
    }

    // クラス個別追加/一括登録時に重複しないように確認する
    public boolean isDuplicated(StudentYearForm studentYearForm, Long year, Long nen, Long kumi) {
        boolean isDuplicated = false;
        // 在籍生徒の生徒IDのリスト
        List<String> ids = new ArrayList<>();
        // 追加生徒の生徒IDのリスト
        List<String> newIds = new ArrayList<>();
        List<StudentYear> result = search(year, nen, kumi);
        for (int i = 0; i < result.size(); i++) {
            ids.add(result.get(i).getStudentId());
        }
        // 個別追加の場合
        if (studentYearForm.getStudentId() != null) {
            newIds.add(studentYearForm.getStudentId());
        }
        // 一括登録の場合
        List<String> studentIds = studentYearForm.getStudentIds();
        if (studentIds != null) {
            for (int i = 0; i < studentIds.size(); i++) {
                newIds.add(studentIds.get(i));
            }
        }
        // idsとnewIdsをHashSetに追加し、重複を確認する
        HashSet<String> set = new HashSet<>();
        for (int i = 0; i < ids.size(); i++) {
            set.add(ids.get(i));
        }
        for (int i = 0; i < newIds.size(); i++) {
            set.add(newIds.get(i));
        }
        if (ids.size() + newIds.size() != set.size()) {
            isDuplicated = true;
        }
        return isDuplicated;
    }

    // クラス在籍生徒を1名削除
    public void deleteClass(Long id) {
        StudentYear studentYear = studentYearRepository.findById(id).orElseThrow();
        studentYearRepository.delete(studentYear);
    }

//    // 画像をアップロード
//    public void upload(Long id, String userId, MultipartFile file) throws IOException {
//        StudentYear studentYear = studentYearRepository.findById(id).orElseThrow();
//        MultipartFile multipartFile = file; 
////        try {
////            byte[] bytes = multipartFile.getBytes();            
////        } catch (IOException e) {
////            e.printStackTrace();
////            byte[] bytes = null;
////        }
//        byte[] bytes = file.getBytes();
//        studentYear.setImage(bytes); 
//        studentYear.setUpdatedBy(userId);
//        studentYearRepository.save(studentYear);
//        
//    }

    // 写真を追加
    public void addImage(Long id, String userId, StudentYearForm studentYearForm) throws IOException {
        StudentYear studentYear = studentYearRepository.findById(id).orElseThrow();
        MultipartFile multipartFile = studentYearForm.getImage();
        byte[] bytes = multipartFile.getBytes();
        studentYear.setImage(bytes);
        studentYear.setUpdatedBy(userId);
        studentYearRepository.save(studentYear);
    }

    // 写真を削除
    public void deleteImage(Long id) {
        StudentYear studentYear = studentYearRepository.findById(id).orElseThrow();
        studentYear.setImage(null);
        studentYearRepository.save(studentYear);
    }

    // 写真byte[]をbase64に変換（1年分）
    public String image(Long id) {
        StudentYear studentYear = studentYearRepository.findById(id).orElseThrow();
        StringBuffer data = new StringBuffer();
        String image = Base64.getEncoder().encodeToString(studentYear.getImage());
        // 拡張子をjpegと指定 <img ht:src="">で指定できる形にする
        data.append("data:image/jpeg;base64,");
        data.append(image);
        return data.toString();
    }

    // 写真byte[]をbase64に変換（3年分）
    public List<String> images(String studentId) {
        List<String> images = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            images.add(null);
        }
        List<StudentYear> classList = classList(studentId);
        for (int i = 0; i < classList.size(); i++) {
            if (classList.get(i) != null) {
                if (classList.get(i).getImage() != null) {
                    StringBuffer data = new StringBuffer();
                    String image = Base64.getEncoder().encodeToString(classList.get(i).getImage());
                    // 拡張子をjpegと指定 <img ht:src="">で指定できる形にする
                    data.append("data:image/jpeg;base64,");
                    data.append(image);
                    images.set(i, data.toString());
                }
            }
        }
        return images;
    }

    // クラス全員分の写真byte[]をbase64に変換し、リストにする
    public List<String> imageList(List<StudentYear> result) {
        List<String> imageList = new ArrayList<>();
        for (int i = 0; i < result.size(); i++) {
            if (result.get(i).getImage() != null) {
                StringBuffer data = new StringBuffer();
                String image = Base64.getEncoder().encodeToString(result.get(i).getImage());
                // 拡張子をjpegと指定 <img ht:src="">で指定できる形にする
                data.append("data:image/jpeg;base64,");
                data.append(image);
                imageList.add(i, data.toString());
            } else {
                imageList.add(i, null);
            }
        }
        return imageList;
    }

//    // クラス在籍生徒をソートし、出席番号を割り振って更新
//    public void sort(Long year, Long nen, Long kumi, String userId) {
//        List<StudentYear> result = studentYearRepository.findAll(Specification.where(studentSpecification.year(year))
//                .and(studentSpecification.nen(nen)).and(studentSpecification.kumi(kumi)));
//        result.sort(Comparator.comparing(StudentYear::getStudentId));
//        for (int i = 0; i < result.size(); i++) {
//            StudentYear studentYear = studentYearRepository.findById(result.get(i).getStudentYearId()).orElseThrow();
//            studentYear.setBan((long) i + 1);
//            studentYear.setUpdatedBy(userId);
//            studentYearRepository.save(studentYear);
//        }
//    }

    // クラス在籍生徒が削除可能か判定
    // 紐づいたカルテ・出席簿・成績のデータがないかどうか確認
    public boolean dataExists(Long id) {
        StudentYear studentYear = studentYearRepository.findById(id).orElseThrow();
        int nendo = Integer.valueOf(studentYear.getYear().toString());
        // カルテの確認
        List<Karte> resultK = karteRepository.findAllByStudentIdOrderByDateDesc(studentYear.getStudentId());
        List<Karte> resultKarte = new ArrayList<>();
        for (int i = 0; i < resultK.size(); i++) {
            LocalDate localDate = resultK.get(i).getDate();
            if ((localDate.getYear() == nendo && localDate.getMonthValue() >= 4)
                    || (localDate.getYear() == nendo + 1 && localDate.getMonthValue() < 4)) {
                resultKarte.add(resultK.get(i));
            }
        }
        // 出席簿の確認
        List<Attendance> resultA = attendanceRepository.findAllByStudentId(studentYear.getStudentId());
        List<Attendance> resultAttendance = new ArrayList<>();
        for (int i = 0; i < resultA.size(); i++) {
            LocalDate localDate = resultA.get(i).getDate();
            if ((localDate.getYear() == nendo && localDate.getMonthValue() >= 4)
                    || (localDate.getYear() == nendo + 1 && localDate.getMonthValue() < 4)) {
                resultAttendance.add(resultA.get(i));
            }
        }
        // 成績の確認
        List<Grade> resultGrade = gradeRepository
                .findAll(Specification.where(studentSpecification.gradeYear(studentYear.getYear()))
                        .and(studentSpecification.gradeStudentId(studentYear.getStudentId())));
        boolean dataExists = false;
        // データがあればtrueを返す
        if (resultKarte.size() > 0 || resultAttendance.size() > 0 || resultGrade.size() > 0) {
            dataExists = true;
        }
        return dataExists;
    }

}