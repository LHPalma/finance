CREATE EXTENSION IF NOT EXISTS timescaledb;

CREATE TABLE api_audit_log
(
    id             BIGSERIAL,
    time           TIMESTAMPTZ NOT NULL,
    username       TEXT,
    ip             INET,
    action         TEXT        NOT NULL,
    description    TEXT,
    method_name    TEXT,
    endpoint       TEXT,
    http_method    TEXT,
    status         TEXT        NOT NULL,
    execution_time BIGINT,
    error_message  TEXT,
    user_agent     TEXT,
    request_id     TEXT,
    session_id     TEXT,
    PRIMARY KEY (id, time)
);

SELECT create_hypertable(
               'api_audit_log',
               'time',
               chunk_time_interval => INTERVAL '7 days',
               if_not_exists => TRUE
       );

CREATE INDEX idx_audit_username_time ON api_audit_log (username, time DESC);
CREATE INDEX idx_audit_action_time ON api_audit_log (action, time DESC);
CREATE INDEX idx_audit_status_time ON api_audit_log (status, time DESC);
CREATE INDEX idx_audit_endpoint ON api_audit_log (endpoint, time DESC);

CREATE INDEX idx_audit_error_message ON api_audit_log USING GIN (to_tsvector('english', error_message))
    WHERE error_message IS NOT NULL;

ALTER TABLE api_audit_log
    SET (
        timescaledb.compress,
        timescaledb.compress_segmentby = 'username, action, status',
        timescaledb.compress_orderby = 'time DESC'
        );

SELECT add_compression_policy(
               'api_audit_log',
               INTERVAL '7 days',
               if_not_exists => TRUE
       );

COMMENT ON TABLE api_audit_log IS 'Auditoria automática de ações da API usando TimescaleDB';
COMMENT ON COLUMN api_audit_log.id IS 'Identificador sequencial gerado para mapeamento da JPA/Hibernate';
COMMENT ON COLUMN api_audit_log.time IS 'Timestamp da ação (particionamento principal)';
COMMENT ON COLUMN api_audit_log.execution_time IS 'Tempo de execução em milissegundos';
COMMENT ON COLUMN api_audit_log.action IS 'Identificador da ação (ex: CREATE_USER, DELETE_CLIENT)';