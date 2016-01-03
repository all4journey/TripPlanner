create table US_STATE (
    ID varchar(2) not null,
    DESCRIPTION varchar(20),
    primary key (ID)
) ENGINE=INNODB;

insert into US_STATE
values ("NY", "New York");

insert into US_STATE
values ("GA", "Georgia");

insert into US_STATE
values ("PA", "Pennsylvania");

create table ADDRESS (
    ID varchar(50) not null,
    USER_ID varchar(50) not null,
    STREET varchar(100),
    STATE_ID varchar(2) not null,
    ZIPCODE varchar(10),
    primary key (ID),
    index USER_ID_IND (USER_ID),
    FOREIGN KEY (USER_ID)
        REFERENCES USER(ID)
        ON DELETE CASCADE,
    index STATE_ID_IND (STATE_ID),
    FOREIGN KEY (STATE_ID)
        REFERENCES US_STATE(ID)
        ON DELETE CASCADE
) ENGINE=INNODB;

create table VEHICLE (
    ID varchar(50) not null,
    USER_ID varchar(50) not null,
    YEAR varchar(4),
    MAKE varchar(10),
    MODEL varchar(10),
    primary key (ID),
    index USER_ID_IND (USER_ID),
    FOREIGN KEY (USER_ID)
        REFERENCES USER(ID)
        ON DELETE CASCADE
) ENGINE=INNODB;
