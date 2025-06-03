-- DDL para as tabelas (já fornecido, mas colocado aqui para referência completa)
-- DROP TABLE IF EXISTS auditoria_usuario;
-- DROP TABLE IF EXISTS avaliacao;
-- DROP TABLE IF EXISTS solicitacao;
-- DROP TABLE IF EXISTS reserva;
-- DROP TABLE IF EXISTS espaco_fisico;
-- DROP TABLE IF EXISTS usuario;

-- CREATE TABLE IF NOT EXISTS usuario (
--     id BIGSERIAL PRIMARY KEY,
--     nome VARCHAR(255) NOT NULL,
--     email VARCHAR(255) UNIQUE NOT NULL,
--     senha VARCHAR(255) NOT NULL,
--     tipo VARCHAR(50) NOT NULL -- ADMIN, GESTOR, PROFESSOR, ALUNO, FUNCIONARIO
-- );

-- CREATE TABLE IF NOT EXISTS espaco_fisico (
--     id BIGSERIAL PRIMARY KEY,
--     nome VARCHAR(255) NOT NULL,
--     localizacao VARCHAR(255),
--     capacidade INT NOT NULL,
--     tipo VARCHAR(50) NOT NULL, -- AUDITORIO, SALA_DE_AULA, etc.
--     disponivel BOOLEAN NOT NULL DEFAULT TRUE
-- );

-- CREATE TABLE IF NOT EXISTS reserva (
--     id BIGSERIAL PRIMARY KEY,
--     usuario_id BIGINT NOT NULL,
--     espaco_id BIGINT NOT NULL,
--     data_hora_inicio TIMESTAMP WITHOUT TIME ZONE NOT NULL,
--     data_hora_fim TIMESTAMP WITHOUT TIME ZONE NOT NULL,
--     status VARCHAR(50) NOT NULL DEFAULT 'PENDENTE', -- PENDENTE, APROVADA, REJEITADA, CANCELADA
--     FOREIGN KEY (usuario_id) REFERENCES usuario(id),
--     FOREIGN KEY (espaco_id) REFERENCES espaco_fisico(id)
-- );

-- CREATE TABLE IF NOT EXISTS solicitacao (
--     id BIGSERIAL PRIMARY KEY,
--     usuario_id BIGINT NOT NULL,
--     espaco_id BIGINT NOT NULL,
--     data_reserva DATE NOT NULL,
--     hora_reserva TIME WITHOUT TIME ZONE NOT NULL,
--     data_solicitacao TIMESTAMP WITHOUT TIME ZONE NOT NULL,
--     status VARCHAR(50) NOT NULL DEFAULT 'PENDENTE', -- PENDENTE, APROVADA, REJEITADA
--     FOREIGN KEY (usuario_id) REFERENCES usuario(id),
--     FOREIGN KEY (espaco_id) REFERENCES espaco_fisico(id)
-- );

-- CREATE TABLE IF NOT EXISTS avaliacao (
--     id BIGSERIAL PRIMARY KEY,
--     solicitacao_id BIGINT UNIQUE NOT NULL, -- Uma avaliação por solicitação
--     gestor_id BIGINT NOT NULL,
--     status VARCHAR(50) NOT NULL, -- APROVADA, REJEITADA
--     justificativa TEXT,
--     data_avaliacao TIMESTAMP WITHOUT TIME ZONE NOT NULL,
--     FOREIGN KEY (solicitacao_id) REFERENCES solicitacao(id),
--     FOREIGN KEY (gestor_id) REFERENCES usuario(id)
-- );

-- CREATE TABLE IF NOT EXISTS auditoria_usuario (
--     id BIGSERIAL PRIMARY KEY,
--     usuario_id BIGINT NOT NULL,
--     acao VARCHAR(255) NOT NULL,
--     data_hora TIMESTAMP WITHOUT TIME ZONE NOT NULL,
--     FOREIGN KEY (usuario_id) REFERENCES usuario(id)
-- );

TRUNCATE TABLE auditoria_usuario RESTART IDENTITY CASCADE;
TRUNCATE TABLE avaliacao RESTART IDENTITY CASCADE;
TRUNCATE TABLE solicitacao RESTART IDENTITY CASCADE;
TRUNCATE TABLE reserva RESTART IDENTITY CASCADE;
TRUNCATE TABLE espaco_fisico RESTART IDENTITY CASCADE;
TRUNCATE TABLE usuario RESTART IDENTITY CASCADE;


