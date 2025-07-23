-- Скрипт сгенерирован Devart dbForge Studio for MySQL, Версия 5.0.97.1
-- Домашняя страница продукта: http://www.devart.com/ru/dbforge/mysql/studio
-- Дата скрипта: 26.05.2025 12:07:01
-- Версия сервера: 5.0.67-community-nt
-- Версия клиента: 4.1

-- 
-- Отключение внешних ключей
-- 
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;

-- 
-- Установка базы данных по умолчанию
--
USE bdkindergarten;

--
-- Описание для таблицы attendance
--
DROP TABLE IF EXISTS attendance;
CREATE TABLE attendance (
  ID INT(11) NOT NULL AUTO_INCREMENT,
  Id_Children INT(11) DEFAULT NULL,
  `Date` DATE DEFAULT NULL,
  Status VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (ID),
  CONSTRAINT FK_attendance_children_ID FOREIGN KEY (Id_Children)
    REFERENCES children(ID) ON DELETE NO ACTION ON UPDATE NO ACTION
)
ENGINE = INNODB
AUTO_INCREMENT = 10
AVG_ROW_LENGTH = 16384
CHARACTER SET utf8
COLLATE utf8_general_ci;

--
-- Описание для таблицы classroom
--
DROP TABLE IF EXISTS classroom;
CREATE TABLE classroom (
  ID INT(11) NOT NULL AUTO_INCREMENT,
  Name_room VARCHAR(255) DEFAULT NULL,
  namber_room VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (ID),
  UNIQUE INDEX ID (ID)
)
ENGINE = INNODB
AUTO_INCREMENT = 6
AVG_ROW_LENGTH = 3276
CHARACTER SET utf8
COLLATE utf8_general_ci;

--
-- Описание для таблицы groups
--
DROP TABLE IF EXISTS groups;
CREATE TABLE groups (
  ID INT(11) NOT NULL AUTO_INCREMENT,
  Name_groups VARCHAR(255) DEFAULT NULL,
  `Year` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (ID),
  UNIQUE INDEX ID (ID)
)
ENGINE = INNODB
AUTO_INCREMENT = 6
AVG_ROW_LENGTH = 3276
CHARACTER SET utf8
COLLATE utf8_general_ci;

--
-- Описание для таблицы kindergarten
--
DROP TABLE IF EXISTS kindergarten;
CREATE TABLE kindergarten (
  ID INT(11) NOT NULL AUTO_INCREMENT,
  Name VARCHAR(255) DEFAULT NULL,
  Adres VARCHAR(255) DEFAULT NULL,
  Telefon VARCHAR(255) DEFAULT NULL,
  Director VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (ID)
)
ENGINE = INNODB
AUTO_INCREMENT = 2
AVG_ROW_LENGTH = 16384
CHARACTER SET utf8
COLLATE utf8_general_ci;

--
-- Описание для таблицы lessons
--
DROP TABLE IF EXISTS lessons;
CREATE TABLE lessons (
  ID INT(11) NOT NULL AUTO_INCREMENT,
  Name_Lessons VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (ID)
)
ENGINE = INNODB
AUTO_INCREMENT = 5
AVG_ROW_LENGTH = 4096
CHARACTER SET latin1
COLLATE latin1_swedish_ci;

--
-- Описание для таблицы menu
--
DROP TABLE IF EXISTS menu;
CREATE TABLE menu (
  ID INT(11) NOT NULL AUTO_INCREMENT,
  `Date` DATE DEFAULT NULL,
  Type VARCHAR(255) DEFAULT NULL,
  Food VARCHAR(255) DEFAULT NULL,
  Number_grams VARCHAR(255) DEFAULT NULL,
  Callories VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (ID)
)
ENGINE = INNODB
AUTO_INCREMENT = 4
AVG_ROW_LENGTH = 5461
CHARACTER SET utf8
COLLATE utf8_general_ci;

--
-- Описание для таблицы teacher
--
DROP TABLE IF EXISTS teacher;
CREATE TABLE teacher (
  ID INT(11) NOT NULL AUTO_INCREMENT,
  FIO VARCHAR(255) DEFAULT NULL,
  Telefon VARCHAR(255) DEFAULT NULL,
  Adres VARCHAR(255) DEFAULT NULL,
  Specialization VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (ID),
  UNIQUE INDEX ID (ID)
)
ENGINE = INNODB
AUTO_INCREMENT = 5
AVG_ROW_LENGTH = 4096
CHARACTER SET utf8
COLLATE utf8_general_ci;

