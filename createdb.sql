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
    client_name varchar(100) not null,
    book_id int not null references books(id),
    start_date date not null,
    end_date date not null
);

-- insert into books(title, author, page_count, release_date) values ('123', 'abc', 1, '01.01.2000');