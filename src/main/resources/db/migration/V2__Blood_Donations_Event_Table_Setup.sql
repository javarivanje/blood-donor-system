CREATE TABLE IF NOT EXISTS blood_donation_event (
    id            BIGSERIAL PRIMARY KEY,
    organizer_id  BIGINT NOT NULL,
    event_name    TEXT NOT NULL,
    event_date    DATE NOT NULL,
    blood_type    TEXT NOT NULL CHECK (blood_type in ('APos', 'ANeg', 'BPos', 'BNeg', 'ABPos', 'ABNeg', 'OPos', 'ONeg')),
    units         INTEGER NOT NULL
);

ALTER TABLE blood_donation_event
ADD CONSTRAINT fk_organizer_id FOREIGN KEY (organizer_id) REFERENCES users(id);