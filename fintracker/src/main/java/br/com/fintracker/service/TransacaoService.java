package br.com.fintracker.service;

import br.com.fintracker.dto.TransacaoDTO;
import br.com.fintracker.model.Transacao;
import br.com.fintracker.repository.TransacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransacaoService implements ApiCRUD <Transacao, Long> {

    @Autowired
    private TransacaoRepository repository;
    @Override
    public Transacao inserirNoBancoDeDados(Transacao transacao) {
        return repository.save(transacao);
    }

    @Override
    public Optional<Transacao> buscarNoBancoDeDados(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Transacao> buscarTodosOsRegistrosNoBancoDeDados() {
        return repository.findAll();
    }

    @Override
    public Optional<Transacao> atualizarNoBancoDeDados(Long id, TransacaoDTO dto) {
        Transacao transacao = repository.getReferenceById(id);
        transacao = new TransacaoDTO(dto);
        return repository.save(transacao);
    }

    @Override
    public Optional<Transacao> removerDoBancoDeDados(Long aLong) {
        return Optional.empty();
    }

    @Override
    public Optional<Transacao> inativarDoBancoDeDados(Long aLong) {
        return Optional.empty();
    }
}
