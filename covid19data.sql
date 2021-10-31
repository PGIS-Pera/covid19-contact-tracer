create table if not exists conformation_token
(
    id          int auto_increment
        primary key,
    create_date datetime(6)  null,
    email       varchar(255) not null,
    end_date    datetime(6)  null,
    token       varchar(255) null,
    constraint UK_qc8k8gissckg12ghxhmfjw1vm
        unique (email)
);

create table if not exists district
(
    id       int auto_increment
        primary key,
    name     varchar(255) null,
    province varchar(255) null,
    constraint UK_kvkg405mr04cjytv1tuyh7ryr
        unique (name)
);

create table if not exists ds_office
(
    id         int auto_increment
        primary key,
    address    varchar(255) null,
    email      varchar(255) null,
    land       varchar(255) null,
    name       varchar(60)  null,
    distric_id int          not null,
    constraint fk_ds_office_vs_district
        foreign key (distric_id) references district (id)
);

create table if not exists grama_niladhari
(
    id           int auto_increment
        primary key,
    name         varchar(255) null,
    number       varchar(255) null,
    ds_office_id int          not null,
    constraint fk_grama_niladhari_vs_ds_office
        foreign key (ds_office_id) references ds_office (id)
);

create table if not exists location_interact
(
    id                 int auto_increment
        primary key,
    created_at         datetime(6)  not null,
    created_by         varchar(20)  not null,
    updated_at         datetime(6)  not null,
    updated_by         varchar(20)  not null,
    name               varchar(255) null,
    grama_niladhari_id int          not null,
    constraint fk_location_interact_vs_grama_niladhari
        foreign key (grama_niladhari_id) references grama_niladhari (id)
);

create table if not exists news
(
    id                 int auto_increment
        primary key,
    mobile             varchar(255) null,
    grama_niladhari_id int          not null,
    constraint UK_b39xi57ybaue1akp57c8e1bd
        unique (mobile),
    constraint fk_person_vs_grama_niladhari
        foreign key (grama_niladhari_id) references grama_niladhari (id)
);

create table if not exists person
(
    id                    int auto_increment
        primary key,
    created_at            datetime(6)  not null,
    created_by            varchar(20)  not null,
    updated_at            datetime(6)  not null,
    updated_by            varchar(20)  not null,
    address               varchar(255) null,
    code                  varchar(255) not null,
    date_of_birth         date         null,
    gender                varchar(255) null,
    mobile                varchar(10)  null,
    name                  varchar(255) not null,
    nic                   varchar(12)  null,
    person_status         varchar(255) null,
    working_place_address varchar(255) null,
    grama_niladhari_id    int          not null,
    constraint UK_4gr67lk0t9mc9aoqfbo9mdmtw
        unique (code),
    constraint UK_pae7hh118yg0kk52ywn33330p
        unique (nic),
    constraint fk_news_vs_grama_niladhari
        foreign key (grama_niladhari_id) references grama_niladhari (id)
);

create table if not exists role
(
    id         int auto_increment
        primary key,
    created_at datetime(6)  not null,
    created_by varchar(20)  not null,
    updated_at datetime(6)  not null,
    updated_by varchar(20)  not null,
    role_name  varchar(255) not null,
    constraint UK_iubw515ff0ugtm28p8g3myt0h
        unique (role_name)
);

create table if not exists stay_location
(
    id                 int auto_increment
        primary key,
    created_at         datetime(6)  not null,
    created_by         varchar(20)  not null,
    updated_at         datetime(6)  not null,
    updated_by         varchar(20)  not null,
    address            varchar(255) null,
    email              varchar(255) null,
    land               varchar(255) null,
    name               varchar(60)  null,
    grama_niladhari_id int          not null,
    constraint fk_stay_location_vs_grama_niladhari
        foreign key (grama_niladhari_id) references grama_niladhari (id)
);

create table if not exists treatment_center
(
    id                    int auto_increment
        primary key,
    created_at            datetime(6)  not null,
    created_by            varchar(20)  not null,
    updated_at            datetime(6)  not null,
    updated_by            varchar(20)  not null,
    name                  varchar(255) null,
    number                varchar(255) null,
    treatment_center_type varchar(255) null,
    ds_office_id          int          not null,
    constraint fk_treatment_center_vs_ds_office
        foreign key (ds_office_id) references ds_office (id)
);

create table if not exists turn
(
    id              int auto_increment
        primary key,
    created_at      datetime(6)  not null,
    created_by      varchar(20)  not null,
    updated_at      datetime(6)  not null,
    updated_by      varchar(20)  not null,
    identified_date date         null,
    person_status   varchar(255) null,
    remark          varchar(255) null,
    person_id       int          not null,
    constraint fk_turn_vs_person
        foreign key (person_id) references person (id)
);

create table if not exists person_location_interact_time
(
    id                   int auto_increment
        primary key,
    created_at           datetime(6)  not null,
    created_by           varchar(20)  not null,
    updated_at           datetime(6)  not null,
    updated_by           varchar(20)  not null,
    arrival_at           datetime(6)  null,
    leave_at             datetime(6)  null,
    stop_active          varchar(255) null,
    location_interact_id int          not null,
    turn_id              int          not null,
    constraint fk_person_location_interact_time_vs_location_interact
        foreign key (location_interact_id) references location_interact (id),
    constraint fk_person_location_interact_time_vs_turn
        foreign key (turn_id) references turn (id)
);

