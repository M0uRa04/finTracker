package br.com.fintracker.repository;

import br.com.fintracker.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository <Categoria, Long> {
    Categoria findByNomeCategoria(String nomeCategoria);
}
