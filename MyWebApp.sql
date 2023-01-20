/* 
Remove the tables if they exist
*/
DROP TABLE if EXISTS process;
DROP TABLE if EXISTS labeled_files;
DROP TABLE if EXISTS projects;
DROP TABLE if EXISTS users;
DROP TABLE if EXISTS process;

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
And which user it belongs to with his user id.
*/
CREATE TABLE projects
(
    id INT auto_increment NOT NULL,
    project_name VARCHAR(20) UNIQUE,
    user_id INT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id)
        REFERENCES users (id)
        ON DELETE RESTRICT
);

/*
Creates the table for all the labeled files, to keep track of 
which files belong to certain projects and or users.
*/
CREATE TABLE labeled_files
(
    id INT auto_increment NOT NULL,
    label VARCHAR(100) NOT NULL,
    path VARCHAR(100) NOT NULL,
    project_id INT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (project_id)
        REFERENCES projects (id)
        ON DELETE RESTRICT
);

CREATE TABLE process
(
    id INT auto_increment NOT NULL,
    type VARCHAR(100) NOT NULL,
    project_id INT,
    user_id INT NOT NULL,
    unique_id VARCHAR(36) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (project_id)
        REFERENCES projects (id)
        ON DELETE RESTRICT,
    FOREIGN KEY (user_id)
        REFERENCES users (id)
        ON DELETE RESTRICT
);

-- Inserts a test user into the database
INSERT INTO users (username) VALUES ('test_user')