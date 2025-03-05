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
    void inserirNoBancoDeDados_DeveRetornarCreatedComCategoriaInserida() throws Exception {
        DadosCadastroCategoria dadosCadastroCategoria = new DadosCadastroCategoria("Categoria Teste", BigDecimal.valueOf(1500));
        DadosRespostaCategoria dadosRespostaCategoria = new DadosRespostaCategoria("Categoria Teste", BigDecimal.valueOf(1500), 1L);
    
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
                        .content(new ObjectMapper().writeValueAsString(dadosCadastroCategoria)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void buscarPorId_DeveRetornarOkComCategoriaSeEncontrada() {
        long id = 1L;
        DadosRespostaCategoria dadosRespostaCategoria = new DadosRespostaCategoria(1L, 1L, "Categoria Teste", BigDecimal.valueOf(1500), 1L);

        when(service.buscarPorId(id)).thenReturn(Optional.of(dadosRespostaCategoria));

        ResponseEntity<DadosRespostaCategoria> response = controller.buscarPorId(id);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(dadosRespostaCategoria, response.getBody());
        verify(service, times(1)).buscarPorId(id);
    }

    @Test
    void atualizar_CategoriaNaoEncontrada_DeveRetornarNotFound() {
        long id = 99L;
        DadosAtualizacaoCategoria dadosAtualizacaoCategoria = new DadosAtualizacaoCategoria("Nova Categoria", BigDecimal.valueOf(2000), true);

        when(service.atualizar(id, dadosAtualizacaoCategoria)).thenReturn(Optional.empty());

        ResponseEntity<DadosRespostaCategoria> response = controller.atualizar(id, dadosAtualizacaoCategoria);

        assertEquals(404, response.getStatusCodeValue());
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
