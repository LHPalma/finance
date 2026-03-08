CREATE TABLE system_account_type
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    name             VARCHAR(100) NOT NULL,
    description      VARCHAR(255),
    allows_overdraft BOOLEAN      NOT NULL DEFAULT FALSE,
    updated_at       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_active        BOOLEAN      NOT NULL DEFAULT TRUE
);

INSERT INTO system_account_type (name, description, allows_overdraft)
VALUES ('CHECKING', 'Conta Corrente padrão do sistema', true),
       ('SAVINGS', 'Conta Poupança com rendimentos', false),
       ('INVESTMENT', 'Conta de Investimentos / Corretora', false),
       ('WALLET', 'Carteira Física (Dinheiro em Espécie)', false);