--
-- Описание для таблицы children
--
DROP TABLE IF EXISTS children;
CREATE TABLE children (
  ID INT(11) NOT NULL AUTO_INCREMENT,
  FIO VARCHAR(255) DEFAULT NULL,
  Birthday DATE DEFAULT NULL,
  Date_receipt DATE DEFAULT NULL,
  Date_departure DATE DEFAULT NULL,
  Id_Groups INT(11) DEFAULT NULL,
  BirthdayCertificate VARCHAR(255) DEFAULT NULL,
  retirement VARCHAR(255) DEFAULT NULL,
  Status VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (ID),
  UNIQUE INDEX ID (ID),
  CONSTRAINT FK_children_groups_ID FOREIGN KEY (Id_Groups)
    REFERENCES groups(ID) ON DELETE NO ACTION ON UPDATE NO ACTION
)
ENGINE = INNODB
AUTO_INCREMENT = 93
AVG_ROW_LENGTH = 1820
CHARACTER SET utf8
COLLATE utf8_general_ci;

--
-- Описание для таблицы timetable
--
DROP TABLE IF EXISTS timetable;
CREATE TABLE timetable (
  ID INT(11) NOT NULL AUTO_INCREMENT,
  `Date` DATE DEFAULT NULL,
  `Time` VARCHAR(255) DEFAULT NULL,
  Id_Lessons INT(11) DEFAULT NULL,
  Id_Group INT(11) DEFAULT NULL,
  Id_Classroom INT(11) DEFAULT NULL,
  Id_Teacher INT(11) DEFAULT NULL,
  PRIMARY KEY (ID),
  CONSTRAINT FK_timetable_classroom_ID FOREIGN KEY (Id_Classroom)
    REFERENCES classroom(ID) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT FK_timetable_groups_ID FOREIGN KEY (Id_Group)
    REFERENCES groups(ID) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT FK_timetable_lessons_ID FOREIGN KEY (Id_Lessons)
    REFERENCES lessons(ID) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT FK_timetable_teacher_ID FOREIGN KEY (Id_Teacher)
    REFERENCES teacher(ID) ON DELETE RESTRICT ON UPDATE RESTRICT
)
ENGINE = INNODB
AUTO_INCREMENT = 5
AVG_ROW_LENGTH = 4096
CHARACTER SET utf8
COLLATE utf8_general_ci;

--
-- Описание для таблицы parents
--
DROP TABLE IF EXISTS parents;
CREATE TABLE parents (
  ID INT(11) NOT NULL AUTO_INCREMENT,
  FIO VARCHAR(255) DEFAULT NULL,
  Telefon VARCHAR(255) DEFAULT NULL,
  Ardres VARCHAR(255) DEFAULT NULL,
  Passport VARCHAR(255) DEFAULT NULL,
  Id_Children INT(11) DEFAULT NULL,
  PRIMARY KEY (ID),
  UNIQUE INDEX ID (ID),
  CONSTRAINT FK_parents_children_ID FOREIGN KEY (Id_Children)
    REFERENCES children(ID) ON DELETE RESTRICT ON UPDATE RESTRICT
)
ENGINE = INNODB
AUTO_INCREMENT = 42
AVG_ROW_LENGTH = 1638
CHARACTER SET utf8
COLLATE utf8_general_ci;

-- 
-- Вывод данных для таблицы attendance
--
INSERT INTO attendance VALUES 
  (9, 73, '2024-05-02', '????????????');

-- 
-- Вывод данных для таблицы classroom
--
INSERT INTO classroom VALUES 
  (1, '???????', '101'),
  (2, '??????????????', '103'),
  (3, '?????????????', '133'),
  (4, '???????????????', '122'),
  (5, '??????????????', '112');

-- 
-- Вывод данных для таблицы groups
--
INSERT INTO groups VALUES 
  (1, '???????', '2023'),
  (2, '?????????', '2023'),
  (3, '?????????', '2022'),
  (4, '????????', '2024'),
  (5, '??????????', '2020');