create table if not exists turn_history
(
    id                int auto_increment
        primary key,
    created_at        datetime(6)  not null,
    created_by        varchar(20)  not null,
    updated_at        datetime(6)  not null,
    updated_by        varchar(20)  not null,
    dose_number       int          not null,
    person_status     varchar(255) null,
    remark            varchar(255) null,
    turn_end_at       date         null,
    turn_start_at     date         null,
    vaccinated_date   date         null,
    vaccinated_status varchar(255) null,
    vaccine_name      varchar(255) null,
    stay_location_id  int          not null,
    turn_id           int          not null,
    constraint fk_turn_vs_turn_history
        foreign key (turn_id) references turn (id),
    constraint fk_turn_vs_turn_stay_location
        foreign key (stay_location_id) references stay_location (id)
);

create table if not exists turn_in_history_note
(
    id                          int auto_increment
        primary key,
    created_at                  datetime(6)  not null,
    created_by                  varchar(20)  not null,
    updated_at                  datetime(6)  not null,
    updated_by                  varchar(20)  not null,
    feeling_end_at              datetime(6)  null,
    feeling_start_at            datetime(6)  null,
    person_status               varchar(255) null,
    remark                      varchar(255) null,
    symptoms                    varchar(255) null,
    turn_in_history_note_status varchar(255) null,
    treatment_center_id         int          null,
    turn_history_id             int          not null,
    constraint fk_turn_in_history_note_vs_treatment_center
        foreign key (treatment_center_id) references treatment_center (id),
    constraint fk_turn_in_history_note_vs_turn_history
        foreign key (turn_history_id) references turn_history (id)
);

create table if not exists user_details
(
    id            int auto_increment
        primary key,
    created_at    datetime(6)                   not null,
    created_by    varchar(20)                   not null,
    updated_at    datetime(6)                   not null,
    updated_by    varchar(20)                   not null,
    address       varchar(255) collate utf8_bin null,
    calling_name  varchar(255)                  null,
    date_of_birth date                          null,
    email         varchar(255)                  null,
    gender        varchar(255)                  null,
    land          varchar(255)                  null,
    mobile_one    varchar(10)                   null,
    mobile_two    varchar(255)                  null,
    name          varchar(255)                  null,
    nic           varchar(12)                   null,
    number        varchar(255)                  not null,
    office_email  varchar(255)                  null,
    stop_active   varchar(255)                  null,
    title         varchar(255)                  null,
    constraint UK_4d9rdl7d52k8x3etihxlaujvh
        unique (email),
    constraint UK_k72n5tr2mhtm128rq1sthdufm
        unique (office_email),
    constraint UK_kfucs51ugoh91nkt752qybixg
        unique (nic)
);

create table if not exists user
(
    id              int auto_increment
        primary key,
    created_at      datetime(6)  not null,
    created_by      varchar(20)  not null,
    updated_at      datetime(6)  not null,
    updated_by      varchar(20)  not null,
    enabled         bit          not null,
    password        varchar(255) not null,
    username        varchar(255) not null,
    user_details_id int          not null,
    constraint UK_sb8bbouer5wak8vyiiy4pf2bx
        unique (username),
    constraint fk_user_vs_user_details
        foreign key (user_details_id) references user_details (id)
);

create table if not exists user_details_files
(
    id              int auto_increment
        primary key,
    created_at      datetime(6)  not null,
    created_by      varchar(20)  not null,
    updated_at      datetime(6)  not null,
    updated_by      varchar(20)  not null,
    mime_type       varchar(255) null,
    name            varchar(255) null,
    new_id          varchar(255) null,
    new_name        varchar(255) null,
    pic             longblob     null,
    user_details_id int          not null,
    constraint UK_3pj2kpx26q4j8aevn4h83xvf
        unique (new_id),
    constraint fk_user_details_file_vs_user_details
        foreign key (user_details_id) references user_details (id)
);

create table if not exists user_role
(
    user_id int not null,
    role_id int not null,
    constraint fk_user_role_vs_role
        foreign key (role_id) references role (id),
    constraint fk_user_role_vs_user
        foreign key (user_id) references user (id)
);

create table if not exists user_session_log
(
    id                      int auto_increment
        primary key,
    created_at              datetime(6)  not null,
    failure_attempts        int          not null,
    user_session_log_status varchar(255) null,
    user_id                 int          null,
    constraint FKrhb4wune1hnnhdsbiah2ojo5l
        foreign key (user_id) references user (id)
);

create table if not exists user_treatment_center
(
    id                  int auto_increment
        primary key,
    created_at          datetime(6)  not null,
    created_by          varchar(20)  not null,
    updated_at          datetime(6)  not null,
    updated_by          varchar(20)  not null,
    stop_active         varchar(255) null,
    treatment_center_id int          null,
    user_id             int          null,
    constraint fk_user_treatment_center_vs_treatment_center
        foreign key (treatment_center_id) references treatment_center (id),
    constraint fk_user_treatment_center_vs_user
        foreign key (user_id) references user (id)
);


