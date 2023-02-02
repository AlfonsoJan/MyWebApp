/* 
Remove the tables if they exist
*/
DROP TABLE if EXISTS user_project_files;
DROP TABLE if EXISTS process;
DROP TABLE if EXISTS labeled_files;
DROP TABLE if EXISTS projects;
DROP TABLE if EXISTS users;

/*
Creates the table users, to give every user an id.
*/
CREATE TABLE users
(
    id INT AUTO_INCREMENT NOT NULL,
    username VARCHAR(20) NOT NULL,
    PRIMARY KEY (id)
);

/*
Creates the table projects, to keep track of every project.
*/
CREATE TABLE projects
(
    id INT AUTO_INCREMENT NOT NULL,
    project_name VARCHAR(20),
    PRIMARY KEY (id)
);

/*
Creates the table for all the labeled files.
*/
CREATE TABLE labeled_files
(
    id INT auto_increment NOT NULL,
    label VARCHAR(100) NOT NULL,
    path VARCHAR(100) NOT NULL,
    PRIMARY KEY (id)
);

/*
Creates the table for all the Process.
*/
CREATE TABLE process (
    id INT auto_increment NOT NULL,
    type VARCHAR(100) NOT NULL,
    unique_id VARCHAR(36) NOT NULL,
    id_project INT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (id_project)
        REFERENCES projects (id)
        ON DELETE RESTRICT
);

/*
Creates the table to link the project with files to the user.
*/
CREATE TABLE user_project_files (
    user_id int NOT NULL,
    project_id INT NOT NULL,
    file_id INT NOT NULL,
    FOREIGN KEY (user_id)
        REFERENCES users (id),
    FOREIGN KEY (project_id)
        REFERENCES projects (id),
    FOREIGN KEY (file_id)
        REFERENCES labeled_files (id),
    INDEX (user_id)
);

-- Inserts a test user into the database
INSERT INTO users (username) VALUES ('test_user')