INSERT INTO usuario (id, nome, email, senha, tipo) VALUES (1, 'Admin do Sistema', 'admin@gestao.com', '$2a$10$w850c/0U1.Y/j/M.t3q0m.k3q0m.k3q0m.k3q0m.k3q0m.k3q0m.k3q0m.k3q0m.k3q0m.k3q0m.', 'ADMIN');
INSERT INTO usuario (id, nome, email, senha, tipo) VALUES (2, 'Aluno Exemplo', 'aluno@gmail.com', '$2a$10$w850c/0U1.Y/j/M.t3q0m.k3q0m.k3q0m.k3q0m.k3q0m.k3q0m.k3q0m.k3q0m.k3q0m.k3q0m.', 'ALUNO');
INSERT INTO usuario (id, nome, email, senha, tipo) VALUES (3, 'Prof. Ana Silva', 'ana.silva@escola.com', '$2a$10$w850c/0U1.Y/j/M.t3q0m.k3q0m.k3q0m.k3q0m.k3q0m.k3q0m.k3q0m.k3q0m.k3q0m.k3q0m.', 'PROFESSOR');
INSERT INTO usuario (id, nome, email, senha, tipo) VALUES (4, 'Func. João Mendes', 'joao.mendes@escola.com', '$2a$10$w850c/0U1.Y/j/M.t3q0m.k3q0m.k3q0m.k3q0m.k3q0m.k3q0m.k3q0m.k3q0m.k3q0m.k3q0m.', 'FUNCIONARIO');
INSERT INTO usuario (id, nome, email, senha, tipo) VALUES (5, 'Guilherme Novais Vieira', 'gvj624003@gmail.com', '$2a$10$w850c/0U1.Y/j/M.t3q0m.k3q0m.k3q0m.k3q0m.k3q0m.k3q0m.k3q0m.k3q0m.k3q0m.k3q0m.', 'ALUNO');
INSERT INTO usuario (id, nome, email, senha, tipo) VALUES (62, 'Vinicius Marques', 'vinicius@gmail.com', '$2a$10$w850c/0U1.Y/j/M.t3q0m.k3q0m.k3q0m.k3q0m.k3q0m.k3q0m.k3q0m.k3q0m.k3q0m.k3q0m.', 'ALUNO');
INSERT INTO usuario (id, nome, email, senha, tipo) VALUES (63, 'Marcos', 'marcos@gmail.com', '$2a$10$w850c/0U1.Y/j/M.t3q0m.k3q0m.k3q0m.k3q0m.k3q0m.k3q0m.k3q0m.k3q0m.k3q0m.k3q0m.', 'PROFESSOR');
INSERT INTO usuario (id, nome, email, senha, tipo) VALUES (8, 'wagner teofilo', 'wagner@gmail.com', '$2a$10$w850c/0U1.Y/j/M.t3q0m.k3q0m.k3q0m.k3q0m.k3q0m.k3q0m.k3q0m.k3q0m.k3q0m.k3q0m.', 'ALUNO');
INSERT INTO usuario (id, nome, email, senha, tipo) VALUES (64, 'Alex Marcis', 'alex@gmail.com', '$2a$10$w850c/0U1.Y/j/M.t3q0m.k3q0m.k3q0m.k3q0m.k3q0m.k3q0m.k3q0m.k3q0m.k3q0m.k3q0m.', 'ALUNO');


INSERT INTO espaco_fisico (id, nome, localizacao, capacidade, tipo, disponivel) VALUES (101, 'Sala 101', 'Bloco A', 30, 'SALA_DE_AULA', TRUE);
INSERT INTO espaco_fisico (id, nome, localizacao, capacidade, tipo, disponivel) VALUES (102, 'Laboratório de Informática', 'Bloco B', 20, 'LABORATORIO', TRUE);
INSERT INTO espaco_fisico (id, nome, localizacao, capacidade, tipo, disponivel) VALUES (103, 'Auditório Principal', 'Prédio Central', 200, 'AUDITORIO', TRUE);
INSERT INTO espaco_fisico (id, nome, localizacao, capacidade, tipo, disponivel) VALUES (104, 'Sala de Reunião Alpha', 'Andar 3', 8, 'SALA_DE_REUNIAO', TRUE);


INSERT INTO solicitacao (id, usuario_id, espaco_id, data_reserva, hora_reserva, data_solicitacao, status) VALUES
(1, 2, 101, '2025-06-10', '09:00:00', '2025-06-02 14:00:00', 'PENDENTE');

INSERT INTO solicitacao (id, usuario_id, espaco_id, data_reserva, hora_reserva, data_solicitacao, status) VALUES
(2, 3, 102, '2025-06-11', '15:00:00', '2025-06-02 14:30:00', 'PENDENTE');

INSERT INTO solicitacao (id, usuario_id, espaco_id, data_reserva, hora_reserva, data_solicitacao, status) VALUES
(3, 5, 101, '2025-06-10', '10:00:00', '2025-06-02 15:00:00', 'PENDENTE');

INSERT INTO reserva (id, usuario_id, espaco_id, data_hora_inicio, data_hora_fim, status) VALUES
(1, 1, 103, '2025-06-15 10:00:00', '2025-06-15 12:00:00', 'APROVADA');

INSERT INTO reserva (id, usuario_id, espaco_id, data_hora_inicio, data_hora_fim, status) VALUES
(2, 4, 104, '2025-06-20 09:00:00', '2025-06-20 10:30:00', 'PENDENTE');

INSERT INTO avaliacao (id, solicitacao_id, gestor_id, status, justificativa, data_avaliacao) VALUES
(1, 1, 1, 'APROVADA', 'Solicitação aprovada para Sala 101.', '2025-06-02 14:05:00');

INSERT INTO auditoria_usuario (id, usuario_id, acao, data_hora) VALUES (1, 1, 'USUARIO_CRIADO', '2025-06-02 13:50:00');
INSERT INTO auditoria_usuario (id, usuario_id, acao, data_hora) VALUES (2, 2, 'USUARIO_CRIADO', '2025-06-02 13:51:00');
INSERT INTO auditoria_usuario (id, usuario_id, acao, data_hora) VALUES (3, 1, 'SOLICITACAO_CRIADA: ID 1', '2025-06-02 14:00:00');
INSERT INTO auditoria_usuario (id, usuario_id, acao, data_hora) VALUES (4, 1, 'SOLICITACAO_APROVADA: ID 1', '2025-06-02 14:05:00');