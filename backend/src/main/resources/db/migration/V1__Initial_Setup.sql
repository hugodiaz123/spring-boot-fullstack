create table customer(
    id bigserial primary key,
    name text not null,
    email text not null,
    password text not null,
    age int not null,
    gender text not null
);