-- 
-- Вывод данных для таблицы kindergarten
--
INSERT INTO kindergarten VALUES 
  (1, '??????? ??? "???????"', '??. ??????? 52', '89035573745', '???????? ???? ?????????');

-- 
-- Вывод данных для таблицы lessons
--
INSERT INTO lessons VALUES 
  (1, 'dfgdfgdfg'),
  (2, 'fgdfgtgtdh'),
  (3, 'rtet55ey'),
  (4, 'wrwrwerewt');

-- 
-- Вывод данных для таблицы menu
--
INSERT INTO menu VALUES 
  (1, '2025-02-25', '????', '???????? ? ????????', '500?.', '450'),
  (2, '2025-02-27', '????', '???', '444?.', '222'),
  (3, '2025-02-25', '????', '???????? ???????', NULL, NULL);

-- 
-- Вывод данных для таблицы teacher
--
INSERT INTO teacher VALUES 
  (1, '??????? ?????? ??????????', '890345734', '1', '??????? ????????? ?????'),
  (2, '????????? ????? ????????????', '89223435454', '3', '??????? ??????? ?????'),
  (3, '?????????? ????? ????????????', '89553674525', '4', '??????? ????????? ?????'),
  (4, '????????? ???? ??????????', '33333333333333', '2', '??????? ??????? ?????');

-- 
-- Вывод данных для таблицы children
--
INSERT INTO children VALUES 
  (73, '?????? ???? ????????', '2021-05-05', NULL, NULL, NULL, '8465 83645', NULL, '????????'),
  (74, '??????? ???? ????????', '2021-05-13', NULL, NULL, NULL, '9475 84867', NULL, '????????'),
  (75, '??????? ?????? ??????????', '2021-08-13', NULL, NULL, NULL, '9947 83266', NULL, '????????'),
  (79, '???????? ???? ??????????', '2021-04-15', NULL, NULL, NULL, '7465 38465', NULL, '????????'),
  (88, '???????? ??????? ????????', '2021-02-16', NULL, NULL, NULL, '3425 2355', NULL, '????????'),
  (89, '??????? ??????? ?????????', '2021-07-07', NULL, NULL, NULL, '2345 23075', NULL, '????????'),
  (90, '??????? ?????? ????????', '2021-09-16', NULL, NULL, NULL, '3426 76865', NULL, '????????'),
  (91, '????????? ???????? ??????????', '2021-08-25', NULL, NULL, NULL, '2865 94876', NULL, '????????'),
  (92, '??????? ?????? ????????', '2021-05-29', '2025-05-20', '2030-05-30', 4, '9873 82656', '??????????? ??????????????', '????????');

-- 
-- Вывод данных для таблицы timetable
--
INSERT INTO timetable VALUES 
  (1, '2025-02-13', '12:00', 3, 1, 1, 1),
  (2, '2025-02-13', '13:00', 1, 5, 2, 1),
  (3, '2025-04-15', '324', 2, 3, 1, 3),
  (4, '2024-03-07', 'RFEFREFR', NULL, 1, 1, 1);

-- 
-- Вывод данных для таблицы parents
--
INSERT INTO parents VALUES 
  (21, '??????? ????? ????????', '908536545', '???? 34', '7878 67688', 73),
  (22, '?????? ???? ?????????', '890537365', '???? 56', '8935 72345', 74),
  (23, '???????? ????? ??????????', '8903563556', '??????? 23', '6735 36475', 75),
  (26, '??????? ??????? ??????????', '8905447635', '???? 45', '7634 83465', 79),
  (34, 'fdgdfhdfh', '345436', '6346', '556456', NULL),
  (37, '???????? ??????? ????????', '890635573', '??????? 30', '7023 86235', 88),
  (38, '??????? ??????? ?????????', '8906435475', '??????? 45', '8926 83526', 89),
  (39, '??????? ?????? ????????', '8906256473', '??????? 32', '8274 03786', 90),
  (40, '????????? ???????? ??????????', '8906626757', '????????? 87', '7863 96235', 91),
  (41, '??????? ?????? ????????', '8906635653', '????????? 64', '9938 76245', 92);

-- 
-- Включение внешних ключей
-- 
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;