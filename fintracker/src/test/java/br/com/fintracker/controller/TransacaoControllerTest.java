package br.com.fintracker.controller;

import br.com.fintracker.dto.transacao.DadosAtualizacaoTransacao;
import br.com.fintracker.dto.transacao.DadosCadastroTransacao;
import br.com.fintracker.dto.transacao.DadosRespostaTransacao;
import br.com.fintracker.model.transacao.TipoTransacao;
import br.com.fintracker.repository.TransacaoRepository;
import br.com.fintracker.repository.UsuarioRepository;
import br.com.fintracker.service.JWTService;
import br.com.fintracker.service.TransacaoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest(TransacaoController.class)
class TransacaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransacaoService service;

    @MockBean
    private JWTService jwtService;

    @MockBean
    private UsuarioRepository usuarioRepository;

    @MockBean
    private TransacaoRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    private DadosCadastroTransacao dadosCadastro;
    private DadosRespostaTransacao dadosResposta;
    private String token;

    @BeforeEach
    void setUp() {

        token = autenticarUsuario("robson@test.com", "senha123");

        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity()) // Configura o Spring Security no MockMvc
                .build();

        dadosCadastro = new DadosCadastroTransacao(
                TipoTransacao.ENTRADA,
                1L,
                LocalDate.now(),
                BigDecimal.valueOf(1000),
                "Salário"
        );

        dadosRespostaCategoria = new DadosRespostaCategoria(
            1L, 
            1L,
            "Categoria Teste", 
            BigDecimal.valueOf(1500)
            );

        dadosResposta = new DadosRespostaTransacao(
                TipoTransacao.ENTRADA,
                dadosRespostaCategoria, // Substitua por DadosRespostaCategoria se necessário
                LocalDate.now(),
                BigDecimal.valueOf(1000),
                "Salário"
        );
    }

    /*
    Acredito que não é necessária essa parte do código
    @Configuration
    static class TestSecurityConfiguration {
        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http.csrf(AbstractHttpConfigurer::disable); // Desabilita CSRF explicitamente
            return http.build();
        }
    }
    */
    @Test
    void deveInserirTransacaoComSucesso() throws Exception {
        when(service.inserirNoBancoDeDados(ArgumentMatchers.any(DadosCadastroTransacao.class))).thenReturn(dadosResposta);

        mockMvc.perform(post("/transacao")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dadosCadastro)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.transacao", is(dadosResposta.transacao().name())))//ficar de olho aqui
                .andExpect(jsonPath("$.valor", is(dadosResposta.valor().intValue())))
                .andExpect(jsonPath("$.descricao", is(dadosResposta.descricao())));

        verify(service, times(1)).inserirNoBancoDeDados(ArgumentMatchers.any(DadosCadastroTransacao.class));
    }

    @Test
    void deveBuscarTransacaoPorId() throws Exception {
        when(service.buscarTransacaoPorIdEUsuario(eq(1L), eq(1L))).thenReturn(Optional.of(dadosResposta));

        mockMvc.perform(get("/transacao/1")
                       .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transacao", is(dadosResposta.transacao().name())))
                .andExpect(jsonPath("$.valor", is(dadosResposta.valor().intValue())))
                .andExpect(jsonPath("$.descricao", is(dadosResposta.descricao())));

        verify(service, times(1)).buscarTransacaoPorIdEUsuario(eq(1L), eq(1L));
    }

    @Test
    void deveListarTodasTransacoes() throws Exception {
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
    void deveAtualizarTransacao() throws Exception {
        DadosAtualizacaoTransacao dadosAtualizacao = new DadosAtualizacaoTransacao(
                TipoTransacao.SAIDA,
                1L, //verificar se deu certo essa categoria ou se devo passar null
                LocalDate.now(),
                BigDecimal.valueOf(200),
                "Compra no mercado"
        );

        when(service.atualizar(eq(1L), eq(1L), ArgumentMatchers.any(DadosAtualizacaoTransacao.class))).thenReturn(Optional.of(dadosResposta));

        mockMvc.perform(patch("/transacao/1")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dadosAtualizacao)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transacao", is(dadosResposta.transacao().name())))
                .andExpect(jsonPath("$.valor", is(dadosResposta.valor().intValue())))
                .andExpect(jsonPath("$.descricao", is(dadosResposta.descricao())));

        verify(service, times(1)).atualizar(anyLong(), eq(1L), ArgumentMatchers.any(DadosAtualizacaoTransacao.class));
    }

    @Test
    void deveDeletarTransacao() throws Exception {
        doNothing().when(service).deletar(1L);

        mockMvc.perform(delete("/transacao/1")
                .header("Authorization", "Bearer " + token))    
            .andExpect(status().isNoContent());

        verify(service, times(1)).deletar(1L);
    }

    private String autenticarUsuario(String email, String senha) throws Exception {
        String jsonBody = String.format("{\"email\":\"%s\", \"senha\":\"%s\"}", email, senha);

        String responseBody = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode jsonNode = new ObjectMapper().readTree(responseBody);
        return jsonNode.get("token").asText();
    }
}
