package br.com.fintracker.service;

import br.com.fintracker.dto.transacao.DadosAtualizacaoTransacao;
import br.com.fintracker.dto.transacao.DadosCadastroTransacao;
import br.com.fintracker.dto.transacao.DadosRespostaTransacao;
import br.com.fintracker.infra.security.UserContext;
import br.com.fintracker.model.transacao.TipoTransacao;
import br.com.fintracker.model.transacao.Transacao;
import br.com.fintracker.model.usuario.Usuario;
import br.com.fintracker.repository.CategoriaRepository;
import br.com.fintracker.repository.TransacaoRepository;
import br.com.fintracker.repository.UsuarioRepository;
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

    @Autowired
    private CategoriaService categoriaService;
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
            if (!dadosAtualizacao.descricao().isBlank()) {
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
        var categoria = categoriaRepository.findByIdAndUsuarioIdAndIsAtivoTrue(dadosCadastro.categoriaId(), UserContext.getUserId()).orElseThrow(() -> new IllegalArgumentException("Categoria informada inexistente, inválida ou inativa, e/ou usuário inválido."));
//        if (transacao.getTipoTransacao().equals(TipoTransacao.SAIDA)) {
//            categoriaService.atualizaAtingimentoCota(transacao.getValor(), categoria);
//        }
        transacao.setUsuario((Usuario) usuarioRepository.findByEmail(emailUsuario));
        transacao.setCategoria(categoria);

        transacaoRepository.save(transacao);
        return new DadosRespostaTransacao(transacao);
    }

    public Optional<DadosRespostaTransacao> buscarTransacaoPorIdEUsuario(Long idTransacao,Long idUsuario) {
        var transacaoProcurada = transacaoRepository.findByIdAndUsuarioId(idTransacao, idUsuario);
        return Optional.of(new DadosRespostaTransacao(transacaoRepository.findByIdAndUsuarioId(idTransacao, idUsuario).orElseThrow()));
    }

    public List<DadosRespostaTransacao> listarTodos(Long usuarioId) {
        return transacaoRepository
                .findAllByUsuarioId(usuarioId)
                .stream()
                .map(DadosRespostaTransacao::new)
                .collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public Optional<DadosRespostaTransacao> atualizar(Long idUsuario, Long idTransacao, DadosAtualizacaoTransacao dadosAtualizacao) {
        var transacao = transacaoRepository.findByIdAndUsuarioId(idTransacao, idUsuario);
        var transacaoAtualizada = atualizarAtributos(transacao, dadosAtualizacao);
        //transacaoRepository.saveAndFlush(transacaoAtualizada); Duplicidade de salvamento
        return Optional.of(new DadosRespostaTransacao(transacaoAtualizada));
    }

    @Deprecated
    public void inativar(Long id) {
    }

    @Transactional
    public void deletar(Long idTransacao) {
        transacaoRepository.deleteByIdAndUsuarioId(idTransacao, UserContext.getUserId());
    }
}
