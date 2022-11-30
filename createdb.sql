-- create database ep0401_pw2;

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
    client_id int not null references clients(id),
    book_id int not null references books(id),
    start_date date not null,
    end_date date not null
);

create table clients
(
    id serial primary key,
    passport_serial char(4) not null,
    passport_number char(6) not null,
    registration_date timestamptz not null,
    foreign key (passport_serial, passport_number) references passports(serial, number) on update cascade
);

create table passports
(
    serial char(4) not null,
    number char(6) not null,
    first_name varchar(30) not null,
    last_name varchar(30) not null,
    patronymic varchar(30) null,
    primary key (serial, number)
);

/*insert into books(title, author, page_count, release_date) values ('123', 'abc', 1, '01.01.2000');

insert into book_rental(client_id, book_id, start_date, end_date)
VALUES (2, 1, '01.01.2022', '10.01.2022');

insert into passports(serial, number, first_name, last_name, patronymic)
VALUES ('1234', '567890', 'Иван', 'Иванов', 'Иванович')

insert into clients(passport_serial, passport_number, registration_date)
VALUES ('1234', '567890', '01.01.2022');

select * from clients*/