create table TOKEN (
    ID varchar(50) not null,
    USER_ID varchar(50) not null,
    TOKEN varchar(50) not null,
    PRIMARY KEY (ID),
    FOREIGN key (USER_ID)
        REFERENCES USER(ID)
        ON DELETE CASCADE
) ENGINE=INNODB;

--Add scheduled event to expire tokens
--CREATE EVENT clearExpired
--ON SCHEDULE AT CURRENT_TIMESTAMP + INTERVAL 2 MINUTES
--DO
--    DELETE FROM TOKEN
--    WHERE TIMESTAMPDIFF(MINUTE, NOW(), TOKEN.TIMESTAMP)>60;