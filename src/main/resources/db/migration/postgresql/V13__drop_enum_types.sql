-- Migração de saneamento: remove tipos legados, caso existam.
-- O schema atual em PostgreSQL usa VARCHAR + CHECK constraints.
DROP TYPE IF EXISTS user_status;
DROP TYPE IF EXISTS user_email_type;
DROP TYPE IF EXISTS user_email_status;
DROP TYPE IF EXISTS user_address_type;
DROP TYPE IF EXISTS zip_code_verification_status;
DROP TYPE IF EXISTS user_address_status;
DROP TYPE IF EXISTS telephone_type;
DROP TYPE IF EXISTS user_telephone_status;
DROP TYPE IF EXISTS source_file_status;
DROP TYPE IF EXISTS vna_status;
