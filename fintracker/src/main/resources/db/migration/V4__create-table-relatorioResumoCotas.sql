CREATE TABLE relatorio_resumo_cotas (
    id INTEGER NOT NULL AUTO_INCREMENT,
    categoria_id INTEGER,
    total_gasto DECIMAL,
    porcentagem_atingimento FLOAT,
    status_atingimento_cota VARCHAR(30),

    PRIMARY KEY (id),

    constraint fk_relatorio_resumo_cotas_categoria_id foreign key (categoria_id) references categoria(id)

);