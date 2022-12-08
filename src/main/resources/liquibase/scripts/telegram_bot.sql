--liquibase formatted sql

-- changeset mabelod:1
CREATE TABLE notification_task
(
    id           BIGINT,
    id_chat      BIGINT,
    notification TEXT,
    login        TEXT,
    first_name   TEXT,
    last_name    TEXT,
    date_of_dispatch TIMESTAMP
);


