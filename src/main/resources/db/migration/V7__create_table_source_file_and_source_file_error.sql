CREATE TABLE source_file
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,

    file_name          VARCHAR(255)                                               NOT NULL,
    original_file_name VARCHAR(255),
    storage_path       VARCHAR(500)                                               NOT NULL,

    domain             VARCHAR(100)                                               NOT NULL,
    content_type       VARCHAR(100),

    user_id            BIGINT,

    file_size          BIGINT,
    checksum           VARCHAR(64),

    status             ENUM ('PENDING', 'PROCESSED', 'FAILED', 'PARTIAL_FAILURE') NOT NULL DEFAULT 'PENDING',

    metadata           JSON,

    created_at         TIMESTAMP                                                  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at         TIMESTAMP                                                  NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_source_file_user FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE,
    INDEX idx_source_file_user (user_id),
    INDEX idx_source_file_checksum (checksum),
    INDEX idx_source_file_status (status)
);

CREATE TABLE source_file_error
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    source_file_id     BIGINT    NOT NULL,

    error_message      TEXT      NOT NULL,
    location_reference VARCHAR(255),
    raw_data           TEXT,
    error_details      JSON,

    created_at         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_error_source_file FOREIGN KEY (source_file_id) REFERENCES source_file (id) ON DELETE CASCADE,
    INDEX idx_error_source_file (source_file_id)
);