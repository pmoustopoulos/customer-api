--comment: Add version column for optimistic locking (JPA @Version)
ALTER TABLE customers
    ADD COLUMN version BIGINT NOT NULL DEFAULT 0;
