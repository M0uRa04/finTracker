package br.com.fintracker.service;

import br.com.fintracker.dto.categoria.DadosAtualizacaoCategoria;
import br.com.fintracker.dto.categoria.DadosCadastroCategoria;
import br.com.fintracker.dto.categoria.DadosRespostaCategoria;
import br.com.fintracker.model.categoria.Categoria;
import br.com.fintracker.repository.CategoriaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoriaServiceTest {

    @Mock
    private CategoriaRepository repository;

    @InjectMocks
    private CategoriaService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void inserirCategoria_DeveInserirNovaCategoria() {
        DadosCadastroCategoria dados = new DadosCadastroCategoria("Alimentação", BigDecimal.valueOf(500));
        Categoria categoria = new Categoria(dados);

        when(repository.findByNomeCategoria("ALIMENTAÇÃO")).thenReturn(null);
        when(repository.saveAndFlush(any(Categoria.class))).thenReturn(categoria);

        DadosRespostaCategoria resposta = service.inserirCategoria(dados);

        assertNotNull(resposta);
        assertEquals("ALIMENTAÇÃO", resposta.nomeCategoria());
        verify(repository, times(1)).saveAndFlush(any(Categoria.class));
    }

    @Test
    void inserirCategoria_DeveLancarExcecaoSeCategoriaJaExistir() {
        DadosCadastroCategoria dados = new DadosCadastroCategoria("Alimentação", BigDecimal.valueOf(500));
        Categoria categoria = new Categoria(dados);

        when(repository.findByNomeCategoria("ALIMENTAÇÃO")).thenReturn(categoria);

        assertThrows(IllegalArgumentException.class, () -> service.inserirCategoria(dados));
        verify(repository, never()).saveAndFlush(any(Categoria.class));
    }

    @Test
    void listarCategorias_DeveRetornarListaDeCategorias() {
        Categoria categoria1 = new Categoria("Alimentação", BigDecimal.valueOf(500), true);
        Categoria categoria2 = new Categoria("Transporte", BigDecimal.valueOf(300), true);

        when(repository.findAll()).thenReturn(Arrays.asList(categoria1, categoria2));

        List<DadosRespostaCategoria> categorias = service.listarCategorias();

        assertNotNull(categorias);
        assertEquals(2, categorias.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void buscarCategoriaPorNome_DeveRetornarCategoriaQuandoExistir() {
        Categoria categoria = new Categoria("Alimentação", BigDecimal.valueOf(500), true);

        when(repository.findByNomeCategoria("ALIMENTAÇÃO")).thenReturn(categoria);

        DadosRespostaCategoria resposta = service.buscarCategoriaPorNome("Alimentação");

        assertNotNull(resposta);
        assertEquals("Alimentação", resposta.nomeCategoria());
        verify(repository, times(1)).findByNomeCategoria("ALIMENTAÇÃO");
    }

    @Test
    void buscarCategoriaPorNome_DeveLancarExcecaoSeNaoExistir() {
        when(repository.findByNomeCategoria("ALIMENTAÇÃO")).thenReturn(null);

        DadosAtualizacaoCategoria dados = new DadosAtualizacaoCategoria("Alimentação", null, null);

        assertThrows(EntityNotFoundException.class, () -> service.buscarCategoriaPorNome(dados.nomeCategoria()));
    }

    @Test
    void inativarCategoria_DeveInativarCategoria() {
        Categoria categoria = new Categoria("Alimentação", BigDecimal.valueOf(500), true);

        when(repository.findByNomeCategoria("ALIMENTAÇÃO")).thenReturn(categoria);

        DadosAtualizacaoCategoria dados = new DadosAtualizacaoCategoria("Alimentação", null, null);

        service.inativarCategoria(dados);

        assertFalse(categoria.getAtivo());
        verify(repository, times(1)).saveAndFlush(categoria);
    }

    @Test
    void inativarCategoria_DeveLancarExcecaoSeCategoriaNaoExistir() {
        when(repository.findByNomeCategoria("ALIMENTAÇÃO")).thenReturn(null);

        DadosAtualizacaoCategoria dados = new DadosAtualizacaoCategoria("Alimentação", null, null);

        assertThrows(EntityNotFoundException.class, () -> service.inativarCategoria(dados));
    }

    @Test
    void deletarCategoria_DeveDeletarCategoria() {
        Categoria categoria = new Categoria("Alimentação", BigDecimal.valueOf(500), true);

        when(repository.findByNomeCategoria("ALIMENTAÇÃO")).thenReturn(categoria);

        DadosAtualizacaoCategoria dados = new DadosAtualizacaoCategoria("Alimentação", null, null);

        service.deletarCategoria(dados);

        verify(repository, times(1)).delete(categoria);
    }

    @Test
    void deletarCategoria_DeveLancarExcecaoSeCategoriaNaoExistir() {
        when(repository.findByNomeCategoria("ALIMENTAÇÃO")).thenReturn(null);

        DadosAtualizacaoCategoria dados = new DadosAtualizacaoCategoria("Alimentação", null, null);

        assertThrows(EntityNotFoundException.class, () -> service.deletarCategoria(dados));
    }
}
