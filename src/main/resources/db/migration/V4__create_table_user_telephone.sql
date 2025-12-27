CREATE TABLE user_telephone
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id    BIGINT                                                                           NOT NULL,
    area_code  VARCHAR(5)                                                                       NOT NULL,
    telephone  VARCHAR(15)                                                                      NOT NULL,
    type       ENUM ('PERSONAL', 'PROFESSIONAL', 'RESIDENTIAL', 'COMMERCIAL')                   NOT NULL,
    is_primary BOOLEAN                                                                          NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP                                                                        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP                                                                        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    status     ENUM ('ACTIVE', 'INACTIVE', 'UNVERIFIED', 'OUTDATED', 'WRONG_PERSON', 'INVALID') NOT NULL DEFAULT 'ACTIVE',

    CONSTRAINT fk_user_telephone_user
        FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE

);

CREATE INDEX idx_user_telephone_user_id ON user_telephone (user_id);