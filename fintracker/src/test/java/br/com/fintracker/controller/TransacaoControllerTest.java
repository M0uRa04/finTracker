package br.com.fintracker.controller;

import br.com.fintracker.dto.transacao.DadosAtualizacaoTransacao;
import br.com.fintracker.dto.transacao.DadosCadastroTransacao;
import br.com.fintracker.dto.transacao.DadosRespostaTransacao;
import br.com.fintracker.model.transacao.TipoTransacao;
import br.com.fintracker.service.TransacaoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransacaoController.class)
class TransacaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransacaoService service;

    @Autowired
    private ObjectMapper objectMapper;

    private DadosCadastroTransacao dadosCadastro;
    private DadosRespostaTransacao dadosResposta;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        dadosCadastro = new DadosCadastroTransacao(
                TipoTransacao.ENTRADA,
                1L,
                LocalDate.now(),
                BigDecimal.valueOf(1000),
                "Salário"
        );

        dadosResposta = new DadosRespostaTransacao(
                TipoTransacao.ENTRADA,
                null, // Substitua por DadosRespostaCategoria se necessário
                LocalDate.now(),
                BigDecimal.valueOf(1000),
                "Salário"
        );
    }

    @Test
    void deveInserirTransacaoComSucesso() throws Exception {
        when(service.inserirNoBancoDeDados(any(DadosCadastroTransacao.class))).thenReturn(dadosResposta);

        mockMvc.perform(post("/transacao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dadosCadastro)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.transacao", is(dadosResposta.transacao().toString())))
                .andExpect(jsonPath("$.valor", is(dadosResposta.valor().intValue())))
                .andExpect(jsonPath("$.descricao", is(dadosResposta.descricao())));

        verify(service, times(1)).inserirNoBancoDeDados(any(DadosCadastroTransacao.class));
    }

    @Test
    void deveBuscarTransacaoPorId() throws Exception {
        when(service.buscarTransacaoPorIdEUsuario(eq(1L), anyLong())).thenReturn(Optional.of(dadosResposta));

        mockMvc.perform(get("/transacao/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transacao", is(dadosResposta.transacao().toString())))
                .andExpect(jsonPath("$.valor", is(dadosResposta.valor().intValue())))
                .andExpect(jsonPath("$.descricao", is(dadosResposta.descricao())));

        verify(service, times(1)).buscarTransacaoPorIdEUsuario(eq(1L), anyLong());
    }

    @Test
    void deveListarTodasTransacoes() throws Exception {
        when(service.listarTodos(anyLong())).thenReturn(List.of(dadosResposta));

        mockMvc.perform(get("/transacao")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].transacao", is(dadosResposta.transacao().toString())))
                .andExpect(jsonPath("$[0].valor", is(dadosResposta.valor().intValue())))
                .andExpect(jsonPath("$[0].descricao", is(dadosResposta.descricao())));

        verify(service, times(1)).listarTodos(anyLong());
    }

    @Test
    void deveAtualizarTransacao() throws Exception {
        DadosAtualizacaoTransacao dadosAtualizacao = new DadosAtualizacaoTransacao(
                TipoTransacao.SAIDA,
                null,
                LocalDate.now(),
                BigDecimal.valueOf(200),
                "Compra no mercado"
        );

        when(service.atualizar(anyLong(), eq(1L), any(DadosAtualizacaoTransacao.class))).thenReturn(Optional.of(dadosResposta));

        mockMvc.perform(patch("/transacao/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dadosAtualizacao)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transacao", is(dadosResposta.transacao().toString())))
                .andExpect(jsonPath("$.valor", is(dadosResposta.valor().intValue())))
                .andExpect(jsonPath("$.descricao", is(dadosResposta.descricao())));

        verify(service, times(1)).atualizar(anyLong(), eq(1L), any(DadosAtualizacaoTransacao.class));
    }

    @Test
    void deveDeletarTransacao() throws Exception {
        doNothing().when(service).deletar(1L);

        mockMvc.perform(delete("/transacao/1"))
                .andExpect(status().isNoContent());

        verify(service, times(1)).deletar(1L);
    }
}
