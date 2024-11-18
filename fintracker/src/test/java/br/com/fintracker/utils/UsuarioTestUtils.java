package br.com.fintracker.utils;

import br.com.fintracker.model.usuario.Usuario;

public class UsuarioTestUtils {
    public static Usuario criarUsuario(Long id, String nome, String email, String senha, boolean ativo) {
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setSenha(senha);
        usuario.setAtivo(ativo);
        return usuario;
    }
}
