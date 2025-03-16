package br.com.fintracker.controller;

import br.com.fintracker.dto.categoria.DadosAtualizacaoCategoria;
import br.com.fintracker.dto.categoria.DadosCadastroCategoria;
import br.com.fintracker.dto.categoria.DadosRespostaCategoria;
import br.com.fintracker.service.CategoriaService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import java.math.BigDecimal;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class CategoriaControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoriaService service;

    @InjectMocks
    private CategoriaController controller;

    private MockMvc mockMvc;
    private String token;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        token = "fake-jwt-token"; // Simulação do token, evitando a chamada real para autenticação
    }

    @Test
    void inserirNoBancoDeDados_DeveRetornarCreatedComCategoriaInserida() throws Exception {
        DadosCadastroCategoria dadosCadastroCategoria = new DadosCadastroCategoria("Categoria Teste", BigDecimal.valueOf(1500));
        DadosRespostaCategoria dadosRespostaCategoria = new DadosRespostaCategoria(1L, 1L, "Categoria Teste", BigDecimal.valueOf(1500));

        when(service.inserirNoBancoDeDados(dadosCadastroCategoria)).thenReturn(dadosRespostaCategoria);

        MvcResult resultado = mockMvc.perform(post("/categoria")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dadosCadastroCategoria)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = resultado.getResponse().getContentAsString();
        DadosRespostaCategoria resposta = objectMapper.readValue(responseBody, DadosRespostaCategoria.class);

        assertEquals(dadosRespostaCategoria, resposta);
        verify(service, times(1)).inserirNoBancoDeDados(dadosCadastroCategoria);
    }

    @Test
    void inserirNoBancoDeDados_SemToken_DeveRetornarUnauthorized() throws Exception {
        DadosCadastroCategoria dadosCadastroCategoria = new DadosCadastroCategoria("Categoria Teste", BigDecimal.valueOf(1500));

        mockMvc.perform(post("/categoria")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dadosCadastroCategoria)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void buscarPorId_DeveRetornarOkComCategoriaSeEncontrada() {
        long id = 1L;
        DadosRespostaCategoria dadosRespostaCategoria = new DadosRespostaCategoria(1L, 1L,"Categoria Teste", BigDecimal.valueOf(1500));

        when(service.buscarPorId(id)).thenReturn(Optional.of(dadosRespostaCategoria));

        ResponseEntity<DadosRespostaCategoria> response = controller.buscarPorId(id);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(dadosRespostaCategoria, response.getBody());
        verify(service, times(1)).buscarPorId(id);
    }

    @Test
    void atualizar_CategoriaNaoEncontrada_DeveRetornarNotFound() throws Exception {
        long id = 99L;
        DadosAtualizacaoCategoria dadosAtualizacaoCategoria = new DadosAtualizacaoCategoria("Nova Categoria", BigDecimal.valueOf(2000), true);

        when(service.atualizar(id, dadosAtualizacaoCategoria)).thenReturn(Optional.empty());

                mockMvc.perform(patch("/categoria/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dadosAtualizacaoCategoria))
                        .header("Authorization", "Bearer " + token))
                                .andExpect(status().isNotFound());

        verify(service, times(1)).atualizar(id, dadosAtualizacaoCategoria);
    }

    @Test
    void inativar_CategoriaJaInativa_DeveRetornarBadRequest() {
        long id = 2L;
        doThrow(new IllegalStateException("Categoria já está inativa."))
                .when(service).inativar(id);

        Exception exception = assertThrows(IllegalStateException.class, () -> controller.inativar(id));
        assertEquals("Categoria já está inativa.", exception.getMessage());
        verify(service, times(1)).inativar(id);
    }
}
