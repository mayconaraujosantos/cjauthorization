-- Inserindo saldos iniciais para testes
INSERT INTO balance (id, account_id, type, amount)
VALUES (UUID(), '1', 'FOOD', 100.00),
       (UUID(), '1', 'MEAL', 50.00),
       (UUID(), '1', 'CASH', 200.00);
