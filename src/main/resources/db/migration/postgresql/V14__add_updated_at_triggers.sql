CREATE OR REPLACE FUNCTION set_updated_at_column()
RETURNS TRIGGER AS
$$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_user_set_updated_at ON "user";
CREATE TRIGGER trg_user_set_updated_at
    BEFORE UPDATE ON "user"
    FOR EACH ROW
EXECUTE FUNCTION set_updated_at_column();

DROP TRIGGER IF EXISTS trg_user_email_set_updated_at ON user_email;
CREATE TRIGGER trg_user_email_set_updated_at
    BEFORE UPDATE ON user_email
    FOR EACH ROW
EXECUTE FUNCTION set_updated_at_column();

DROP TRIGGER IF EXISTS trg_user_address_set_updated_at ON user_address;
CREATE TRIGGER trg_user_address_set_updated_at
    BEFORE UPDATE ON user_address
    FOR EACH ROW
EXECUTE FUNCTION set_updated_at_column();

DROP TRIGGER IF EXISTS trg_user_telephone_set_updated_at ON user_telephone;
CREATE TRIGGER trg_user_telephone_set_updated_at
    BEFORE UPDATE ON user_telephone
    FOR EACH ROW
EXECUTE FUNCTION set_updated_at_column();

DROP TRIGGER IF EXISTS trg_copom_meeting_set_updated_at ON copom_meeting;
CREATE TRIGGER trg_copom_meeting_set_updated_at
    BEFORE UPDATE ON copom_meeting
    FOR EACH ROW
EXECUTE FUNCTION set_updated_at_column();

DROP TRIGGER IF EXISTS trg_source_file_set_updated_at ON source_file;
CREATE TRIGGER trg_source_file_set_updated_at
    BEFORE UPDATE ON source_file
    FOR EACH ROW
EXECUTE FUNCTION set_updated_at_column();

DROP TRIGGER IF EXISTS trg_system_account_type_set_updated_at ON system_account_type;
CREATE TRIGGER trg_system_account_type_set_updated_at
    BEFORE UPDATE ON system_account_type
    FOR EACH ROW
EXECUTE FUNCTION set_updated_at_column();

DROP TRIGGER IF EXISTS trg_user_account_category_set_updated_at ON user_account_category;
CREATE TRIGGER trg_user_account_category_set_updated_at
    BEFORE UPDATE ON user_account_category
    FOR EACH ROW
EXECUTE FUNCTION set_updated_at_column();

DROP TRIGGER IF EXISTS trg_bank_account_set_updated_at ON bank_account;
CREATE TRIGGER trg_bank_account_set_updated_at
    BEFORE UPDATE ON bank_account
    FOR EACH ROW
EXECUTE FUNCTION set_updated_at_column();
