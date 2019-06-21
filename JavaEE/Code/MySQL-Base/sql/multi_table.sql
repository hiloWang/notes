###################################
#  多表查询
###################################

CREATE TABLE multi_person (
  id   INT PRIMARY KEY,
  name VARCHAR(100)
);

CREATE TABLE multi_idcard (
  id  INT PRIMARY KEY,
  num VARCHAR(100),
  CONSTRAINT person_id_fk FOREIGN KEY (id) REFERENCES multi_person (id)
);


CREATE TABLE multi_department (
  id    INT PRIMARY KEY,
  name  VARCHAR(100),
  floor VARCHAR(100)
);

CREATE TABLE multi_employee (
  id        INT PRIMARY KEY,
  name      VARCHAR(100),
  salary    FLOAT(8, 2),
  depart_id INT,
  CONSTRAINT depart_id_fk FOREIGN KEY (depart_id) REFERENCES multi_department (id)
);


CREATE TABLE multi_teacher (
  id     INT PRIMARY KEY,
  name   VARCHAR(100),
  salary FLOAT(8, 2)
);

CREATE TABLE multi_student (
  id    INT PRIMARY KEY,
  name  VARCHAR(100),
  grade VARCHAR(100)
);

CREATE TABLE multi_teacher_student (
  t_id INT,
  s_id INT,
  PRIMARY KEY (t_id, s_id),
  CONSTRAINT t_id_fk FOREIGN KEY (t_id) REFERENCES multi_teacher (id),
  CONSTRAINT s_id_fk FOREIGN KEY (s_id) REFERENCES multi_student (id)
);