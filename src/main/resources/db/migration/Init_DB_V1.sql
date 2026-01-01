-- Drop tables if exists (untuk development)
DROP TABLE IF EXISTS item_loans CASCADE;
DROP TABLE IF EXISTS item_transactions CASCADE;
DROP TABLE IF EXISTS item_owner_stocks CASCADE;
DROP TABLE IF EXISTS items CASCADE;
DROP TABLE IF EXISTS owners CASCADE;
DROP TABLE IF EXISTS users CASCADE;


CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL CHECK (role IN ('ADMIN', 'STAFF', 'AUDITOR')),
    deleted_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_role ON users(role);
CREATE INDEX idx_users_deleted_at ON users(deleted_at) WHERE deleted_at IS NOT NULL;
CREATE INDEX idx_users_created_at ON users(created_at DESC);


CREATE TABLE owners (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL CHECK (type IN ('DIVISION', 'PERSON', 'LOCATION', 'VENDOR')),
    code_owner VARCHAR(100) UNIQUE,
    active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_owners_code_owner ON owners(code_owner);
CREATE INDEX idx_owners_type ON owners(type);
CREATE INDEX idx_owners_active ON owners(active);
CREATE INDEX idx_owners_name ON owners(name);
CREATE INDEX idx_owners_created_at ON owners(created_at DESC);

CREATE TABLE items (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    code_product VARCHAR(100) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    total_quantity INTEGER NOT NULL DEFAULT 0,
    status VARCHAR(50) NOT NULL CHECK (status IN ('AVAILABLE', 'UNAVAILABLE')),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_items_code_product ON items(code_product);
CREATE INDEX idx_items_name ON items(name);
CREATE INDEX idx_items_status ON items(status);
CREATE INDEX idx_items_created_at ON items(created_at DESC);

CREATE TABLE item_owner_stocks (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    item_id UUID NOT NULL,
    owner_id UUID NOT NULL,
    quantity INTEGER NOT NULL DEFAULT 0,
    borrowed_quantity INTEGER DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_item_owner_stocks_item FOREIGN KEY (item_id)
        REFERENCES items(id) ON DELETE CASCADE,
    CONSTRAINT uq_item_owner UNIQUE (item_id, owner_id)
);

CREATE INDEX idx_item_owner_stocks_item_id ON item_owner_stocks(item_id);
CREATE INDEX idx_item_owner_stocks_owner_id ON item_owner_stocks(owner_id);
CREATE INDEX idx_item_owner_stocks_quantity ON item_owner_stocks(quantity);

CREATE TABLE item_transactions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    item_id UUID NOT NULL,
    from_owner_id UUID,
    to_owner_id UUID,
    transaction_type VARCHAR(50) NOT NULL CHECK (transaction_type IN ('INITIAL_ASSIGN', 'BORROW', 'RETURN', 'TRANSFER')),
    quantity INTEGER NOT NULL,
    performed_by_user_id UUID NOT NULL,
    notes TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_item_transactions_item FOREIGN KEY (item_id)
        REFERENCES items(id) ON DELETE CASCADE,
    CONSTRAINT fk_item_transactions_user FOREIGN KEY (performed_by_user_id)
        REFERENCES users(id) ON DELETE RESTRICT
);

CREATE INDEX idx_item_transactions_item_id ON item_transactions(item_id);
CREATE INDEX idx_item_transactions_from_owner ON item_transactions(from_owner_id);
CREATE INDEX idx_item_transactions_to_owner ON item_transactions(to_owner_id);
CREATE INDEX idx_item_transactions_type ON item_transactions(transaction_type);
CREATE INDEX idx_item_transactions_performed_by ON item_transactions(performed_by_user_id);
CREATE INDEX idx_item_transactions_created_at ON item_transactions(created_at DESC);
CREATE INDEX idx_item_transactions_composite ON item_transactions(item_id, created_at DESC);


CREATE TABLE item_loans (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    item_id UUID NOT NULL,
    owner_id UUID NOT NULL,
    borrower_id UUID NOT NULL,
    quantity INTEGER NOT NULL,
    borrowed_at TIMESTAMP NOT NULL,
    due_date TIMESTAMP NOT NULL,
    returned_at TIMESTAMP,
    status VARCHAR(50) NOT NULL CHECK (status IN ('BORROWED', 'RETURNED', 'OVERDUE')),

    CONSTRAINT fk_item_loans_item FOREIGN KEY (item_id)
        REFERENCES items(id) ON DELETE CASCADE
);

CREATE INDEX idx_item_loans_item_id ON item_loans(item_id);
CREATE INDEX idx_item_loans_owner_id ON item_loans(owner_id);
CREATE INDEX idx_item_loans_borrower_id ON item_loans(borrower_id);
CREATE INDEX idx_item_loans_borrowed_at ON item_loans(borrowed_at DESC);
CREATE INDEX idx_item_loans_returned_at ON item_loans(returned_at) WHERE returned_at IS NOT NULL;
