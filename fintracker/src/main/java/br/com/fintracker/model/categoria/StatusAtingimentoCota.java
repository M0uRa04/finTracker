package br.com.fintracker.model.categoria;

public enum StatusAtingimentoCota {
    DEFAULT ("default"),
    EXCEDIDA ("excedida"),
    ATINGIDA ("atingida"),
    QUASE_ATINGIDA ("quase atingida"),
    DENTRO_DO_ESPERADO ("dentro do esperado");

    private final String status;

    StatusAtingimentoCota(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public static StatusAtingimentoCota fromString(String valor) {
        for (StatusAtingimentoCota tipo : StatusAtingimentoCota.values()) {
            if (tipo.name().equalsIgnoreCase(valor) || tipo.status.equalsIgnoreCase(valor)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Tipo de Transação inválido: " + valor);
    }

    public static StatusAtingimentoCota setStatus(Float porcentagemAtingimento) {
        if (porcentagemAtingimento > 1.0) {
            return EXCEDIDA;
        } else if (porcentagemAtingimento == 1.0) {
            return ATINGIDA;
        } else if (porcentagemAtingimento >= 0.75) {
            return QUASE_ATINGIDA;
        } else {
            return DENTRO_DO_ESPERADO;
        }
    }
}
