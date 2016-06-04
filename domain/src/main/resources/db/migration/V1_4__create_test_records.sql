INSERT INTO USER values (
  "3770b302-84b1-4099-9957-cf6ca52b50cf",
  "TestUser",
  "TestUser123",
  "testuser@testmail.com",
  "$2a$10$FbWHBq8lZ67oUWjSsXO1F.E32WXi9YzYq9YYPexQCzq5pc6zhjbZW",
  CURDATE()
);

insert into ADDRESS values('ec0a0374-9cab-47d3-8803-bcf3243ec423',
  '3770b302-84b1-4099-9957-cf6ca52b50cf',
  '123 hot street',
  'DE',
  '12340',
  'PLACE',
  'test user test place'
);