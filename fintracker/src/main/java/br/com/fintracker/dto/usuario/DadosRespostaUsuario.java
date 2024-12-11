package br.com.fintracker.dto.usuario;

import br.com.fintracker.model.usuario.Usuario;

public record DadosRespostaUsuario (

        String nome,
        String email
){
    public DadosRespostaUsuario(Usuario usuario) {
        this(usuario.getNome(), usuario.getEmail());
    }

    public DadosRespostaUsuario(DadosUsuario dadosUsuario) {
        this(dadosUsuario.nome(), dadosUsuario.email());
    }
}
