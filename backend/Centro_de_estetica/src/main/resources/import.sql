-- 1. ÁREAS
INSERT INTO tb_area (id, nome, descricao) VALUES (1, 'Cabelo', 'Cortes, tratamentos e coloração');
INSERT INTO tb_area (id, nome, descricao) VALUES (2, 'Unhas', 'Manicure, pedicure e spa dos pés');
INSERT INTO tb_area (id, nome, descricao) VALUES (3, 'Estética Facial', 'Limpezas, hidratação e harmonização');
INSERT INTO tb_area (id, nome, descricao) VALUES (4, 'Massagem', 'Relaxante, modeladora e drenagem');

-- 2. SERVIÇOS (vinculando à Área)
INSERT INTO tb_servico (id, nome, descricao, valor, tempo_atendimento, id_area) VALUES (1, 'Corte Masculino', 'Degradê na régua', 45.00, '00:30:00', 1);
INSERT INTO tb_servico (id, nome, descricao, valor, tempo_atendimento, id_area) VALUES (2, 'Corte Feminino', 'Corte e escova', 80.00, '01:00:00', 1);
INSERT INTO tb_servico (id, nome, descricao, valor, tempo_atendimento, id_area) VALUES (3, 'Manicure Simples', 'Cuticulagem e Esmaltação', 35.00, '00:45:00', 2);
INSERT INTO tb_servico (id, nome, descricao, valor, tempo_atendimento, id_area) VALUES (4, 'Limpeza de Pele', 'Extração profunda', 120.00, '01:30:00', 3);
INSERT INTO tb_servico (id, nome, descricao, valor, tempo_atendimento, id_area) VALUES (5, 'Drenagem Linfática', 'Massagem corporal', 150.00, '01:00:00', 4);

-- 3. USUÁRIOS
-- ID 1: Admin
INSERT INTO tb_usuario (id_usuario, nome, email, senha, telefone, status_usuario, tipo) VALUES (1, 'Flavio', 'flavio@admin.com', '1234', '31999990001', 'ATIVO', 'ADMIN');

-- ID 2: Funcionária Rafaella
INSERT INTO tb_usuario (id_usuario, nome, email, senha, telefone, status_usuario, tipo) VALUES (2, 'Rafaella Corcini', 'rafaella@estetica.com', '1234', '31999990002', 'ATIVO', 'FUNCIONARIO');

-- ID 3: Funcionária Maria Luiza
INSERT INTO tb_usuario (id_usuario, nome, email, senha, telefone, status_usuario, tipo) VALUES (3, 'Maria Luiza', 'mlpaq@estetica.com', '1234', '31999990003', 'ATIVO', 'FUNCIONARIO');

-- ID 4: Funcionário Rhuan
INSERT INTO tb_usuario (id_usuario, nome, email, senha, telefone, status_usuario, tipo) VALUES (4, 'Rhuan', 'rhuan@barber.com', '1234', '31999990004', 'ATIVO', 'FUNCIONARIO');

-- Clientes (IDs 5 a 9)
INSERT INTO tb_usuario (id_usuario, nome, email, senha, telefone, status_usuario, tipo) VALUES (5, 'Edna Corcini', 'edna@gmail.com', '1234', '31988880001', 'ATIVO', 'CLIENTE');
INSERT INTO tb_usuario (id_usuario, nome, email, senha, telefone, status_usuario, tipo) VALUES (6, 'Keila', 'keila@gmail.com', '1234', '31988880002', 'ATIVO', 'CLIENTE');
INSERT INTO tb_usuario (id_usuario, nome, email, senha, telefone, status_usuario, tipo) VALUES (7, 'Maria de Lourdes', 'lourdes@gmail.com', '1234', '31988880003', 'ATIVO', 'CLIENTE');
INSERT INTO tb_usuario (id_usuario, nome, email, senha, telefone, status_usuario, tipo) VALUES (8, 'Vitor Oliveira', 'vitor02@gmail.com', '1234', '31988880004', 'ATIVO', 'CLIENTE');
INSERT INTO tb_usuario (id_usuario, nome, email, senha, telefone, status_usuario, tipo) VALUES (9, 'Felipe', 'felipe@gmail.com', '1234', '31988880005', 'ATIVO', 'CLIENTE');

-- 4. VINCULOS DE SERVIÇO (Agora seguro pois forçamos os IDs acima)
INSERT INTO tb_usuario_servico (id_usuario_funcionario, id_servico) VALUES (2, 2);
INSERT INTO tb_usuario_servico (id_usuario_funcionario, id_servico) VALUES (2, 3);
INSERT INTO tb_usuario_servico (id_usuario_funcionario, id_servico) VALUES (3, 4);
INSERT INTO tb_usuario_servico (id_usuario_funcionario, id_servico) VALUES (3, 5);
INSERT INTO tb_usuario_servico (id_usuario_funcionario, id_servico) VALUES (4, 1);

-- 5. HORÁRIOS
INSERT INTO tb_horario_usuario (dia_semana, horario_inicio, horario_fim, id_usuario_funcionario) VALUES ('SEG', '09:00:00', '18:00:00', 2);
INSERT INTO tb_horario_usuario (dia_semana, horario_inicio, horario_fim, id_usuario_funcionario) VALUES ('TER', '09:00:00', '18:00:00', 2);
INSERT INTO tb_horario_usuario (dia_semana, horario_inicio, horario_fim, id_usuario_funcionario) VALUES ('QUA', '10:00:00', '19:00:00', 3);
INSERT INTO tb_horario_usuario (dia_semana, horario_inicio, horario_fim, id_usuario_funcionario) VALUES ('QUI', '10:00:00', '19:00:00', 3);
INSERT INTO tb_horario_usuario (dia_semana, horario_inicio, horario_fim, id_usuario_funcionario) VALUES ('SEX', '13:00:00', '20:00:00', 4);
INSERT INTO tb_horario_usuario (dia_semana, horario_inicio, horario_fim, id_usuario_funcionario) VALUES ('SAB', '08:00:00', '14:00:00', 4);

-- 6. AGENDAMENTOS 
INSERT INTO tb_agendamento (id, data_hora, observacoes, status, valor_cobrado, id_cliente, id_funcionario, id_servico) VALUES (1, '2025-12-25T14:00:00', 'Cliente gosta de café', 'AGENDADO', 80.00, 5, 2, 2);

INSERT INTO tb_agendamento (id, data_hora, observacoes, status, valor_cobrado, id_cliente, id_funcionario, id_servico) VALUES (2, '2025-12-26T15:00:00', 'Primeira vez', 'CONCLUIDO', 45.00, 9, 4, 1);

INSERT INTO tb_agendamento (id, data_hora, observacoes, status, valor_cobrado, id_cliente, id_funcionario, id_servico) VALUES (3, '2025-12-27T10:00:00', 'Imprevisto no trabalho', 'CANCELADO', 120.00, 6, 3, 4);

-- 7. REINICIAR OS CONTADORES (Para evitar erro 500 ao criar novo usuário)
ALTER TABLE tb_area ALTER COLUMN id RESTART WITH 5;
ALTER TABLE tb_servico ALTER COLUMN id RESTART WITH 6;
ALTER TABLE tb_usuario ALTER COLUMN id_usuario RESTART WITH 10;
ALTER TABLE tb_agendamento ALTER COLUMN id RESTART WITH 4;
