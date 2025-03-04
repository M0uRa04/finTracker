package br.com.fintracker.dto.usuario;

import br.com.fintracker.model.usuario.Usuario;

public record DadosRespostaUsuario (
        Long usuarioId,
        String nome,
        String email
){
    public DadosRespostaUsuario(Usuario usuario) {
        this(usuario.getId(),usuario.getNome(), usuario.getEmail());
    }

//    public DadosRespostaUsuario(DadosCadastroUsuario dadosCadastroUsuario) {
//        this(dadosCadastroUsuario.nome(), dadosCadastroUsuario.email());
//    }
}
