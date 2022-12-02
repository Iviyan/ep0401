-- create database ep0401_pw2;

create table roles
(
    id int primary key,
    name varchar(100) not null unique
);

insert into roles(id, name) values (1, 'admin'), (2, 'user');

create table passports
(
    id serial primary key,
    serial char(4) not null,
    number char(6) not null,
    first_name varchar(30) not null,
    last_name varchar(30) not null,
    patronymic varchar(30) null,
    unique (serial, number)
);

create table users
(
    id serial primary key,
    login varchar(30) not null unique,
    password text not null,
    passport_id int not null references passports(id),
    role_id int not null references roles(id),
    registration_date timestamptz not null
);

/*create table user_roles
(
    user_id int references users(id),
    role_id int references roles(id),
    primary key (user_id, role_id)
);*/

create table books
(
    id serial primary key,
    title varchar(200) not null,
    author varchar(100) not null,
    page_count int not null,
    release_date date not null,
    description text not null default ''
);

create table book_rental
(
    id serial primary key,
    user_id int not null references users(id),
    book_id int not null references books(id),
    start_date date not null,
    end_date date not null
);



/*insert into books(title, author, page_count, release_date) values ('123', 'abc', 1, '01.01.2000');

insert into book_rental(client_id, book_id, start_date, end_date)
VALUES (2, 1, '01.01.2022', '10.01.2022');

insert into passports(serial, number, first_name, last_name, patronymic)
VALUES ('1234', '567890', 'Иван', 'Иванов', 'Иванович')



select * from users;
select * from passports;
select * from roles;
insert into roles(id, name) values (1, 'admin'), (2, 'user'), (3, 'salesman');
insert into users(login, password, passport_id, role_id, registration_date)
VALUES ('admin', 'admin', 1, 1, '01.01.2022');*/