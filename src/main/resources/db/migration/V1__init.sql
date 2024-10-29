CREATE TABLE Persons (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    name VARCHAR NOT NULL,
    username VARCHAR UNIQUE NOT NULL,
    password VARCHAR NOT NULL
);

CREATE TABLE Roles (
    id INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    name VARCHAR(20) NOT NULL UNIQUE
);

CREATE TABLE Persons_Roles (
    person_id BIGINT REFERENCES Persons(id),
    role_id INT REFERENCES Roles(id),
    PRIMARY KEY (person_id, role_id)
);

CREATE TABLE Requests (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    status VARCHAR(20) NOT NULL,
    text VARCHAR,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    person_id BIGINT REFERENCES Persons(id)
);

INSERT INTO Persons (name, username, password) VALUES ('Ivan','user', '123');
INSERT INTO Persons (name, username, password) VALUES ('Bob','user2', '123');
INSERT INTO Persons (name, username, password) VALUES ('Tod','operator_user', '123');
INSERT INTO Persons (name, username, password) VALUES ('Mary','operator', '123');
INSERT INTO Persons (name, username, password) VALUES ('Kevin','admin_operator_user', '123');
INSERT INTO Persons (name, username, password) VALUES ('Vasya','admin', '123');

INSERT INTO roles (name) VALUES ('ROLE_USER');
INSERT INTO roles (name) VALUES ('ROLE_OPERATOR');
INSERT INTO roles (name) VALUES ('ROLE_ADMIN');

INSERT INTO Persons_Roles (person_id, role_id) VALUES (1, 1);
INSERT INTO Persons_Roles (person_id, role_id) VALUES (2, 1);
INSERT INTO Persons_Roles (person_id, role_id) VALUES (3, 1);
INSERT INTO Persons_Roles (person_id, role_id) VALUES (3, 2);
INSERT INTO Persons_Roles (person_id, role_id) VALUES (4, 2);
INSERT INTO Persons_Roles (person_id, role_id) VALUES (5, 1);
INSERT INTO Persons_Roles (person_id, role_id) VALUES (5, 2);
INSERT INTO Persons_Roles (person_id, role_id) VALUES (5, 3);
INSERT INTO Persons_Roles (person_id, role_id) VALUES (6, 3);

INSERT INTO requests (status, text, created_at, person_id) VALUES ('DRAFT', 'text1', '2016-06-22 19:10:25-07', 1);
INSERT INTO requests (status, text, created_at, person_id) VALUES ('DRAFT', 'text2', '2016-06-22 20:10:25-07', 1);
INSERT INTO requests (status, text, created_at, person_id) VALUES ('DRAFT', 'text3', '2016-06-22 21:10:25-07', 1);
INSERT INTO requests (status, text, created_at, person_id) VALUES ('SENT', 'text4', '2017-06-22 19:10:25-07', 2);
INSERT INTO requests (status, text, created_at, person_id) VALUES ('APPROVED', 'text1', '2016-07-22 19:10:25-07', 1);
INSERT INTO requests (status, text, created_at, person_id) VALUES ('DECLINED', 'text1', '2016-08-22 19:10:25-07', 2);