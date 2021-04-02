DROP SCHEMA IF EXISTS seats_test CASCADE;
CREATE SCHEMA IF NOT EXISTS seats_test AUTHORIZATION seats;
SET SCHEMA 'seats_test';

CREATE TABLE IF NOT EXISTS seats (id SERIAL PRIMARY KEY, is_occupied boolean DEFAULT false, version integer DEFAULT 1);