CREATE TABLE categoria (

    id INTEGER NOT NULL AUTO_INCREMENT,
    nome_categoria VARCHAR(100) NOT NULL,
    cota DECIMAL,
    is_ativo boolean NOT NULL,
    usuario_id INTEGER NOT NULL,

    PRIMARY KEY (id),

    constraint fk_categoria_usuario_id foreign key(usuario_id) references usuario(id)

);