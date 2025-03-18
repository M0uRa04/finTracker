package br.com.fintracker.controller;

import br.com.fintracker.dto.categoria.DadosRespostaCategoria;
import br.com.fintracker.dto.transacao.DadosAtualizacaoTransacao;
import br.com.fintracker.dto.transacao.DadosCadastroTransacao;
import br.com.fintracker.dto.transacao.DadosRespostaTransacao;
import br.com.fintracker.infra.security.UserContext;
import br.com.fintracker.model.transacao.TipoTransacao;
import br.com.fintracker.model.usuario.Perfis;
import br.com.fintracker.model.usuario.Usuario;
import br.com.fintracker.repository.UsuarioRepository;
import br.com.fintracker.service.JWTService;
import br.com.fintracker.service.TransacaoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransacaoController.class)
@AutoConfigureMockMvc(addFilters = false) // Desativa os filtros de segurança
class TransacaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransacaoService service;

    @MockBean
    private JWTService jwtService;

    @MockBean
    private UsuarioRepository usuarioRepository;

    private DadosCadastroTransacao dadosCadastro;
    private DadosRespostaTransacao dadosResposta;
    private DadosRespostaCategoria dadosRespostaCategoria;
    //private Usuario usuario;
    private String token;
    @BeforeEach
    void setUp() {
        token = "fake-token";
        UserContext.setUserId(1L);

        dadosCadastro = new DadosCadastroTransacao(
                TipoTransacao.ENTRADA,
                1L, // Categoria fictícia
                LocalDate.parse("2025-01-01"),
                BigDecimal.valueOf(1000),
                "Salário"
        );

        dadosRespostaCategoria = new DadosRespostaCategoria(1L, 1L, "Categoria Teste", BigDecimal.valueOf(1500));

        dadosResposta = new DadosRespostaTransacao(
                1L,
                1L,
                TipoTransacao.ENTRADA,
                dadosRespostaCategoria,
                LocalDate.now(),
                BigDecimal.valueOf(1000),
                "Salário"
        );
    }

    @Test
    void deveInserirTransacaoComSucesso() throws Exception {
        when(service.inserirNoBancoDeDados(ArgumentMatchers.any(DadosCadastroTransacao.class)))
                .thenReturn(dadosResposta);

        mockMvc.perform(post("/transacao")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(dadosCadastro)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.transacao", is(dadosResposta.transacao().name())))
                .andExpect(jsonPath("$.valor", is(dadosResposta.valor().intValue())))
                .andExpect(jsonPath("$.descricao", is(dadosResposta.descricao())));

        verify(service, times(1)).inserirNoBancoDeDados(ArgumentMatchers.any(DadosCadastroTransacao.class));
    }

    @Test
    void deveBuscarTransacaoPorIdComSucesso() throws Exception {
        // Simula um retorno válido do serviço
        when(service.buscarTransacaoPorIdEUsuario(eq(1L), anyLong()))
                .thenReturn(Optional.of(dadosResposta));

        mockMvc.perform(get("/transacao/1")
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transacao", is(dadosResposta.transacao().name())))
                .andExpect(jsonPath("$.valor", is(dadosResposta.valor().intValue())))
                .andExpect(jsonPath("$.descricao", is(dadosResposta.descricao())));

        verify(service, times(1)).buscarTransacaoPorIdEUsuario(eq(1L), anyLong());
    }


    @Test
    void deveListarTodasTransacoesComSucesso() throws Exception {
        when(service.listarTodos(eq(1L))).thenReturn(List.of(dadosResposta));

        mockMvc.perform(get("/transacao")
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].transacao", is(dadosResposta.transacao().name())))
                .andExpect(jsonPath("$[0].valor", is(dadosResposta.valor().intValue())))
                .andExpect(jsonPath("$[0].descricao", is(dadosResposta.descricao())));

        verify(service, times(1)).listarTodos(eq(1L));
    }

    @Test
    void deveAtualizarTransacaoComSucesso() throws Exception {
        DadosAtualizacaoTransacao dadosAtualizacao = new DadosAtualizacaoTransacao(
                TipoTransacao.SAIDA,
                null, // Categoria fictícia
                LocalDate.now(),
                BigDecimal.valueOf(200),
                "Compra no mercado"
        );

        when(service.atualizar(anyLong(), anyLong(), ArgumentMatchers.any(DadosAtualizacaoTransacao.class)))
                .thenReturn(Optional.of(dadosResposta));

        mockMvc.perform(patch("/transacao/1")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(dadosAtualizacao)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transacao", is(dadosResposta.transacao().name())))
                .andExpect(jsonPath("$.valor", is(dadosResposta.valor().intValue())))
                .andExpect(jsonPath("$.descricao", is(dadosResposta.descricao())));

        verify(service, times(1)).atualizar(anyLong(), eq(1L), ArgumentMatchers.any(DadosAtualizacaoTransacao.class));
    }

    @Test
    void deveDeletarTransacaoComSucesso() throws Exception {
        doNothing().when(service).deletar(1L);

        mockMvc.perform(delete("/transacao/1"))
                .andExpect(status().isNoContent());

        verify(service, times(1)).deletar(1L);
    }
}
