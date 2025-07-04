CREATE TABLE refresh_tokens
(
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    token      VARCHAR(255)                            NOT NULL,
    user_email VARCHAR(255)                            NOT NULL,
    expiry_at  TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    CONSTRAINT pk_refresh_tokens PRIMARY KEY (id)
);

ALTER TABLE refresh_tokens
    ADD CONSTRAINT uc_refresh_tokens_token UNIQUE (token);