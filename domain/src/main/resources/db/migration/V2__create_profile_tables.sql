create table ADDRESS (
    USER_ID varchar(50) not null,
    STREET varchar(100),
    STATE_ID varchar(2) not null,
    ZIPCODE varchar(10)
);

create table VEHICLE (
    USER_ID varchar(50) not null,
    YEAR varchar(4),
    MAKE varchar(10),
    MODEL varchar(10)
);

create table US_STATE (
    ID varchar(2) not null primary key,
    DESCRIPTION varchar(20)
);

insert into US_STATE
values ("NY", "New York");

insert into US_STATE
values ("GA", "Georgia");

insert into US_STATE
values ("PA", "Pennsylvania");