CREATE TABLE users (
  "user_id" SERIAL PRIMARY KEY,
  "name" VARCHAR(100) NOT NULL,
  "zip_code" VARCHAR(10) NULL
);

CREATE TABLE locations (
  "location_id" SERIAL PRIMARY KEY,
  "zip_code" VARCHAR(10) NOT NULL,
  "latitude" NUMERIC NOT NULL,
  "longitude" NUMERIC NOT NULL
);

INSERT INTO users ("name", "zip_code") VALUES ('Alice', '71937');
INSERT INTO users ("name", "zip_code") VALUES ('Bob', '56171');
INSERT INTO users ("name", "zip_code") VALUES ('Sally', '49430');
