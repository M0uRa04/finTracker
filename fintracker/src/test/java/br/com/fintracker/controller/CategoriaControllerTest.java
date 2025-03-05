package br.com.fintracker.controller;

import br.com.fintracker.dto.categoria.DadosAtualizacaoCategoria;
import br.com.fintracker.dto.categoria.DadosCadastroCategoria;
import br.com.fintracker.dto.categoria.DadosRespostaCategoria;
import br.com.fintracker.service.CategoriaService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CategoriaControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Mock
    private CategoriaService service;

    @InjectMocks
    private CategoriaController controller;

    private MockMvc mockMvc;
    private String token;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this); //preparando e subindo os mocks
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build(); //preparando o controller para receber requisições sem o servidor
        token = autenticarUsuario("robson@test.com", "senha123");
    }

    @Test
    void inserirNoBancoDeDados_DeveRetornarCreatedComCategoriaInserida() {
        DadosCadastroCategoria dadosCadastroCategoria = new DadosCadastroCategoria("Categoria Teste", BigDecimal.valueOf(1500));
        DadosRespostaCategoria dadosRespostaCategoria = new DadosRespostaCategoria("Categoria Teste", BigDecimal.valueOf(1500), 1L);

        when(service.inserirNoBancoDeDados(dadosCadastroCategoria)).thenReturn(dadosRespostaCategoria);

        ResponseEntity<DadosRespostaCategoria> response = controller.inserirNoBancoDeDados(dadosCadastroCategoria);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(dadosRespostaCategoria, response.getBody());
        verify(service, times(1)).inserirNoBancoDeDados(dadosCadastroCategoria);
    }

    @Test
    void buscarPorId_DeveRetornarOkComCategoriaSeEncontrada() {
        long id = 1L;
        DadosRespostaCategoria dadosRespostaCategoria = new DadosRespostaCategoria("Categoria Teste", BigDecimal.valueOf(1500), 1L);

        when(service.buscarPorId(id)).thenReturn(Optional.of(dadosRespostaCategoria));

        ResponseEntity<DadosRespostaCategoria> response = controller.buscarPorId(id);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(dadosRespostaCategoria, response.getBody());
        verify(service, times(1)).buscarPorId(id);
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
