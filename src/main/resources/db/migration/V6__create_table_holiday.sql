CREATE TABLE holiday
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    date         DATE         NOT NULL,
    name         VARCHAR(255) NOT NULL,
    country_code VARCHAR(2)   NOT NULL,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    INDEX        idx_holiday_name (name),
    INDEX        idx_holiday_date (date),

    CONSTRAINT uq_holiday_date_country UNIQUE (date, country_code)
);