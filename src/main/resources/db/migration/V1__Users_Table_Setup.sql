CREATE TABLE IF NOT EXISTS users (
    id          BIGSERIAL PRIMARY KEY,
    first_name  TEXT NOT NULL,
    last_name   TEXT NOT NULL,
    email       TEXT NOT NULL,
    role        TEXT NOT NULL CHECK (role in ('ADMIN', 'DONOR')) ,
    blood_type  TEXT not null CHECK (blood_type in ('APos', 'ANeg', 'BPos', 'BNeg', 'ABPos', 'ABNeg', 'OPos', 'ONeg'))
);

ALTER TABLE users
ADD CONSTRAINT users_email_unique UNIQUE (email);