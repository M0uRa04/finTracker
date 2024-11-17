package br.com.fintracker.dto;

import br.com.fintracker.model.usuario.Usuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UsuarioDTO (

        @NotBlank
        String nome,
        @Email
        String email,
        @NotBlank
        String senha

) {
    public UsuarioDTO(Usuario usuario) {
        this(usuario.getNome(), usuario.getEmail(), usuario.getSenha());
    }
}
