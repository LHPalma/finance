CREATE TABLE user_address (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  type VARCHAR(20) NOT NULL,
  street VARCHAR(70) NOT NULL,
  number VARCHAR(10) NOT NULL,
  complement VARCHAR(100),
  neighborhood VARCHAR(100),
  city VARCHAR(100) NOT NULL,
  state VARCHAR(2) NOT NULL,
  zip_code VARCHAR(9) NOT NULL,
  zip_code_verification_status VARCHAR(30) NOT NULL DEFAULT 'NOT_VERIFIED',
  country VARCHAR(3) DEFAULT 'BRA',
  is_primary BOOLEAN DEFAULT FALSE,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',

  CONSTRAINT fk_user_address_user
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
);

CREATE INDEX idx_user_address_user_id ON user_address(user_id);
CREATE INDEX idx_user_address_verification_status ON user_address(zip_code_verification_status);
