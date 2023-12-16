-- Create the Client table
CREATE TABLE clients (
    id UUID PRIMARY KEY NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL
);

-- Create the Accounts table
CREATE TABLE accounts (
    id UUID PRIMARY KEY NOT NULL,
    client_id UUID NOT NULL,
    account_number VARCHAR(34) NOT NULL, -- max iban length
    currency VARCHAR(3) NOT NULL,
    balance DECIMAL(15, 2) DEFAULT 0.0 NOT NULL,
    FOREIGN KEY (client_id) REFERENCES clients(id) ON DELETE CASCADE
);

CREATE INDEX  accounts_client_id_idx
  on accounts (client_id);

-- Create the Transactions table
CREATE TABLE transactions (
    id UUID PRIMARY KEY NOT NULL,
    sender_account_id UUID NOT NULL,
    receiver_account_id UUID NOT NULL,
    amount DECIMAL(15, 2) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    created_date TIMESTAMP NOT NULL,
    FOREIGN KEY (sender_account_id) REFERENCES accounts(id) ON DELETE CASCADE,
    FOREIGN KEY (receiver_account_id) REFERENCES accounts(id) ON DELETE CASCADE
);

CREATE INDEX transactions_accounts_idx
  on transactions (sender_account_id, receiver_account_id);