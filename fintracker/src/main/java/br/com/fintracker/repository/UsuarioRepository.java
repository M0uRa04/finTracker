package br.com.fintracker.repository;

import br.com.fintracker.model.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsuarioRepository extends JpaRepository <Usuario, Long> {

    List<Usuario> findAllByisAtivoTrue();
}
