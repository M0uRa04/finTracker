package br.com.fintracker.repository;

import br.com.fintracker.model.categoria.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoriaRepository extends JpaRepository <Categoria, Long> {
    Categoria findByNomeCategoria(String nomeCategoria);

    List<Categoria> findByIsAtivoTrue();

    Optional<Categoria> findByIdAndUsuarioIdAndIsAtivoTrue(Long idCategoria, Long idUsuario);

    Optional<Categoria> findByIdAndUsuarioId(Long id, Long idUsuario);

    Optional<List<Categoria>> findAllByUsuarioIdAndIsAtivoTrue(Long idUsuario);
}
