ALTER TABLE blood_donation_event
ADD CONSTRAINT blood_donation_event_unique UNIQUE (event_name, event_date, blood_type);