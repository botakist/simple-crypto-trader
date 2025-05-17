INSERT INTO users (id, username)
values (1, 'tester');
-- insert initial wallet balance 50000 on US
INSERT INTO wallets(id, user_id, currency, balance, timestamp)
values (1, 1, 'USDT', '50000', CURRENT_TIMESTAMP);
