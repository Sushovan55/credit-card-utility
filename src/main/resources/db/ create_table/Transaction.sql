CREATE TYPE txn_type AS ENUM ('Debit', 'Credit');
CREATE CAST (VARCHAR AS txn_type) WITH INOUT AS IMPLICIT;

CREATE TYPE cr_type AS ENUM ('Refund', 'BillPayment', 'Cashback', 'Reversal');
CREATE CAST (VARCHAR AS cr_type) WITH INOUT AS IMPLICIT;

CREATE TABLE cc_transaction (
    id SERIAL PRIMARY KEY,
    credit_card_id INTEGER NOT NULL,
    transaction_detail VARCHAR(500),
    transaction_type txn_type NOT NULL DEFAULT 'Debit',
    transaction_no VARCHAR(100),
    transaction_amount DECIMAL(11, 2) NOT NULL,
    transact_on DATE DEFAULT CURRENT_DATE,
    credit_type cr_type,
    acquired_reward DECIMAL(11, 2),
    CONSTRAINT fk_cc_transaction_credit_card FOREIGN KEY (credit_card_id) REFERENCES credit_card (id)
);

CREATE INDEX idx_transact_on ON cc_transaction(transact_on);