create table US_STATE (
    ID varchar(2) not null,
    DESCRIPTION varchar(20),
    primary key (ID)
) ENGINE=INNODB;

insert into US_STATE (DESCRIPTION, ID) values ("Alabama","AL");
insert into US_STATE (DESCRIPTION, ID) values ("Montana","MT");
insert into US_STATE (DESCRIPTION, ID) values ("Alaska","AK");
insert into US_STATE (DESCRIPTION, ID) values ("Nebraska","NE");
insert into US_STATE (DESCRIPTION, ID) values ("Arizona","AZ");
insert into US_STATE (DESCRIPTION, ID) values ("Nevada","NV");
insert into US_STATE (DESCRIPTION, ID) values ("Arkansas","AR");
insert into US_STATE (DESCRIPTION, ID) values ("New Hampshire","NH");
insert into US_STATE (DESCRIPTION, ID) values ("California","CA");
insert into US_STATE (DESCRIPTION, ID) values ("New Jersey","NJ");
insert into US_STATE (DESCRIPTION, ID) values ("Colorado","CO");
insert into US_STATE (DESCRIPTION, ID) values ("New Mexico","NM");
insert into US_STATE (DESCRIPTION, ID) values ("Connecticut","CT");
insert into US_STATE (DESCRIPTION, ID) values ("New York","NY");
insert into US_STATE (DESCRIPTION, ID) values ("Delaware","DE");
insert into US_STATE (DESCRIPTION, ID) values ("North Carolina","NC");
insert into US_STATE (DESCRIPTION, ID) values ("Florida","FL");
insert into US_STATE (DESCRIPTION, ID) values ("North Dakota","ND");
insert into US_STATE (DESCRIPTION, ID) values ("Georgia","GA");
insert into US_STATE (DESCRIPTION, ID) values ("Ohio","OH");
insert into US_STATE (DESCRIPTION, ID) values ("Hawaii","HI");
insert into US_STATE (DESCRIPTION, ID) values ("Oklahoma","OK");
insert into US_STATE (DESCRIPTION, ID) values ("Idaho","ID");
insert into US_STATE (DESCRIPTION, ID) values ("Oregon","OR");
insert into US_STATE (DESCRIPTION, ID) values ("Illinois","IL");
insert into US_STATE (DESCRIPTION, ID) values ("Pennsylvania","PA");
insert into US_STATE (DESCRIPTION, ID) values ("Indiana","IN");
insert into US_STATE (DESCRIPTION, ID) values ("Rhode Island","RI");
insert into US_STATE (DESCRIPTION, ID) values ("Iowa","IA");
insert into US_STATE (DESCRIPTION, ID) values ("South Carolina","SC");
insert into US_STATE (DESCRIPTION, ID) values ("Kansas","KS");
insert into US_STATE (DESCRIPTION, ID) values ("South Dakota","SD");
insert into US_STATE (DESCRIPTION, ID) values ("Kentucky","KY");
insert into US_STATE (DESCRIPTION, ID) values ("Tennessee","TN");
insert into US_STATE (DESCRIPTION, ID) values ("Louisiana","LA");
insert into US_STATE (DESCRIPTION, ID) values ("Texas","TX");
insert into US_STATE (DESCRIPTION, ID) values ("Maine","ME");
insert into US_STATE (DESCRIPTION, ID) values ("Utah","UT");
insert into US_STATE (DESCRIPTION, ID) values ("Maryland","MD");
insert into US_STATE (DESCRIPTION, ID) values ("Vermont","VT");
insert into US_STATE (DESCRIPTION, ID) values ("Massachusetts","MA");
insert into US_STATE (DESCRIPTION, ID) values ("Virginia","VA");
insert into US_STATE (DESCRIPTION, ID) values ("Michigan","MI");
insert into US_STATE (DESCRIPTION, ID) values ("Washington","WA");
insert into US_STATE (DESCRIPTION, ID) values ("Minnesota","MN");
insert into US_STATE (DESCRIPTION, ID) values ("West Virginia","WV");
insert into US_STATE (DESCRIPTION, ID) values ("Mississippi","MS");
insert into US_STATE (DESCRIPTION, ID) values ("Wisconsin","WI");
insert into US_STATE (DESCRIPTION, ID) values ("Missouri","MO");
insert into US_STATE (DESCRIPTION, ID) values ("Wyoming","WY");

create table ADDRESS_TYPE (
    ID varchar(10) not null,
    DESCRIPTION varchar(50) not null,
    primary key (ID)
) ENGINE=INNODB;

insert into ADDRESS_TYPE (DESCRIPTION, ID) values ("Primary Home Address","HOME");
insert into ADDRESS_TYPE (DESCRIPTION, ID) values ("An Arbitrary place that you love","PLACE");

create table ADDRESS (
    ID varchar(50) not null,
    USER_ID varchar(50) not null,
    STREET varchar(100),
    STATE_ID varchar(2) not null,
    ZIPCODE varchar(10) not null,
    ADDRESS_TYPE varchar(10) not null,
    PLACE_NAME varchar(50) not null,
    primary key (ID),
    index USER_ID_IND (USER_ID),
    FOREIGN KEY (USER_ID)
        REFERENCES USER(ID)
        ON DELETE CASCADE,
    index STATE_ID_IND (STATE_ID),
    FOREIGN KEY (STATE_ID)
        REFERENCES US_STATE(ID)
        ON DELETE CASCADE,
    index ADDRESS_TYPE_IND (ADDRESS_TYPE),
    FOREIGN KEY (ADDRESS_TYPE)
        REFERENCES ADDRESS_TYPE(ID)
        ON DELETE CASCADE,
    UNIQUE KEY (USER_ID, PLACE_NAME)
) ENGINE=INNODB;

