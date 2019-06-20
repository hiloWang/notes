USE day15;
CREATE TABLE mvc_sample_customer (
  id          VARCHAR(100) PRIMARY KEY,
  name        VARCHAR(100) NOT NULL,
  gender      VARCHAR(100) NOT NULL,
  birthday    DATE         NOT NULL,
  cellphone   VARCHAR(100) NOT NULL,
  email       VARCHAR(100) NOT NULL,
  hobbies     VARCHAR(100) NOT NULL,
  type        VARCHAR(100) NOT NULL,
  description VARCHAR(100) NOT NULL
);