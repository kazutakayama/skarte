package com.example.skarte.service;

import java.io.BufferedReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.skarte.bean.StudentsCsv;
import com.example.skarte.entity.Attendance;
import com.example.skarte.entity.Grade;
import com.example.skarte.entity.Karte;
import com.example.skarte.entity.Student;
import com.example.skarte.entity.StudentYear;
import com.example.skarte.form.StudentForm;
import com.example.skarte.repository.AttendanceRepository;
import com.example.skarte.repository.GradeRepository;
import com.example.skarte.repository.KarteRepository;
import com.example.skarte.repository.StudentRepository;
import com.example.skarte.repository.StudentYearRepository;
import com.example.skarte.specification.StudentSpecification;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentsService {

    private final StudentsYearService studentsYearService;
    private final StudentRepository studentRepository;
    private final StudentYearRepository studentYearRepository;
    private final KarteRepository karteRepository;
    private final AttendanceRepository attendanceRepository;
    private final GradeRepository gradeRepository;

    /** 生徒全取得 */
    public List<Student> findAll() {
        return studentRepository.findByOrderByUpdatedAtDesc();
    }

    /** 生徒１件取得 */
    public Student findById(String id) {
        return studentRepository.findById(id).orElseThrow();
    }

    public Student findByStudentId(String id) {
        return studentRepository.findByStudentId(id);
    }

    /** 生徒検索（生徒名、年度） */
    @SuppressWarnings("removal")
    public List<Student> search(String name, String year) {
        List<Student> result;
        if ("".equals(name)) {
            // すべての生徒
            if ("0".equals(year)) {
                result = studentRepository.findAll(Sort.by(Sort.Direction.ASC, "studentId"));
                // 年度検索
            } else {
                result = studentRepository.findAll(Specification.where(StudentSpecification.year(year)),
                        Sort.by(Sort.Direction.ASC, "studentId"));
            }
        } else {
            // 名前検索
            if ("0".equals(year)) {
                result = studentRepository.findAll(Specification.where(StudentSpecification.search(name)),
                        Sort.by(Sort.Direction.ASC, "studentId"));
                // 年度・名前検索
            } else {
                result = studentRepository.findAll(
                        Specification.where(StudentSpecification.search(name)).and(StudentSpecification.year(year)),
                        Sort.by(Sort.Direction.ASC, "studentId"));
            }
        }
        return result;
    }

    /** クラス検索をしたあと、Studentのリストを取得 */
    public List<Student> classStudents(Long year, Long nen, Long kumi) {
        List<StudentYear> result = studentsYearService.search(year, nen, kumi);
        List<Student> students = new ArrayList<>();
        for (int i = 0; i < result.size(); i++) {
            Student student = studentRepository.findById(result.get(i).getStudentId()).orElseThrow();
            students.add(student);
        }
        return students;
    }

    /** 生徒追加 */
    public void add(String userId, StudentForm form) {
        Student student = new Student();
        student.setStudentId(form.getStudentId());
        student.setLastName(form.getLastName());
        student.setFirstName(form.getFirstName());
        student.setLastNameKana(form.getLastNameKana());
        student.setFirstNameKana(form.getFirstNameKana());
        student.setBirth(form.getBirth());
        student.setGender(form.getGender());
        student.setFamily1(form.getFamily1());
        student.setFamily2(form.getFamily2());
        student.setTel1(form.getTel1());
        student.setTel2(form.getTel2());
        student.setTel3(form.getTel3());
        student.setTel4(form.getTel4());
        student.setPostalCode(form.getPostalCode());
        student.setAdress(form.getAdress());
        student.setMemo(form.getMemo());
        student.setCreatedBy(userId);
        student.setUpdatedBy(userId);
        studentRepository.save(student);
    }

    /** 生徒更新 */
    public void update(String studentId, StudentForm form, String userId) {
        Student student = studentRepository.findById(studentId).orElseThrow();
        student.setLastName(form.getLastName());
        student.setFirstName(form.getFirstName());
        student.setLastNameKana(form.getLastNameKana());
        student.setFirstNameKana(form.getFirstNameKana());
        student.setBirth(form.getBirth());
        student.setGender(form.getGender());
        student.setFamily1(form.getFamily1());
        student.setFamily2(form.getFamily2());
        student.setTel1(form.getTel1());
        student.setTel2(form.getTel2());
        student.setTel3(form.getTel3());
        student.setTel4(form.getTel4());
        student.setPostalCode(form.getPostalCode());
        student.setAdress(form.getAdress());
        student.setMemo(form.getMemo());
        student.setTransferred(form.isTransferred());
        student.setUpdatedBy(userId);
        studentRepository.save(student);
    }

    /** /setting クラスで生徒を絞り込んでCSVダウンロード */
    public Object download(String name, String year) throws JsonProcessingException {
        List<Student> students = search(name, year);
        List<StudentsCsv> csvs = students.stream()
                .map(e -> new StudentsCsv(e.getStudentId(), e.getLastName(), e.getFirstName(), e.getLastNameKana(),
                        e.getFirstNameKana(), e.getBirth(), e.getGender(), e.getFamily1(), e.getFamily2(), e.getTel1(),
                        e.getTel2(), e.getTel3(), e.getTel4(), e.getPostalCode(), e.getAdress(), e.getMemo()))
                .collect(Collectors.toList());
        // ファイルをダウンロード
        CsvMapper mapper = new CsvMapper();
        // 日付をフォーマット
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        mapper.registerModule(javaTimeModule);
        
        CsvSchema schema = mapper.schemaFor(StudentsCsv.class).withHeader();
        return mapper.writer(schema).writeValueAsString(csvs);
    }

    /** /students クラスで生徒を絞り込んでCSVダウンロード */
    public Object downloadClass(Long year, Long nen, Long kumi) throws JsonProcessingException {
        List<Student> students = classStudents(year, nen, kumi);
        List<StudentsCsv> csvs = students.stream()
                .map(e -> new StudentsCsv(e.getStudentId(), e.getLastName(), e.getFirstName(), e.getLastNameKana(),
                        e.getFirstNameKana(), e.getBirth(), e.getGender(), e.getFamily1(), e.getFamily2(), e.getTel1(),
                        e.getTel2(), e.getTel3(), e.getTel4(), e.getPostalCode(), e.getAdress(), e.getMemo()))
                .collect(Collectors.toList());
        // ファイルをダウンロード
        CsvMapper mapper = new CsvMapper();
        // 日付をフォーマット
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        mapper.registerModule(javaTimeModule);
        
        CsvSchema schema = mapper.schemaFor(StudentsCsv.class).withHeader();
        return mapper.writer(schema).writeValueAsString(csvs);
    }

    /** オブジェクトのバリデーション（CSVアップロード用） */
    public int validation(Object object) {
        // バリデーターを取得
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        // バリデーションの実行
        Set<ConstraintViolation<Object>> result = validator.validate(object);
        // エラーがなければ0を返す
        return result.size();
    }

    /** 生徒CSVアップロード */
    public List<Student> upload(String userId, BufferedReader br) throws Exception {
        // 「登録に成功した生徒のリスト」
        List<Student> studentList = new ArrayList<>();
        // 「登録待ち生徒のリスト」
        List<StudentForm> studentFormList = new ArrayList<>();
        // 「エラー生徒のリスト」
        List<StudentForm> errorList = new ArrayList<>();
        String line;
        // ヘッダーレコードをとばすためにあらかじめ１行だけ読み取っておく
        line = br.readLine();
        // 行がNULL（CSVの値がなくなる）になるまで処理を繰り返す
        while ((line = br.readLine()) != null) {
            // 負の数字を引数に指定し、中身が空でも、全ての要素を取得
            String[] split = line.split(",", -1);
            StudentForm form = StudentForm.builder().studentId(split[0]).lastName(split[1]).firstName(split[2])
                    .lastNameKana(split[3]).firstNameKana(split[4])
                    .birth(LocalDate.parse(split[5], DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                    .gender((int) Integer.parseInt(split[6])).family1(split[7]).family2(split[8]).tel1(split[9])
                    .tel2(split[10]).tel3(split[11]).tel4(split[12]).postalCode(split[13]).adress(split[14])
                    .memo(split[15]).build();
            // バリデーション
            int result = validation(form);
            // エラーがなければresultが0 && 生徒IDがすでに登録済みでないか確認 → 「登録待ち生徒のリスト」に追加
            if (result == 0 && findByStudentId(form.getStudentId()) == null) {
                studentFormList.add(form);
                // エラーがあった場合、「エラー生徒のリスト」に追加
            } else {
                errorList.add(form);
            }
        }
        // ここまでひとりもエラーがなければ、新規登録生徒どうしの生徒IDの重複チェックを行う
        if (errorList.size() == 0) {
            // データの重複を許さないHashSetに生徒IDを追加する
            HashSet<String> set = new HashSet<>();
            for (int i = 0; i < studentFormList.size(); i++) {
                set.add(studentFormList.get(i).getStudentId());
            }
            // 重複がない場合、新規登録する
            if (studentFormList.size() == set.size()) {
                for (int i = 0; i < studentFormList.size(); i++) {
                    Student student = new Student();
                    student.setStudentId(studentFormList.get(i).getStudentId());
                    student.setLastName(studentFormList.get(i).getLastName());
                    student.setFirstName(studentFormList.get(i).getFirstName());
                    student.setLastNameKana(studentFormList.get(i).getLastNameKana());
                    student.setFirstNameKana(studentFormList.get(i).getFirstNameKana());
                    student.setBirth(studentFormList.get(i).getBirth());
                    student.setGender(studentFormList.get(i).getGender());
                    student.setFamily1(studentFormList.get(i).getFamily1());
                    student.setFamily2(studentFormList.get(i).getFamily2());
                    student.setTel1(studentFormList.get(i).getTel1());
                    student.setTel2(studentFormList.get(i).getTel2());
                    student.setTel3(studentFormList.get(i).getTel3());
                    student.setTel4(studentFormList.get(i).getTel4());
                    student.setPostalCode(studentFormList.get(i).getPostalCode());
                    student.setAdress(studentFormList.get(i).getAdress());
                    student.setMemo(studentFormList.get(i).getMemo());
                    student.setCreatedBy(userId);
                    student.setUpdatedBy(userId);
                    studentRepository.save(student);
                    studentList.add(student);
                }
            }
        }
        return studentList;
    }
     
    /** 3年生クラスを卒業登録する */
    public void graduated(String userId, Long year, Long nen, Long kumi) {
        List<StudentYear> result = studentsYearService.search(year, nen, kumi);
        if (result.size()>0) {
            for (int i = 0; i < result.size(); i++) {
                Student student = findByStudentId(result.get(i).getStudentId());
                student.setTransferred(true);
                student.setUpdatedBy(userId);
                studentRepository.save(student);                
            }
        }
    }

    /** 生徒が削除可能か判定する（生徒IDに紐づけられたクラス、カルテ、出席簿、成績のデータがあるかどうか確認） */
    public boolean dataExists(String id) {
        boolean dataExists = false;
        List<StudentYear> studentYear = studentYearRepository.findAllByStudentIdOrderByYearAsc(id);
        List<Karte> karte = karteRepository.findAllByStudentIdOrderByDateDesc(id);
        List<Attendance> attendance = attendanceRepository.findAllByStudentId(id);
        List<Grade> grade = gradeRepository.findAllByStudentId(id);
        if (studentYear.size() > 0 || karte.size() > 0 || attendance.size() > 0 || grade.size() > 0) {
            dataExists = true;
        }
        return dataExists;
    }

    /** 生徒削除 */
    public void delete(String id) {
        Student student = studentRepository.findById(id).orElseThrow();
        studentRepository.delete(student);
    }
}