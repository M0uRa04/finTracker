package br.com.fintracker.controller;

import br.com.fintracker.dto.categoria.DadosAtualizacaoCategoria;
import br.com.fintracker.dto.categoria.DadosCadastroCategoria;
import br.com.fintracker.dto.categoria.DadosRespostaCategoria;
import br.com.fintracker.service.CategoriaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoriaControllerTest {


    @Mock
    private CategoriaService service;

    @InjectMocks
    private CategoriaController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void inserirNoBancoDeDados_DeveCriarNovaCategoria() {
        DadosCadastroCategoria dados = new DadosCadastroCategoria("Alimentação", BigDecimal.valueOf(500));
        DadosRespostaCategoria resposta = new DadosRespostaCategoria("Alimentação", BigDecimal.valueOf(500));

        when(service.buscarCategoriaPorNome(dados.nomeCategoria())).thenReturn(null);
        when(service.inserirNoBancoDeDados(dados)).thenReturn(resposta);

        ResponseEntity<DadosRespostaCategoria> response = controller.inserirNoBancoDeDados(dados);

        assertNotNull(response);
        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Alimentação", response.getBody().nomeCategoria());
        verify(service, times(1)).inserirCategoria(dados);
    }

    @Test
    void inserirNoBancoDeDados_DeveRetornarBadRequestSeCategoriaJaExistir() {
        DadosCadastroCategoria dados = new DadosCadastroCategoria("Alimentação", BigDecimal.valueOf(500));
        DadosRespostaCategoria respostaExistente = new DadosRespostaCategoria("Alimentação", BigDecimal.valueOf(500));

        when(service.buscarCategoriaPorNome(dados.nomeCategoria())).thenReturn(respostaExistente);

        ResponseEntity<DadosRespostaCategoria> response = controller.inserirNoBancoDeDados(dados);

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
        verify(service, never()).inserirCategoria(dados);
    }

    @Test
    void buscarPorId_DeveRetornarCategoria() {
        Long id = 1L;
        DadosRespostaCategoria resposta = new DadosRespostaCategoria("Alimentação", BigDecimal.valueOf(500));

        when(service.buscarCategoriaPorId(id)).thenReturn(resposta);

        ResponseEntity<DadosRespostaCategoria> response = controller.buscarPorId(id);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Alimentação", response.getBody().nomeCategoria());
        verify(service, times(1)).buscarCategoriaPorId(id);
    }

    @Test
    void listarTodos_DeveRetornarListaDeCategorias() {
        List<DadosRespostaCategoria> categorias = Arrays.asList(
                new DadosRespostaCategoria("Alimentação", BigDecimal.valueOf(500)),
                new DadosRespostaCategoria("Transporte", BigDecimal.valueOf(300))
        );

        when(service.listarCategorias()).thenReturn(categorias);

        ResponseEntity<List<DadosRespostaCategoria>> response = controller.listarTodos();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(service, times(1)).listarCategorias();
    }

    @Test
    void atualizar_DeveAtualizarCategoria() {
        Long id = 1L;
        DadosAtualizacaoCategoria dadosAtualizacao = new DadosAtualizacaoCategoria("Alimentação", BigDecimal.valueOf(600), true);
        DadosRespostaCategoria respostaAtualizada = new DadosRespostaCategoria("Alimentação", BigDecimal.valueOf(600));

        when(service.atualizarCategoria(id, dadosAtualizacao)).thenReturn(respostaAtualizada);

        ResponseEntity<DadosRespostaCategoria> response = controller.atualizar(id, dadosAtualizacao);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(BigDecimal.valueOf(600), response.getBody().cota());
        verify(service, times(1)).atualizarCategoria(id, dadosAtualizacao);
    }

    @Test
    void inativar_DeveInativarCategoria() {
        Long id = 1L;

        DadosAtualizacaoCategoria dadosAtualizacao = new DadosAtualizacaoCategoria("Alimentação", BigDecimal.valueOf(600), true);
        doNothing().when(service).inativarCategoria(id);

        ResponseEntity<Void> response = controller.inativar(id);

        assertNotNull(response);
        assertEquals(204, response.getStatusCodeValue());
        verify(service, times(1)).inativarCategoria(id);
    }

    @Test
    void deletar_DeveDeletarCategoria() {
        DadosAtualizacaoCategoria dadosAtualizacao = new DadosAtualizacaoCategoria("Alimentação", BigDecimal.valueOf(600), true);

        doNothing().when(service).deletarCategoria(1L);

        ResponseEntity<Void> response = controller.deletar(1L);

        assertNotNull(response);
        assertEquals(204, response.getStatusCodeValue());
        verify(service, times(1)).deletarCategoria(1L);
    }
}
