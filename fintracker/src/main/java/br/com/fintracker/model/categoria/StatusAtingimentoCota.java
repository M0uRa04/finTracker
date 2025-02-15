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
}
