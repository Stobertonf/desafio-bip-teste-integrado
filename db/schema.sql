-- Tabela beneficiario
CREATE TABLE IF NOT EXISTS beneficiario (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cpf VARCHAR(11) UNIQUE NOT NULL,
    data_nascimento DATE,
    ativo BOOLEAN DEFAULT true
);

-- Tabela beneficio
CREATE TABLE IF NOT EXISTS beneficio (
    id SERIAL PRIMARY KEY,
    beneficiario_id INTEGER REFERENCES beneficiario(id),
    tipo VARCHAR(50) NOT NULL,
    valor NUMERIC(15,2) NOT NULL,
    saldo NUMERIC(15,2) NOT NULL,
    data_concessao DATE NOT NULL,
    versao INTEGER DEFAULT 0
);
