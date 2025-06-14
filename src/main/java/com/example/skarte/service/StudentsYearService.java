package com.example.skarte.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

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

    /** クラス全取得 */
    public List<StudentYear> findAll() {
        return studentYearRepository.findByOrderByUpdatedAtDesc();
    }

    /** クラス1件取得 */
    public StudentYear findById(Long id) {
        return studentYearRepository.findById(id).orElseThrow();
    }

    /** 生徒IDでリストを取得 */
    public List<StudentYear> findAllByStudentId(String studentId) {
        return studentYearRepository.findAllByStudentIdOrderByYearAsc(studentId);
    }

    /** 生徒IDでリストを取得（リスト） */
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

    /** 年度の各学年のクラス数 */
    public ArrayList<ArrayList<Integer>> yearClassList(int year) {
        ArrayList<ArrayList<Integer>> yearClassList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            HashSet<Integer> set = new HashSet<>();
            List<StudentYear> result = search(year, i + 1, 0);
            for (int j = 0; j < result.size(); j++) {
                set.add(result.get(j).getKumi());
            }
            ArrayList<Integer> classList = new ArrayList<Integer>(set);
            yearClassList.add(null);
            yearClassList.set(i, classList);
        }
        return yearClassList;
    }

    /** 年度の各学年のクラスごとの登録済み生徒の人数 */
    public ArrayList<ArrayList<Integer>> classStudentsRegistered(int year) {
        ArrayList<ArrayList<Integer>> classStudentsRegistered = new ArrayList<>();
        ArrayList<ArrayList<Integer>> yearClassList = yearClassList(year);
        for (int i = 0; i < 3; i++) {
            ArrayList<Integer> studentsCount = new ArrayList<>();
            ArrayList<Integer> classList = yearClassList.get(i);
            for (int j = 0; j < classList.size(); j++) {
                List<StudentYear> result = search(year, i + 1, classList.get(j));
                studentsCount.add(result.size());
            }
            classStudentsRegistered.add(studentsCount);
        }
        return classStudentsRegistered;
    }

    /** 年度の各学年のクラスごとの在籍生徒（転出/卒業していない生徒）の人数 */
    public ArrayList<ArrayList<Integer>> classStudentsExists(int year) {
        ArrayList<ArrayList<Integer>> classStudentsExists = new ArrayList<>();
        ArrayList<ArrayList<Integer>> yearClassList = yearClassList(year);
        for (int i = 0; i < 3; i++) {
            ArrayList<Integer> studentsCount = new ArrayList<>();
            ArrayList<Integer> classList = yearClassList.get(i);
            for (int j = 0; j < classList.size(); j++) {
                List<StudentYear> result = search(year, i + 1, classList.get(j));
                // remove実行後、リストサイズが変わり、１つ前につめられてスキップされてしまうため、逆順ループの処理をおこなう
                for (int k = result.size() - 1; k >= 0; k--) {
                    Student student = studentRepository.findByStudentId(result.get(k).getStudentId());
                    if (student.isTransferred()) {
                        result.remove(k);
                    }
                }
                studentsCount.add(result.size());
            }
            classStudentsExists.add(studentsCount);
        }
        return classStudentsExists;
    }

    /** 年度の各学年のクラスごとの転出/卒業済み生徒の人数 */
    public ArrayList<ArrayList<Integer>> classStudentsTransferred(int year) {
        ArrayList<ArrayList<Integer>> classStudentsTransferred = new ArrayList<>();
        ArrayList<ArrayList<Integer>> yearClassList = yearClassList(year);
        for (int i = 0; i < 3; i++) {
            ArrayList<Integer> studentsCount = new ArrayList<>();
            ArrayList<Integer> classList = yearClassList.get(i);
            for (int j = 0; j < classList.size(); j++) {
                List<StudentYear> result = search(year, i + 1, classList.get(j));
                // remove実行後、リストサイズが変わり、１つ前につめられてスキップされてしまうため、逆順ループの処理をおこなう
                for (int k = result.size() - 1; k >= 0; k--) {
                    Student student = studentRepository.findByStudentId(result.get(k).getStudentId());
                    if (!(student.isTransferred())) {
                        result.remove(k);
                    }
                }
                studentsCount.add(result.size());
            }
            classStudentsTransferred.add(studentsCount);
        }
        return classStudentsTransferred;
    }

    /** 年度のクラスサマリー((登録1,2,3,計),(在籍1,2,3,計),(転出1,2,3,計)) */
    public ArrayList<ArrayList<Integer>> classSummary(int year) {
        ArrayList<ArrayList<Integer>> classSummary = new ArrayList<>();
        // 登録生徒
        ArrayList<ArrayList<Integer>> classStudentsRegistered = classStudentsRegistered(year);
        ArrayList<Integer> rowSumsR = new ArrayList<>();
        int totalR = 0;
        for (List<Integer> row : classStudentsRegistered) {
            int rowSum = 0;
            for (Integer num : row) {
                rowSum += num;
            }
            rowSumsR.add(rowSum);
            totalR += rowSum;
        }
        rowSumsR.add(totalR);
        classSummary.add(rowSumsR);
        // 在籍生徒
        ArrayList<ArrayList<Integer>> classStudentsExists = classStudentsExists(year);
        ArrayList<Integer> rowSumsE = new ArrayList<>();
        int totalE = 0;
        for (List<Integer> row : classStudentsExists) {
            int rowSum = 0;
            for (Integer num : row) {
                rowSum += num;
            }
            rowSumsE.add(rowSum);
            totalE += rowSum;
        }
        rowSumsE.add(totalE);
        classSummary.add(rowSumsE);
        // 転出卒業生徒
        ArrayList<ArrayList<Integer>> classStudentsTransferred = classStudentsTransferred(year);
        ArrayList<Integer> rowSumsT = new ArrayList<>();
        int totalT = 0;
        for (List<Integer> row : classStudentsTransferred) {
            int rowSum = 0;
            for (Integer num : row) {
                rowSum += num;
            }
            rowSumsT.add(rowSum);
            totalT += rowSum;
        }
        rowSumsT.add(totalT);
        classSummary.add(rowSumsT);
        return classSummary;
    }

    /** クラス検索（登録生徒） */
    public List<StudentYear> search(int year, int nen, int kumi) {
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
        // 組が指定されていないときは組・番でソート、組が指定されているときは番でソート
        Sort sort = (kumi == 0) ? Sort.by(Sort.Direction.ASC, "kumi", "ban")
                : Sort.by(Sort.Direction.ASC, "ban");
        return studentYearRepository.findAll(spec, sort);
    }

    /** クラス検索（転出/卒業生徒） */
    public List<StudentYear> transferred(int year, int nen, int kumi) {
        List<StudentYear> transferred = new ArrayList<>();
        List<StudentYear> result = search(year, nen, kumi);
        for (int i = 0; i < result.size(); i++) {
            Student student = studentRepository.findByStudentId(result.get(i).getStudentId());
            if (student.isTransferred()) {
                transferred.add(result.get(i));
            }
        }
        return transferred;
    }

    /** クラス検索（在籍生徒） */
    public List<StudentYear> exists(int year, int nen, int kumi) {
        List<StudentYear> exists = new ArrayList<>();
        List<StudentYear> result = search(year, nen, kumi);
        for (int i = 0; i < result.size(); i++) {
            Student student = studentRepository.findByStudentId(result.get(i).getStudentId());
            if (!(student.isTransferred())) {
                exists.add(result.get(i));
            }
        }
        return exists;
    }

    /** クラスに登録できる候補の生徒のリストを取得 */
    public List<Student> studentsOption(int year, int nen) {
        // 1学年は1年分、2学年は2年分、3学年は3年分の生徒番号を一旦すべて取得
        List<Student> studentsOption = new ArrayList<>();
        String ichi = String.valueOf(year);
        String ni = String.valueOf(year - 1);
        String san = String.valueOf(year - 2);
        if (nen == 1) {
            studentsOption = studentRepository.findAll((root, query, cb) -> cb.like(root.get("studentId"), ichi + "%"));
        } else if (nen == 2) {
            studentsOption = studentRepository.findAll((root, query, cb) -> cb
                    .or(cb.like(root.get("studentId"), ichi + "%"), cb.like(root.get("studentId"), ni + "%")));
        } else if (nen == 3) {
            studentsOption = studentRepository
                    .findAll((root, query, cb) -> cb.or(cb.like(root.get("studentId"), ichi + "%"),
                            cb.like(root.get("studentId"), ni + "%"), cb.like(root.get("studentId"), san + "%")));
        }
        // 年度にすでに登録済みの生徒を取得する
        List<StudentYear> resultYear = studentYearRepository
                .findAll((root, query, cb) -> cb.equal(root.get("year"), year));
        List<Student> resultStudents = new ArrayList<>();
        for (int i = 0; i < resultYear.size(); i++) {
            Student student = studentRepository.findById(resultYear.get(i).getStudentId()).orElseThrow();
            resultStudents.add(student);
        }
        // リストの差分を取得する
        studentsOption.removeAll(resultStudents);
        // 転出済生徒を取り除く
        for (int i = studentsOption.size() - 1; i >= 0; i--) {
            Student student = studentRepository.findByStudentId(studentsOption.get(i).getStudentId());
            if (student.isTransferred()) {
                studentsOption.remove(i);
            }
        }
        // 生徒番号昇順で並び替え
        studentsOption.sort(Comparator.comparing(Student::getStudentId));
        return studentsOption;
    }

    /** クラス個別追加 */
    public void add(String userId, StudentYearForm studentYearForm, int year, int nen, int kumi) {
        // クラス在籍生徒の人数を数え、一番最後の番号をセットする
        List<StudentYear> result = search(year, nen, kumi);
        int count = result.size();
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

    /** クラス一括登録 */
    public List<String> create(String userId, StudentYearForm studentYearForm, int year, int nen, int kumi) {
        List<String> studentIds = studentYearForm.getStudentIds();
        if (studentIds != null) {
            // 一括追加生徒を番号1で一時的に登録する
            for (int i = 0; i < studentIds.size(); i++) {
                StudentYear studentYear = new StudentYear();
                studentYear.setStudentId(studentIds.get(i));
                studentYear.setYear(year);
                studentYear.setNen(nen);
                studentYear.setKumi(kumi);
                studentYear.setBan(1);
                studentYear.setCreatedBy(userId);
                studentYear.setUpdatedBy(userId);
                studentYearRepository.save(studentYear);
            }
        }
        return studentIds;
    }

    /** クラス在籍生徒を名前の順にソートし、出席番号を割り振って更新 */
    public void sort(int year, int nen, int kumi, String userId) {
        // クラスの在籍生徒<StudentYear>を取得
        List<StudentYear> studentsYear = search(year, nen, kumi);
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
                if (result.get(j).getYear() == year && result.get(j).getNen() == nen
                        && result.get(j).getKumi() == kumi) {
                    // 順番に出席番号を割り振って更新
                    StudentYear studentYear = result.get(j);
                    studentYear.setBan(i + 1);
                    studentYear.setUpdatedBy(userId);
                    studentYearRepository.save(studentYear);
                }
            }
        }
    }

    /** クラス個別追加/一括登録時に重複しないように確認する */
    public boolean isDuplicated(StudentYearForm studentYearForm, int year, int nen, int kumi) {
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

    /** クラス在籍生徒を1名削除 */
    public void deleteClass(Long id) {
        StudentYear studentYear = studentYearRepository.findById(id).orElseThrow();
        studentYearRepository.delete(studentYear);
    }

    /** 写真を追加 */
    public void addImage(Long id, String userId, StudentYearForm studentYearForm) throws IOException {
        StudentYear studentYear = studentYearRepository.findById(id).orElseThrow();
        MultipartFile multipartFile = studentYearForm.getImage();
        byte[] bytes = multipartFile.getBytes();
        studentYear.setImage(bytes);
        studentYear.setUpdatedBy(userId);
        studentYearRepository.save(studentYear);
    }

    /** 写真を削除 */
    public void deleteImage(Long id) {
        StudentYear studentYear = studentYearRepository.findById(id).orElseThrow();
        studentYear.setImage(null);
        studentYearRepository.save(studentYear);
    }

    /** 写真byte[]をbase64に変換（1年分） */
    public String image(Long id) {
        StudentYear studentYear = studentYearRepository.findById(id).orElseThrow();
        StringBuffer data = new StringBuffer();
        String image = Base64.getEncoder().encodeToString(studentYear.getImage());
        // 拡張子をjpegと指定 <img ht:src="">で指定できる形にする
        data.append("data:image/jpeg;base64,");
        data.append(image);
        return data.toString();
    }

    /** 写真byte[]をbase64に変換（3年分） */
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

    /** クラス全員分の写真byte[]をbase64に変換し、リストにする */
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

    /** クラス在籍生徒が削除可能か判定（紐づいたカルテ・出席簿・成績のデータがないかどうか確認） */
    public boolean dataExists(Long id) {
        StudentYear studentYear = studentYearRepository.findById(id).orElseThrow();
        int nendo = studentYear.getYear();
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
                .findAll((root, query, cb) -> cb.and(cb.equal(root.get("year"), studentYear.getYear()),
                        cb.equal(root.get("studentId"), studentYear.getStudentId())));
        boolean dataExists = false;
        // データがあればtrueを返す
        if (resultKarte.size() > 0 || resultAttendance.size() > 0 || resultGrade.size() > 0) {
            dataExists = true;
        }
        return dataExists;
    }

}