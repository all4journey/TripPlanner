create table USER (
    ID varchar(50) not null,
    FIRST_NAME varchar(50) not null,
    LAST_NAME varchar(50) not null,
    EMAIL_ADDRESS varchar(250) not null unique,
    PASSWORD varchar(250) not null,
    REGISTRATION_DATE date not null,
    primary key (ID)
) ENGINE=INNODB;