package br.com.fintracker.controller;

import br.com.fintracker.dto.categoria.DadosAtualizacaoCategoria;
import br.com.fintracker.dto.categoria.DadosCadastroCategoria;
import br.com.fintracker.dto.categoria.DadosRespostaCategoria;
import br.com.fintracker.service.CategoriaService;
import jakarta.validation.Valid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoriaControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Mock
    private CategoriaService service;

    @InjectMocks
    private CategoriaController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void inserirNoBancoDeDados_DeveRetornarCreatedComCategoriaInserida() {
        DadosCadastroCategoria dadosCadastroCategoria = new DadosCadastroCategoria("Categoria Teste", BigDecimal.valueOf(1500));
        DadosRespostaCategoria dadosRespostaCategoria = new DadosRespostaCategoria(1L,  1L,"Categoria Teste", BigDecimal.valueOf(1500));

        when(service.inserirNoBancoDeDados(dadosCadastroCategoria)).thenReturn(dadosRespostaCategoria);

        ResponseEntity<DadosRespostaCategoria> response = controller.inserirNoBancoDeDados(dadosCadastroCategoria);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(dadosRespostaCategoria, response.getBody());
        verify(service, times(1)).inserirNoBancoDeDados(dadosCadastroCategoria);
    }

    @Test
    void buscarPorId_DeveRetornarOkComCategoriaSeEncontrada() {
        long id = 1L;
        DadosRespostaCategoria dadosRespostaCategoria = new DadosRespostaCategoria(1L,1L,"Categoria Teste", BigDecimal.valueOf(1500));

        when(service.buscarPorId(id)).thenReturn(Optional.of(dadosRespostaCategoria));

        ResponseEntity<DadosRespostaCategoria> response = controller.buscarPorId(id);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(dadosRespostaCategoria, response.getBody());
        verify(service, times(1)).buscarPorId(id);
    }

    @Test
    void buscarPorId_DeveRetornarNotFoundSeNaoEncontrada() {
        long id = 1L;

        when(service.buscarPorId(id)).thenReturn(Optional.empty());

        ResponseEntity<DadosRespostaCategoria> response = controller.buscarPorId(id);

        assertEquals(404, response.getStatusCodeValue());
        verify(service, times(1)).buscarPorId(id);
    }

    @Test
    void listarTodos_DeveRetornarOkComListaDeCategorias() {
        List<DadosRespostaCategoria> categorias = List.of(
                new DadosRespostaCategoria(1L,1L,"Categoria 1", BigDecimal.valueOf(1200)),
                new DadosRespostaCategoria(2L, 2L,"Categoria 2", BigDecimal.valueOf(1500))
        );

        when(service.listarTodos()).thenReturn(categorias);

        ResponseEntity<List<DadosRespostaCategoria>> response = controller.listarTodos();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(categorias, response.getBody());
        verify(service, times(1)).listarTodos();
    }

    @Test
    void atualizar_DeveRetornarOkComCategoriaAtualizada() {
        long id = 1L;
        DadosAtualizacaoCategoria dadosAtualizacaoCategoria = new DadosAtualizacaoCategoria("Categoria Atualizada", BigDecimal.valueOf(1800), true);
        DadosRespostaCategoria dadosRespostaCategoria = new DadosRespostaCategoria(1L, 1L,"Categoria Atualizada", BigDecimal.valueOf(1800));

        when(service.atualizar(id, dadosAtualizacaoCategoria)).thenReturn(Optional.of(dadosRespostaCategoria));

        ResponseEntity<DadosRespostaCategoria> response = controller.atualizar(id, dadosAtualizacaoCategoria);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(dadosRespostaCategoria, response.getBody());
        verify(service, times(1)).atualizar(id, dadosAtualizacaoCategoria);
    }

    @Test
    void inativar_DeveRetornarNoContent() {
        long id = 1L;

        doNothing().when(service).inativar(id);

        ResponseEntity<Void> response = controller.inativar(id);

        assertEquals(204, response.getStatusCodeValue());
        verify(service, times(1)).inativar(id);
    }

    @Test
    void deletar_DeveRetornarNoContent() {
        long id = 1L;

        doNothing().when(service).deletar(id);

        ResponseEntity<Void> response = controller.deletar(id);

        assertEquals(204, response.getStatusCodeValue());
        verify(service, times(1)).deletar(id);
    }
}
