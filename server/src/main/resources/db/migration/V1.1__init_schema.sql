create sequence doublefree_id_sequence start with 1 increment by 50;

create table auth_user
(
    id       bigint       not null primary key,
    email    varchar(255) not null,
    password varchar(255) not null,
    role     varchar(255) not null
)
