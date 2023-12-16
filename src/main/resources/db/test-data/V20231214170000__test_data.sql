INSERT INTO clients (id, first_name, last_name)
VALUES
('84d38aae-ff55-46b9-8d74-ee75a425eba9', 'Jack', 'Johnson'),
('24c8fda4-59ec-4fd6-8b11-d264358f9ea6', 'Anna', 'Bell'),
('7887f298-e914-4ad5-bec9-a8e916e9eede', 'Mike', 'Spike')
;

INSERT INTO accounts (id, client_id, account_number, currency, balance)
VALUES
('efd2661d-96a9-460f-aa5a-9737a450caee', '84d38aae-ff55-46b9-8d74-ee75a425eba9', 'IBAN0001', 'USD', 100.00),
('93131577-fe7a-490e-a776-14dd66aa49ba', '84d38aae-ff55-46b9-8d74-ee75a425eba9', 'IBAN0002', 'EUR', 100.00),
('91212bc1-8d84-4e57-94f5-6c9cc4f8714d', '84d38aae-ff55-46b9-8d74-ee75a425eba9', 'IBAN0003', 'NOK', 100.00),
('2d25b876-4d40-4918-b145-fe806bb01914', '24c8fda4-59ec-4fd6-8b11-d264358f9ea6', 'IBAN0004', 'USD', 200.00),
('36eae1cb-7031-4df0-bf4d-7679e9845d13', '24c8fda4-59ec-4fd6-8b11-d264358f9ea6', 'IBAN0005', 'EUR', 200.00),
('c922d577-0f22-4103-8cc5-b586a530f2a2', '24c8fda4-59ec-4fd6-8b11-d264358f9ea6', 'IBAN0006', 'NOK', 200.00)
;
