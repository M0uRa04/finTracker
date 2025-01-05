package br.com.fintracker.service;

import br.com.fintracker.dto.transacao.DadosAtualizacaoTransacao;
import br.com.fintracker.dto.transacao.DadosCadastroTransacao;
import br.com.fintracker.dto.transacao.DadosRespostaTransacao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransacaoService implements CrudService <DadosRespostaTransacao, DadosCadastroTransacao, DadosAtualizacaoTransacao> {

    @Override
    public DadosRespostaTransacao inserirNoBancoDeDados(DadosCadastroTransacao dadosCadastro) {
        return null;
    }

    @Override
    public Optional<DadosRespostaTransacao> buscarPorId(Long id) {
        return Optional.empty();
    }

    @Override
    public List<DadosRespostaTransacao> listarTodos() {
        return null;
    }

    @Override
    public Optional<DadosRespostaTransacao> atualizar(Long id, DadosAtualizacaoTransacao dadosAtualizacao) {
        return Optional.empty();
    }

    @Override
    public void inativar(Long id) {

    }

    @Override
    public void deletar(Long id) {

    }
}
