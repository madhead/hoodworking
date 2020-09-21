CREATE TABLE applications
(
    id          VARCHAR(64) PRIMARY KEY,
    user_id     BIGINT NOT NULL,
    user_name   TEXT   NOT NULL,
    helpfulness TEXT   NOT NULL,
    contact     TEXT   NOT NULL
);

CREATE INDEX ON applications (id);
