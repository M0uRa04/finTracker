package br.com.fintracker.model.usuario;

public enum Perfis {

    USER ("user"),
    ADMIN("admin");

    private String perfil;

    Perfis(String perfil) {
        this.perfil = perfil;
    }

    public String getPerfil() {
        return perfil;
    }
}
