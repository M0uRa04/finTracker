CREATE TABLE categoria (

    id INTEGER NOT NULL AUTO_INCREMENT,
    nome_categoria VARCHAR(100) NOT NULL,
    cota DECIMAL,
    is_ativo boolean NOT NULL,

    PRIMARY KEY (id)

);