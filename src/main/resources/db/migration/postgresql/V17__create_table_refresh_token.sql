CREATE TABLE refresh_token
(
    id                   BIGSERIAL PRIMARY KEY,
    token_id             UUID                     NOT NULL UNIQUE,
    user_id              BIGINT                   NOT NULL,
    expires_at           TIMESTAMP WITH TIME ZONE NOT NULL,
    created_at           TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    revoked_at           TIMESTAMP WITH TIME ZONE,
    replaced_by_token_id UUID,
    CONSTRAINT fk_refresh_token_user
        FOREIGN KEY (user_id) REFERENCES "user" (id)
);

CREATE INDEX idx_refresh_token_user_id ON refresh_token (user_id);
