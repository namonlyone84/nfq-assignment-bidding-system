CREATE TABLE user (
  id BIGINT NOT NULL auto_increment,
  userName VARCHAR (45),
  firstName VARCHAR (45),
  lastName VARCHAR (45),
  PRIMARY KEY (ID)
);

CREATE TABLE job (
  id BIGINT NOT NULL,
  caption VARCHAR (512),
  description VARCHAR (1024),
  status VARCHAR (45),
  price BIGINT,
  currencyCode VARCHAR (45),
  userId INT NOT NULL,
  PRIMARY KEY (ID)
);