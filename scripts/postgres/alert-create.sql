
CREATE TABLE alerts (
id uuid PRIMARY KEY,
key jsonb UNIQUE NOT NULL
system_source VARCHAR ( 50 ) UNIQUE NOT NULL,
type VARCHAR ( 50 ) NOT NULL,
template_name VARCHAR ( 50 ) UNIQUE NOT NULL,
template_properties jsonb UNIQUE NOT NULL
created_by VARCHAR ( 100 ) UNIQUE NOT NULL,
create_date TIMESTAMP NOT NULL,
last_updated_by VARCHAR ( 50 ) UNIQUE NOT NULL,
last_update_date TIMESTAMP NOT NULL,
expiration_date TIMESTAMP
);

CREATE INDEX search_key ON alerts USING gin(key);
