CREATE TABLE categoria (

    id INTEGER NOT NULL AUTO_INCREMENT,
    nome_categoria VARCHAR(100) NOT NULL,
    cota DECIMAL,
    atingimento_cota DECIMAL,
    status_atingimento_cota VARCHAR (50) NOT NULL,
    is_ativo boolean NOT NULL,
    usuario_id INTEGER NOT NULL,
    total_gasto DECIMAL,

    PRIMARY KEY (id),

    constraint fk_categoria_usuario_id foreign key(usuario_id) references usuario(id)

);