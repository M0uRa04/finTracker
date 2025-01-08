package br.com.fintracker.service;

import br.com.fintracker.dto.transacao.DadosAtualizacaoTransacao;
import br.com.fintracker.dto.transacao.DadosCadastroTransacao;
import br.com.fintracker.dto.transacao.DadosRespostaTransacao;
import br.com.fintracker.model.transacao.Transacao;
import br.com.fintracker.model.usuario.Usuario;
import br.com.fintracker.repository.CategoriaRepository;
import br.com.fintracker.repository.TransacaoRepository;
import br.com.fintracker.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransacaoService{

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;
    public Long getAuthenticateUserId () throws EntityNotFoundException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = (Usuario) usuarioRepository.findByEmail(email);
        return usuario.getId();
    }

    private Transacao atualizarAtributos(Optional<Transacao> transacao, DadosAtualizacaoTransacao dadosAtualizacao) {
        if (transacao.isPresent()) {
            var transacaoAtualizada = transacao.get();
            if (dadosAtualizacao.tipoTransacao() != null) {
                transacaoAtualizada.setTipoTransacao(dadosAtualizacao.tipoTransacao());
            }
            if (dadosAtualizacao.categoria() != null) {
                transacaoAtualizada.setCategoria(dadosAtualizacao.categoria());
            }
            if (dadosAtualizacao.dataTransacao() != null) {
                transacaoAtualizada.setDataTransacao(dadosAtualizacao.dataTransacao());
            }
            if (dadosAtualizacao.valor() != null) {
                transacaoAtualizada.setValor(dadosAtualizacao.valor());
            }
            if (dadosAtualizacao.descricao() != null) {
                transacaoAtualizada.setDescricao(dadosAtualizacao.descricao());
            }
            transacaoRepository.saveAndFlush(transacaoAtualizada);
            return transacaoAtualizada;
        } else {
            return transacao.get();
        }
    }

    public DadosRespostaTransacao inserirNoBancoDeDados(DadosCadastroTransacao dadosCadastro) {
        var transacao = new Transacao(dadosCadastro);

        var emailUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
        var categoria = categoriaRepository.findByIdAndIsAtivoTrue(dadosCadastro.categoriaId()).orElseThrow(() -> new RuntimeException("Categoria informada inexistente ou inválida"));

        transacao.setUsuario((Usuario) usuarioRepository.findByEmail(emailUsuario));
        transacao.setCategoria(categoria);

        transacaoRepository.saveAndFlush(transacao);
        return new DadosRespostaTransacao(transacao);
    }

    public Optional<DadosRespostaTransacao> buscarTransacaoPorIdEUsuario(Long idTransacao,Long idUsuario) {
        var transacaoProcurada = transacaoRepository.findByIdAndUsuarioId(idTransacao, idUsuario);
        return Optional.of(new DadosRespostaTransacao(transacaoRepository.findByIdAndUsuarioId(idTransacao, idUsuario).orElseThrow(() -> new EntityNotFoundException("Transação não encontrada para o ID fornecido"))));
    }

    public List<DadosRespostaTransacao> listarTodos(Long usuarioId) {
        return transacaoRepository
                .findAllByUsuarioId(usuarioId)
                .stream()
                .map(DadosRespostaTransacao::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public Optional<DadosRespostaTransacao> atualizar(Long idUsuario, Long idTransacao, DadosAtualizacaoTransacao dadosAtualizacao) {
        var transacao = transacaoRepository.findByIdAndUsuarioId(idTransacao, idUsuario);
        var transacaoAtualizada = atualizarAtributos(transacao, dadosAtualizacao);
        transacaoRepository.saveAndFlush(transacaoAtualizada);
        return Optional.of(new DadosRespostaTransacao(transacaoAtualizada));
    }

    @Deprecated
    public void inativar(Long id) {
    }

    public void deletar(Long id) {
        transacaoRepository.deleteById(id);
    }
}
