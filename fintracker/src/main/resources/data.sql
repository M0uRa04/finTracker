-- Inserindo usuários
INSERT INTO usuario (id, nome, email, senha, is_ativo, perfil)
VALUES
(1, 'Alice Santos', 'alice@example.com', '$2a$10$C4Gz.OiLOVYz9n6zR5YX8OfWfl6rUUB.ZZZwABQXJmI.PxxKlyH7e', TRUE, 'USER'),
(2, 'Robson Moura', 'robson@example.com', '$2a$10$C4Gz.OiLOVYz9n6zR5YX8OfWfl6rUUB.ZZZwABQXJmI.PxxKlyH7e', TRUE, 'ADMIN'),
(3, 'Clara Souza', 'clara@example.com', '$2a$10$C4Gz.OiLOVYz9n6zR5YX8OfWfl6rUUB.ZZZwABQXJmI.PxxKlyH7e', TRUE, 'USER'),
(4, 'Daniel Costa', 'daniel@example.com', '$2a$10$C4Gz.OiLOVYz9n6zR5YX8OfWfl6rUUB.ZZZwABQXJmI.PxxKlyH7e', TRUE, 'USER'),
(5, 'Eduarda Silva', 'eduarda@example.com', '$2a$10$C4Gz.OiLOVYz9n6zR5YX8OfWfl6rUUB.ZZZwABQXJmI.PxxKlyH7e', TRUE, 'USER');

-- Inserindo categorias
INSERT INTO categoria (id, nome_categoria, cota, is_ativo)
VALUES
(1, 'Alimentação', 500.00, TRUE),
(2, 'Transporte', 300.00, TRUE),
(3, 'Saúde', 200.00, TRUE),
(4, 'Educação', 400.00, TRUE),
(5, 'Lazer', 250.00, TRUE);
