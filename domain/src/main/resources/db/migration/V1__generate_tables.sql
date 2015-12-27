create table USER (
    ID varchar(50) not null,
    FIRST_NAME varchar(50) not null,
    LAST_NAME varchar(50) not null,
    REGISTRATION_DATE date not null
);

create table ADDRESS (
    USER_ID varchar(50) not null,
    STREET varchar(100),
    STATE_ID varchar(2),
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
    DESCRIPTION varchar(20),
    TIMEZONE_ID varchar(50)
);

create table TIMEZONE (
    ID varchar(50) not null primary key,
    DESCRIPTION varchar(50)
);