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
 --deleted CHAR(1) NOT NULL,
 PRIMARY KEY (user_id)
);

DROP TABLE IF EXISTS notices;
CREATE TABLE IF NOT EXISTS notices (
 notice_id SERIAL NOT NULL,
 title TEXT NOT NULL,
 contents TEXT,
 --created_by CHAR(8) NOT NULL,
 --updated_by CHAR(8) NOT NULL,
 created_at TIMESTAMP NULL,
 updated_at TIMESTAMP NULL,
 deleted bool NOT NULL,
 PRIMARY KEY (notice_id)
);

DROP TABLE IF EXISTS students;
CREATE TABLE IF NOT EXISTS students (
 student_id CHAR(8) NOT NULL,
 last_name VARCHAR(40) NOT NULL,
 first_name VARCHAR(40) NOT NULL,
 last_name_kana VARCHAR(40) NOT NULL,
 first_name_kana VARCHAR(40) NOT NULL,
 birth CHAR(8) NOT NULL,
 gender CHAR(1) NOT NULL,
 family1  VARCHAR(40) NOT NULL,
 family2  VARCHAR(40),
 family3  VARCHAR(40),
 family4  VARCHAR(40),
 tel1 VARCHAR(40) NOT NULL,
 tel2 VARCHAR(40),
 tel3 VARCHAR(40),
 tel4 VARCHAR(40),
 postal_code CHAR(7) NOT NULL,
 adress VARCHAR(255) NOT NULL,
 memo TEXT,
 created_by CHAR(8) NOT NULL,
 updated_by CHAR(8) NOT NULL,
 created_at TIMESTAMP NOT NULL,
 updated_at TIMESTAMP NOT NULL,
 deleted CHAR(1) NOT NULL,
 PRIMARY KEY (student_id)
);

DROP TABLE IF EXISTS students_year;
CREATE TABLE IF NOT EXISTS students_year (
 student_id CHAR(8) NOT NULL,
 year CHAR(4) NOT NULL,
 nen CHAR(1) NOT NULL,
 kumi CHAR(1) NOT NULL,
 ban CHAR(2) NOT NULL,
 path VARCHAR(255) NOT NULL,
 created_by CHAR(8) NOT NULL,
 updated_by CHAR(8) NOT NULL,
 created_at TIMESTAMP NOT NULL,
 updated_at TIMESTAMP NOT NULL,
 deleted CHAR(1) NOT NULL
);

DROP TABLE IF EXISTS karte;
CREATE TABLE IF NOT EXISTS karte (
 karte_id SERIAL NOT NULL,
 student_id CHAR(8) NOT NULL,
 important CHAR(1) NOT NULL,
 date VARCHAR(255) NOT NULL,
 contents TEXT NOT NULL,
 created_by CHAR(8) NOT NULL,
 updated_by CHAR(8) NOT NULL,
 created_at TIMESTAMP NOT NULL,
 updated_at TIMESTAMP NOT NULL,
 deleted CHAR(1) NOT NULL
);

DROP TABLE IF EXISTS attendance;
CREATE TABLE IF NOT EXISTS attendance (
 attendance_id SERIAL NOT NULL,
 student_id CHAR(8) NOT NULL,
 date CHAR(8) NOT NULL,
 syusseki CHAR(1) NOT NULL,
 chikoku CHAR(1) NOT NULL,
 soutai CHAR(1) NOT NULL,
 kesseki CHAR(1) NOT NULL,
 syuttei CHAR(1) NOT NULL,
 kibiki CHAR(1) NOT NULL,
 confirmed CHAR(1) NOT NULL,
 contents TEXT NOT NULL,
 created_by CHAR(8) NOT NULL,
 updated_by CHAR(8) NOT NULL,
 created_at TIMESTAMP NOT NULL,
 updated_at TIMESTAMP NOT NULL,
 deleted CHAR(1) NOT NULL
);

DROP TABLE IF EXISTS grades;
CREATE TABLE IF NOT EXISTS grades (
 grades_id SERIAL NOT NULL,
 student_id CHAR(8) NOT NULL,
 year CHAR(4) NOT NULL,
 term CHAR(1) NOT NULL,
 subject CHAR(2) NOT NULL,
 grades CHAR(1) NOT NULL,
 confirmed CHAR(8) NOT NULL,
 created_by CHAR(8) NOT NULL,
 updated_by CHAR(8) NOT NULL,
 created_at TIMESTAMP NOT NULL,
 updated_at TIMESTAMP NOT NULL,
 deleted CHAR(1) NOT NULL
);