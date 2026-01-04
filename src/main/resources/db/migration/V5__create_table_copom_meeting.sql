CREATE TABLE copom_meeting
(
    id                    BIGINT AUTO_INCREMENT PRIMARY KEY,
    meeting_date          DATE      NOT NULL COMMENT 'Data do segundo dia da reunião (decisão)',
    meeting_number        INT,
    description           VARCHAR(255),

    previous_selic_target DECIMAL(10, 2) COMMENT 'Meta vigente antes da reunião',
    selic_target          DECIMAL(10, 2) COMMENT 'Nova meta definida',
    minutes_url           VARCHAR(255),

    created_at            TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at            TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE INDEX idx_copom_meeting_date ON copom_meeting (meeting_date);