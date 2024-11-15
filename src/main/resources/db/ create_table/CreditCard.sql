CREATE TABLE credit_card (
      id SERIAL PRIMARY KEY,
      name VARCHAR(100) NOT NULL,
      display_name VARCHAR(20) NOT NULL,
      bank_id INTEGER NOT NULL,
      card_detail_id INTEGER,
      monthly_limit DECIMAL(11, 2) NOT NULL,
      joining_fee DECIMAL(11, 2) DEFAULT 0,
      annual_fee DECIMAL(11, 2) DEFAULT 0,
      lounge_access BOOLEAN DEFAULT FALSE,
      activated_on DATE DEFAULT CURRENT_DATE,
      billing_start INTEGER NOT NULL,
      billing_end INTEGER NOT NULL,
      status BOOLEAN DEFAULT TRUE,
      CONSTRAINT chk_bill_start_day CHECK (billing_start BETWEEN 1 AND 31),
      CONSTRAINT chk_bill_end_day CHECK (billing_end BETWEEN 1 AND 31),
      CONSTRAINT fk_credit_card_bank FOREIGN KEY (bank_id) REFERENCES bank(id)
  );