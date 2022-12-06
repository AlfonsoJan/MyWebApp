/* 
Remove the tables if they exist
*/
drop table if exists label_files;
drop table if exists projects;
drop table if exists users;

/*
Creates the table users, to give every user an id.
*/
create table users
(
    id int auto_increment not null,
    username varchar(20) not null,
    primary key(id)
);

/*
Creates the table probjects, to keep track of every project.
And which user it belongs to with his user id.
*/
create table projects
(
    id int auto_increment not null,
    project_name varchar(20),
    user_id int not null,
    primary key(id),
    foreign key(user_id)
        references users(id)
        on delete restrict
);

/*
Creates the table for all the labeled files, to keep track of 
which files belong to certain projects and or users.
*/
create table label_files
(
    id int auto_increment not null,
    label varchar(100) not null,
    path varchar(100) not null,
    project int not null,
    primary key(id),
    foreign key(project)
        references projects(id)
        on delete restrict
);