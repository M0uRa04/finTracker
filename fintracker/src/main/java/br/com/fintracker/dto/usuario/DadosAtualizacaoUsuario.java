package br.com.fintracker.dto.usuario;

public record DadosAtualizacaoUsuario (

        Long id,
        String nome,
        String email,
        String senha,
        Boolean isAtivo
){

}
