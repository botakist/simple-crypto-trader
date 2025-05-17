CREATE TABLE users
(
    id       INTEGER PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE wallets
(
    id         INTEGER PRIMARY KEY,
    user_id    INTEGER        NOT NULL,
    currency   VARCHAR(10)    NOT NULL,
    balance    DECIMAL(20, 8) NOT NULL,
    timestamp TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE trades
(
    id        INTEGER PRIMARY KEY,
    user_id   INTEGER        NOT NULL,
    pair      VARCHAR(10)    NOT NULL, -- BTCUSDT or ETHUSDT
    type      VARCHAR(4)     NOT NULL, -- BUY or SELL
    amount    DECIMAL(20, 8) NOT NULL,
    price     DECIMAL(20, 8) NOT NULL,
    total     DECIMAL(20, 8) NOT NULL,
    timestamp TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE prices
(
    id        INTEGER PRIMARY KEY,
    pair      VARCHAR(10)    NOT NULL,
    bid_price DECIMAL(20, 8) NOT NULL, -- Best SELL price
    ask_price DECIMAL(20, 8) NOT NULL, -- Best BUY price
    source    VARCHAR(50)    NOT NULL, -- Binance or Huobi
    timestamp TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP
);
