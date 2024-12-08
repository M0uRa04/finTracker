package br.com.fintracker.dto.usuario;

import br.com.fintracker.model.usuario.Usuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record DadosUsuario(

        @NotBlank
        String nome,
        @Email
        String email,
        @NotBlank
        String senha

) {
    public DadosUsuario(Usuario usuario) {
        this(usuario.getNome(), usuario.getEmail(), usuario.getSenha());
    }
}
