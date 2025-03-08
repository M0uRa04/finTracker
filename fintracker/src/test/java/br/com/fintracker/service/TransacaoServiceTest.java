package br.com.fintracker.service;

import br.com.fintracker.dto.transacao.DadosAtualizacaoTransacao;
import br.com.fintracker.dto.transacao.DadosCadastroTransacao;
import br.com.fintracker.dto.transacao.DadosRespostaTransacao;
import br.com.fintracker.infra.security.UserContext;
import br.com.fintracker.model.categoria.Categoria;
import br.com.fintracker.model.transacao.TipoTransacao;
import br.com.fintracker.model.transacao.Transacao;
import br.com.fintracker.model.usuario.Usuario;
import br.com.fintracker.repository.CategoriaRepository;
import br.com.fintracker.repository.TransacaoRepository;
import br.com.fintracker.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransacaoServiceTest {

    private TransacaoService service;

    @Mock
    @Autowired
    private TransacaoRepository transacaoRepository;

    @Mock
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Mock
    @Autowired
    private CategoriaRepository categoriaRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new TransacaoService();
//        service.transacaoRepository = transacaoRepository;
//        service.usuarioRepository = usuarioRepository;
//        service.categoriaRepository = categoriaRepository;

        dadosCadastroTransacao = new DadosCadastroTransacao(
                TipoTransacao.ENTRADA,
                1L,
                LocalDate.now(),
                BigDecimal.valueOf(1000),
                "Salário"
        );
    }

    @Test
    void deveInserirTransacaoComSucesso() {
        var usuario = new Usuario(); //verificar esse tipo de instanciação
        usuario.setId(1L);

        var categoria = new Categoria(); //verificar esse tipo de instanciação
        categoria.setId(1L);

        var dadosCadastro = new DadosCadastroTransacao(
                TipoTransacao.ENTRADA,
                1L,
                LocalDate.now(),
                BigDecimal.valueOf(1000),
                "Salário"
        );

        when(UserContext.getUserId()).thenReturn(1L);
        when(categoriaRepository.findByIdAndUsuarioIdAndIsAtivoTrue(1L, 1L)).thenReturn(Optional.of(categoria));
        when(usuarioRepository.findByEmail(anyString())).thenReturn(usuario);
        when(transacaoRepository.save(any(Transacao.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var resposta = service.inserirNoBancoDeDados(dadosCadastro);

        assertNotNull(resposta);
        assertEquals(dadosCadastro.tipoTransacao(), resposta.transacao());
        assertEquals(dadosCadastro.valor(), resposta.valor());
        assertEquals(dadosCadastro.descricao(), resposta.descricao());

        verify(transacaoRepository, times(1)).save(any(Transacao.class));
    }

    @Test
    void deveBuscarTransacaoPorIdEUsuario() {
        var usuarioId = 1L;
        var transacao = new Transacao(dadosCadastroTransacao); //passando um DTO ao invés de setar os valores manualmente
        /*transacao.setId(1L);
        transacao.setTipoTransacao(TipoTransacao.ENTRADA);
        transacao.setValor(BigDecimal.valueOf(1000));
        transacao.setDescricao("Salário");*/

        when(transacaoRepository.findByIdAndUsuarioId(1L, usuarioId)).thenReturn(Optional.of(transacao));

        var resposta = service.buscarTransacaoPorIdEUsuario(1L, usuarioId);

        assertTrue(resposta.isPresent());
        assertEquals(transacao.getTipoTransacao(), resposta.get().transacao());
        verify(transacaoRepository, times(1)).findByIdAndUsuarioId(1L, usuarioId);
    }

    @Test
    void deveListarTodasTransacoesDoUsuario() {
        var usuarioId = 1L;
        var transacoes = List.of(
                new Transacao(BigDecimal.valueOf(1000), LocalDate.now(), null, "Salário", TipoTransacao.ENTRADA, null),
                new Transacao(BigDecimal.valueOf(200), LocalDate.now(), null, "Compra", TipoTransacao.SAIDA, null)
        );

        when(transacaoRepository.findAllByUsuarioId(usuarioId)).thenReturn(transacoes);

        var resposta = service.listarTodos(usuarioId);

        assertNotNull(resposta);
        assertEquals(2, resposta.size());
        verify(transacaoRepository, times(1)).findAllByUsuarioId(usuarioId);
    }

    @Test
    void deveAtualizarTransacao() {
        var usuarioId = 1L;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        var transacao = new Transacao(BigDecimal.valueOf(1000), LocalDate.parse(formatter.format(LocalDate.now())), null, "Salário", TipoTransacao.ENTRADA, null);
        transacao.setId(1L);

        var dadosAtualizacao = new DadosAtualizacaoTransacao(
                TipoTransacao.SAIDA,
                null,
                LocalDate.now(),
                BigDecimal.valueOf(200),
                "Compra no mercado"
        );

        when(transacaoRepository.findByIdAndUsuarioId(1L, usuarioId)).thenReturn(Optional.of(transacao));
        when(transacaoRepository.saveAndFlush(any(Transacao.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var resposta = service.atualizar(usuarioId, 1L, dadosAtualizacao);

        assertTrue(resposta.isPresent());
        assertEquals(dadosAtualizacao.tipoTransacao(), resposta.get().transacao());
        assertEquals(dadosAtualizacao.valor(), resposta.get().valor());
        assertEquals(dadosAtualizacao.descricao(), resposta.get().descricao());

        verify(transacaoRepository, times(1)).saveAndFlush(any(Transacao.class));
    }

    @Test
    void deveDeletarTransacao() {
        doNothing().when(transacaoRepository).deleteByIdAndUsuarioId(1L, 1L);

        service.deletar(1L);

        verify(transacaoRepository, times(1)).deleteByIdAndUsuarioId(1L, UserContext.getUserId());
    }
}
