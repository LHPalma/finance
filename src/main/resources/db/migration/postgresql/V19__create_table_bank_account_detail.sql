CREATE TABLE bank_account_detail
(
    account_id      BIGINT PRIMARY KEY,
    system_type_id  BIGINT         NOT NULL,
    balance         NUMERIC(15, 2) NOT NULL DEFAULT 0.00,
    overdraft_limit NUMERIC(15, 2) NOT NULL DEFAULT 0.00,
    currency        VARCHAR(3)     NOT NULL DEFAULT 'BRL',

    CONSTRAINT fk_bank_account_detail_account FOREIGN KEY (account_id) REFERENCES account (id) ON DELETE CASCADE,
    CONSTRAINT fk_bank_account_detail_system_type FOREIGN KEY (system_type_id) REFERENCES system_account_type (id) ON DELETE RESTRICT
);


CREATE INDEX idx_bank_account_detail_system_type ON bank_account_detail (system_type_id);