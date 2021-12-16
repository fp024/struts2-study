CREATE TABLE IF NOT EXISTS person (
    email           VARCHAR(50) PRIMARY KEY,
    first_name      VARCHAR(50) NOT NULL,
    last_name       VARCHAR(50) NOT NULL,
    age             INT         NOT NULL
);