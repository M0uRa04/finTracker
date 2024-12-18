package br.com.fintracker.repository;

import br.com.fintracker.model.transacao.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransacaoRepository extends JpaRepository <Transacao, Long> {
}
