-- create database ep0401_pw2;

create table roles
(
    id int primary key,
    name varchar(100) not null unique
);

insert into roles(id, name) values (1, 'admin'), (2, 'user'), (3, 'salesman');

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

create table authors
(
    id serial primary key,
    name varchar(100) not null unique
);

create table countries
(
    id serial primary key,
    name varchar(100) not null unique
);

create table languages
(
    id serial primary key,
    name varchar(100) not null unique
);

create table genres
(
    id serial primary key,
    name varchar(100) not null unique
);

create table categories
(
    id serial primary key,
    name varchar(100) not null unique
);

create table selections
(
    id serial primary key,
    creation_date date not null,
    name varchar(100) not null unique
);

create table books
(
    id serial primary key,
    title varchar(200) not null,
    author_id int not null references authors(id),
    page_count int not null,
    release_date date not null,
    description text not null default '',
    category_id int not null references categories(id),
    language_id int not null references languages(id),
    country_id int not null references countries(id)
);

create table book_genres
(
    book_id int references books(id),
    genre_id int references genres(id),
    primary key (book_id, genre_id)
);

create table book_rental
(
    id serial primary key,
    user_id int not null references users(id),
    book_id int not null references books(id),
    start_date date not null,
    end_date date not null
);

create table selection_books
(
    selection_id int references selections(id),
    book_id int references books(id),
    primary key (selection_id, book_id)
);

create table suppliers
(
    id serial primary key,
    inn varchar(12) not null unique,
    name varchar(100) not null
);

create table shipments
(
    id serial primary key,
    supplier_id int not null references suppliers(id),
    book_id int not null references books(id),
    datetime timestamptz not null
);