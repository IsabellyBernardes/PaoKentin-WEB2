#### https://www.youtube.com/watch?v=Ms5jqhgozJI

# SCRIPT DE CRIAÇÃO DO BANCO DE DADOS PARA O SISTEMA PÃOKENTIN


 -- Opcional: Se precisar criar o banco de dados.
 * CREATE DATABASE PaoKentinDB;
 * GO

-- Garante que todos os comandos seguintes serão executados no contexto do banco correto.
* USE PaoKentinDB;
* GO

## ========= CRIAÇÃO DAS TABELAS =========

CREATE TABLE Pao (
id INT PRIMARY KEY IDENTITY(1,1),
nome VARCHAR(100) NOT NULL,
descricao TEXT,
tempo_preparo_minutos INT NOT NULL,
cor_hex VARCHAR(7) NOT NULL DEFAULT '#0d6efd' 
);
GO

CREATE TABLE Fornada (
id INT PRIMARY KEY IDENTITY(1,1),
pao_id INT NOT NULL,
data_hora_inicio DATETIME NOT NULL,
CONSTRAINT FK_Fornada_Pao FOREIGN KEY (pao_id) REFERENCES Pao(id)
);
GO


## ========= INSERÇÃO DE DADOS INICIAIS =========

PRINT 'Inserindo dados iniciais na tabela Pao...';
INSERT INTO Pao (nome, descricao, tempo_preparo_minutos, cor_hex) VALUES
('Pão Francês', 'Tradicional pão francês, crocante por fora e macio por dentro.', 20, '#fd7e14'),
('Pão Doce', 'Pão macio com uma leve cobertura de açúcar.', 35, '#6f42c1'),
('Pão Carteiro', 'Pão achatado e saboroso, ótimo para sanduíches.', 25, '#198754');
GO
