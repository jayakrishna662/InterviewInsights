ALTER TABLE users
    ADD CONSTRAINT uk_users_roll_number UNIQUE (roll_number);
