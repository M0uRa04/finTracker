CREATE TABLE transacao (

    id INTEGER NOT NULL AUTO_INCREMENT,
    valor DECIMAL NOT NULL,
    data_transacao DATE NOT NULL,
    descricao varchar(200),
    tipo_transacao VARCHAR (40) NOT NULL,
    usuario_id INTEGER NOT NULL,
    categoria_id INTEGER NOT NULL,


    PRIMARY KEY (id),

    constraint fk_transacao_usuario_id foreign key(usuario_id) references usuario(id),
    constraint fk_trasacao_categoria_id foreign key(categoria_id) references categoria(id)

);