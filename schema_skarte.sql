DROP TABLE IF EXISTS users;
CREATE TABLE IF NOT EXISTS users (
 user_id VARCHAR(7) NOT NULL,
 authority VARCHAR(255),
 password VARCHAR(255),
 last_name VARCHAR(255),
 first_name VARCHAR(255), 
 created_by VARCHAR(7) NOT NULL,
 updated_by VARCHAR(7) NOT NULL,
 created_at TIMESTAMP NULL,
 updated_at TIMESTAMP NULL,
 deleted bool NOT NULL,
 PRIMARY KEY (user_id)
);

DROP TABLE IF EXISTS notices;
CREATE TABLE IF NOT EXISTS notices (
 notice_id SERIAL NOT NULL,
 title TEXT NOT NULL,
 contents TEXT,
 created_by VARCHAR(7) NOT NULL,
 updated_by VARCHAR(7) NOT NULL,
 created_at TIMESTAMP NULL,
 updated_at TIMESTAMP NULL,
 PRIMARY KEY (notice_id)
);

DROP TABLE IF EXISTS students;
CREATE TABLE IF NOT EXISTS students (
 student_id VARCHAR(7) NOT NULL,
 last_name VARCHAR(255) NOT NULL,
 first_name VARCHAR(255) NOT NULL,
 last_name_kana VARCHAR(255) NOT NULL,
 first_name_kana VARCHAR(255) NOT NULL,
 birth DATE NOT NULL,
 gender INT NOT NULL,
 family1 VARCHAR(255),
 family2 VARCHAR(255),
 tel1 VARCHAR(255),
 tel2 VARCHAR(255),
 tel3 VARCHAR(255),
 tel4 VARCHAR(255),
 postal_code VARCHAR(255),
 adress VARCHAR(1000),
 memo TEXT,
 created_by VARCHAR(7) NOT NULL,
 updated_by VARCHAR(7) NOT NULL,
 created_at TIMESTAMP NOT NULL,
 updated_at TIMESTAMP NOT NULL,
 transferred bool NOT NULL,
 PRIMARY KEY (student_id)
);

DROP TABLE IF EXISTS students_year;
CREATE TABLE IF NOT EXISTS students_year (
 student_year_id SERIAL NOT NULL,
 student_id VARCHAR(7) NOT NULL,
 year INT NOT NULL,
 nen INT NOT NULL,
 kumi INT NOT NULL,
 ban INT NOT NULL,
 image BYTEA,
 created_by VARCHAR(7) NOT NULL,
 updated_by VARCHAR(7) NOT NULL,
 created_at TIMESTAMP NOT NULL,
 updated_at TIMESTAMP NOT NULL
);
ALTER TABLE students_year ADD CONSTRAINT FK_students_year_students FOREIGN KEY (student_id) REFERENCES students;

DROP TABLE IF EXISTS karte;
CREATE TABLE IF NOT EXISTS karte (
 karte_id SERIAL NOT NULL,
 student_id VARCHAR(7) NOT NULL,
 date DATE NOT NULL,
 contents TEXT NOT NULL,
 created_by VARCHAR(7) NOT NULL,
 updated_by VARCHAR(7) NOT NULL,
 created_at TIMESTAMP NOT NULL,
 updated_at TIMESTAMP NOT NULL
);
ALTER TABLE karte ADD CONSTRAINT FK_karte_students FOREIGN KEY (student_id) REFERENCES students;

DROP TABLE IF EXISTS attendance;
CREATE TABLE IF NOT EXISTS attendance (
 attendance_id SERIAL NOT NULL,
 student_id VARCHAR(7) NOT NULL,
 date DATE NOT NULL,
 kiroku INT,
 created_by VARCHAR(7) NOT NULL,
 updated_by VARCHAR(7) NOT NULL,
 created_at TIMESTAMP NOT NULL,
 updated_at TIMESTAMP NOT NULL
);
ALTER TABLE attendance ADD CONSTRAINT FK_attendance_students FOREIGN KEY (student_id) REFERENCES students;

DROP TABLE IF EXISTS grade;
CREATE TABLE IF NOT EXISTS grade (
 grade_id SERIAL NOT NULL,
 student_id VARCHAR(7) NOT NULL,
 year INT NOT NULL,
 term INT NOT NULL,
 subject INT NOT NULL,
 rating INT,
 created_by VARCHAR(7) NOT NULL,
 updated_by VARCHAR(7) NOT NULL,
 created_at TIMESTAMP NOT NULL,
 updated_at TIMESTAMP NOT NULL
);
ALTER TABLE grade ADD CONSTRAINT FK_grades_students FOREIGN KEY (student_id) REFERENCES students;

DROP TABLE IF EXISTS schedule;
CREATE TABLE IF NOT EXISTS schedule (
 schedule_id SERIAL NOT NULL,
 date DATE NOT NULL,
 holiday bool NOT NULL,
 created_by VARCHAR(7) NOT NULL,
 updated_by VARCHAR(7) NOT NULL,
 created_at TIMESTAMP NULL,
 updated_at TIMESTAMP NULL,
 PRIMARY KEY (schedule_id)
);