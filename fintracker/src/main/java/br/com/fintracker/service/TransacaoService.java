package br.com.fintracker.service;

import br.com.fintracker.dto.transacao.DadosAtualizacaoTransacao;
import br.com.fintracker.dto.transacao.DadosCadastroTransacao;
import br.com.fintracker.dto.transacao.DadosRespostaTransacao;
import br.com.fintracker.model.transacao.Transacao;
import br.com.fintracker.model.usuario.Usuario;
import br.com.fintracker.repository.CategoriaRepository;
import br.com.fintracker.repository.TransacaoRepository;
import br.com.fintracker.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransacaoService implements CrudService <DadosRespostaTransacao, DadosCadastroTransacao, DadosAtualizacaoTransacao> {

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Override
    public DadosRespostaTransacao inserirNoBancoDeDados(DadosCadastroTransacao dadosCadastro) {
        var transacao = new Transacao(dadosCadastro);

        var emailUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
        var categoria = categoriaRepository.findByIdAndIsAtivoTrue(dadosCadastro.categoriaId()).orElseThrow(() -> new RuntimeException("Categoria informada inexistente ou inválida"));

        transacao.setUsuario((Usuario) usuarioRepository.findByEmail(emailUsuario));
        transacao.setCategoria(categoria);

        transacaoRepository.saveAndFlush(transacao);
        return new DadosRespostaTransacao(transacao);
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
