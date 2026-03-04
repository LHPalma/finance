CREATE TABLE vna
(
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    security       VARCHAR(50)    NOT NULL,
    selic_code     VARCHAR(20)    NOT NULL,
    reference_date DATE           NOT NULL,
    price          DECIMAL(25, 8) NOT NULL,
    index_value    DECIMAL(25, 8),

    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uq_vna_selic_date UNIQUE (selic_code, reference_date)
);

CREATE INDEX idx_vna_date ON vna (reference_date);
CREATE INDEX idx_vna_selic ON vna (selic_code);