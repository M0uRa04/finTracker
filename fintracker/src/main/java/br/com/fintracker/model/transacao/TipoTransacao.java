package br.com.fintracker.model.transacao;

public enum TipoTransacao {
    ENTRADA("Entrada"),
    SAIDA("Saída"),
    INVESTIMENTO("Investimento");

    private final String descricao;

    TipoTransacao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public static TipoTransacao fromString(String valor) {
        for (TipoTransacao tipo : TipoTransacao.values()) {
            if (tipo.name().equalsIgnoreCase(valor) || tipo.descricao.equalsIgnoreCase(valor)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Tipo de Transação inválido: " + valor);
    }

}
