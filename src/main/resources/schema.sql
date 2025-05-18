CREATE TABLE users
(
    id       INTEGER PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE wallets
(
    id        INTEGER        NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id   INTEGER        NOT NULL,
    currency  VARCHAR(10)    NOT NULL,
    balance   DECIMAL(20, 8) NOT NULL,
    timestamp TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE trades
(
    id        INTEGER        NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id   INTEGER        NOT NULL,
    symbol    VARCHAR(10)    NOT NULL, -- BTCUSDT or ETHUSDT only
    side      VARCHAR(4)     NOT NULL, -- BUY or SELL
    qty       DECIMAL(20, 8) NOT NULL,
    price     DECIMAL(20, 8) NOT NULL,
    exchange  VARCHAR(20)    NOT NULL,
    timestamp TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE prices
(
    id         INTEGER        NOT NULL AUTO_INCREMENT PRIMARY KEY,
    pair       VARCHAR(10)    NOT NULL,
    bid_price  DECIMAL(20, 8) NOT NULL, -- Best SELL price
    bid_qty    DECIMAL(20, 8) NOT NULL,
    bid_source VARCHAR(10)    NOT NULL, -- best BUY source
    ask_price  DECIMAL(20, 8) NOT NULL, -- Best BUY price
    ask_qty    DECIMAL(20, 8) NOT NULL,
    ask_source VARCHAR(10)    NOT NULL, -- best ASK source
    timestamp  TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP
);
