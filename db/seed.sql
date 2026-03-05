-- Inserir beneficiarios
INSERT INTO beneficiario (nome, cpf, data_nascimento) VALUES
('Jo?o Silva', '12345678901', '1980-05-15'),
('Maria Santos', '98765432101', '1990-08-20'),
('Pedro Oliveira', '11122233344', '1975-03-10')
ON CONFLICT (cpf) DO NOTHING;

-- Inserir beneficios
INSERT INTO beneficio (beneficiario_id, tipo, valor, saldo, data_concessao) VALUES
(1, 'AUXILIO_ALIMENTACAO', 500.00, 500.00, '2024-01-01'),
(1, 'AUXILIO_TRANSPORTE', 300.00, 300.00, '2024-01-01'),
(2, 'AUXILIO_ALIMENTACAO', 500.00, 500.00, '2024-01-01'),
(3, 'AUXILIO_SAUDE', 800.00, 800.00, '2024-01-01')
ON CONFLICT DO NOTHING;
