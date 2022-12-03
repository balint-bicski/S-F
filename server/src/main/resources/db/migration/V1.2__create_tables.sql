create table caff_metadata
(
    id           bigint                   not null primary key,
    creator      varchar(255)             not null,
    uploader     varchar(255)             not null,
    created_date timestamp with time zone not null,
    ciff_count   int                      not null,
    size         int                      not null,
    title        varchar(255)             not null
);

create table comment
(
    id           bigint                   not null primary key,
    caff_id      bigint                   not null references caff_metadata,
    creator      varchar(255)             not null,
    content      varchar                  not null,
    created_date timestamp with time zone not null
);

create table purchase_tokens
(
    id           bigint                   not null primary key,
    created_date timestamp with time zone not null,
    caff_id      bigint                   not null references caff_metadata,
    user_id      bigint                   not null references auth_user,
    token        uuid                     not null
);
