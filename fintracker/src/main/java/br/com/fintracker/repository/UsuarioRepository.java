package br.com.fintracker.repository;

import br.com.fintracker.model.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UsuarioRepository extends JpaRepository <Usuario, Long> {

    UserDetails findByEmail(String email);
    List<Usuario> findAllByisAtivoTrue();
}
