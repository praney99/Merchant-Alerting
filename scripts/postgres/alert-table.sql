
CREATE TABLE alerts 
(
id uuid PRIMARY KEY,
key jsonb UNIQUE NOT NULL
system_source VARCHAR ( 50 ) NOT NULL,
type VARCHAR ( 50 ) NOT NULL,
template_name VARCHAR ( 50 ) UNIQUE NOT NULL,
template_properties jsonb NOT NULL
created_by VARCHAR ( 100 ) NOT NULL,
create_date TIMESTAMP NOT NULL,
last_updated_by VARCHAR ( 50 ) NOT NULL,
last_update_date TIMESTAMP NOT NULL,
expiration_date TIMESTAMP
);

CREATE INDEX search_key 
ON alerts USING gin(key);

CREATE INDEX search
ON alerts (system_source, type);

CREATE TABLE user_alerts 
(
user_ldap VARCHAR ( 50 ) NOT NULL,
alert_id uuid UNIQUE NOT NULL,
is_dismissed boolean,
dismiss_date TIMESTAMP NOT NULL,
PRIMARY KEY (user_ldap, alert_id)
);



