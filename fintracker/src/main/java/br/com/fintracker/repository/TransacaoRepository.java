package br.com.fintracker.repository;

import br.com.fintracker.model.transacao.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransacaoRepository extends JpaRepository <Transacao, Long> {
    Optional<Transacao> findByIdAndUsuarioId(Long idTransacao, Long idUsuario);
    List<Transacao> findAllByUsuarioId(Long idUsuario);
    void deleteByIdAndUsuarioId(Long idTransacao, Long idUsuario);

    void deleteAllByUsuarioId(Long id);
}
