CREATE TABLE bank_account
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id          BIGINT         NOT NULL,
    name             VARCHAR(100)   NOT NULL,
    description      VARCHAR(255),
    system_type_id   BIGINT         NOT NULL,
    user_category_id BIGINT,
    balance          DECIMAL(15, 2) NOT NULL DEFAULT 0.00,
    overdraft_limit  DECIMAL(15, 2) NOT NULL DEFAULT 0.00,
    currency         VARCHAR(3)     NOT NULL DEFAULT 'BRL',
    created_at       TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_active        BOOLEAN        NOT NULL DEFAULT TRUE,

    CONSTRAINT fk_bank_account_user
        FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE,

    CONSTRAINT fk_bank_account_system_type
        FOREIGN KEY (system_type_id) REFERENCES system_account_type (id) ON DELETE RESTRICT,

    CONSTRAINT fk_bank_account_user_category
        FOREIGN KEY (user_category_id) REFERENCES user_account_category (id) ON DELETE SET NULL
);

CREATE INDEX idx_bank_account_user ON bank_account (user_id);