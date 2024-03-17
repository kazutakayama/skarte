DROP TABLE IF EXISTS users;
CREATE TABLE IF NOT EXISTS users (
 user_id CHAR(8) NOT NULL,
 password VARCHAR(255) NOT NULL,
 last_name VARCHAR(40) NOT NULL,
 first_name VARCHAR(40) NOT NULL,
 last_name_kana VARCHAR(40) NOT NULL,
 first_name_kana VARCHAR(40) NOT NULL,
 mail VARCHAR(255) NOT NULL, 
 --created_by CHAR(8) NOT NULL,
 --updated_by CHAR(8) NOT NULL,
 --created_at TIMESTAMP NOT NULL,
 --updated_at TIMESTAMP NOT NULL,
 --deleted bool NOT NULL,
 PRIMARY KEY (user_id)
);

DROP TABLE IF EXISTS notices;
CREATE TABLE IF NOT EXISTS notices (
 notice_id SERIAL NOT NULL,
 title TEXT NOT NULL,
 contents TEXT,
 --created_by INT NOT NULL,
 --updated_by INT NOT NULL,
 created_at TIMESTAMP NULL,
 updated_at TIMESTAMP NULL,
 deleted bool NOT NULL,
 PRIMARY KEY (notice_id)
);

DROP TABLE IF EXISTS students;
CREATE TABLE IF NOT EXISTS students (
 student_id INT NOT NULL,
 last_name VARCHAR(255) NOT NULL,
 first_name VARCHAR(255) NOT NULL,
 last_name_kana VARCHAR(255) NOT NULL,
 first_name_kana VARCHAR(255) NOT NULL,
 birth DATE NOT NULL,
 gender INT NOT NULL,
 family1 VARCHAR(255) NOT NULL,
 family2 VARCHAR(255),
 family3 VARCHAR(255),
 family4 VARCHAR(255),
 tel1 INT NOT NULL,
 tel2 INT,
 tel3 INT,
 tel4 INT,
 postal_code INT NOT NULL,
 adress VARCHAR(1000) NOT NULL,
 memo TEXT,
 --created_by INT NOT NULL,
 --updated_by INT NOT NULL,
 created_at TIMESTAMP NOT NULL,
 updated_at TIMESTAMP NOT NULL,
 transferred bool NOT NULL,
 deleted bool NOT NULL,
 PRIMARY KEY (student_id)
);

DROP TABLE IF EXISTS students_year;
CREATE TABLE IF NOT EXISTS students_year (
 student_year_id SERIAL NOT NULL,
 student_id INT NOT NULL,
 year INT NOT NULL,
 nen INT NOT NULL,
 kumi INT NOT NULL,
 ban INT NOT NULL,
 path VARCHAR(255),
 --created_by INT NOT NULL,
 --updated_by INT NOT NULL,
 created_at TIMESTAMP NOT NULL,
 updated_at TIMESTAMP NOT NULL,
 deleted bool NOT NULL
);
ALTER TABLE students_year ADD CONSTRAINT FK_students_year_students FOREIGN KEY (student_id) REFERENCES students;

DROP TABLE IF EXISTS karte;
CREATE TABLE IF NOT EXISTS karte (
 karte_id SERIAL NOT NULL,
 student_id INT NOT NULL,
 --important CHAR(1) NOT NULL,
 date DATE NOT NULL,
 contents TEXT NOT NULL,
 --created_by INT NOT NULL,
 --updated_by INT NOT NULL,
 created_at TIMESTAMP NOT NULL,
 updated_at TIMESTAMP NOT NULL,
 deleted bool NOT NULL
);
ALTER TABLE karte ADD CONSTRAINT FK_karte_students FOREIGN KEY (student_id) REFERENCES students;

DROP TABLE IF EXISTS attendance;
CREATE TABLE IF NOT EXISTS attendance (
 attendance_id SERIAL NOT NULL,
 student_id INT NOT NULL,
 date DATE NOT NULL,
 chikoku INT,
 soutai INT,
 kesseki INT,
 syuttei INT,
 kibiki INT,
 --confirmed CHAR(1) NOT NULL,
 --contents TEXT NOT NULL,
 --created_by CHAR(8) NOT NULL,
 --updated_by CHAR(8) NOT NULL,
 created_at TIMESTAMP NOT NULL,
 updated_at TIMESTAMP NOT NULL,
 deleted bool NOT NULL
);
ALTER TABLE attendance ADD CONSTRAINT FK_attendance_students FOREIGN KEY (student_id) REFERENCES students;

DROP TABLE IF EXISTS grades;
CREATE TABLE IF NOT EXISTS grades (
 grade_id SERIAL NOT NULL,
 student_id INT NOT NULL,
 year INT NOT NULL,
 term INT NOT NULL,
 subject INT NOT NULL,
 grade INT,
 --confirmed CHAR(8) NOT NULL,
 --created_by CHAR(8) NOT NULL,
 --updated_by CHAR(8) NOT NULL,
 created_at TIMESTAMP NOT NULL,
 updated_at TIMESTAMP NOT NULL,
 deleted bool NOT NULL
);
ALTER TABLE grades ADD CONSTRAINT FK_grades_students FOREIGN KEY (student_id) REFERENCES students;