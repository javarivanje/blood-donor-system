CREATE TABLE IF NOT EXISTS blood_donations (
    id BIGSERIAL PRIMARY KEY,
    donor_id BIGINT NOT NULL,
    admin_id BIGINT NOT NULL,
    units INTEGER NOT NULL ,
    donation_date DATE NOT NULL
);

ALTER TABLE blood_donations
ADD CONSTRAINT fk_donor_id FOREIGN KEY (donor_id) REFERENCES users(id);

ALTER TABLE blood_donations
ADD CONSTRAINT fk_admin_id FOREIGN KEY (admin_id) REFERENCES users(id);

ALTER TABLE blood_donations
ADD CONSTRAINT blood_donations_unique UNIQUE (donor_id, donation_date);