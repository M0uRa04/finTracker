package br.com.fintracker.service;

import br.com.fintracker.dto.categoria.DadosAtualizacaoCategoria;
import br.com.fintracker.dto.categoria.DadosCadastroCategoria;
import br.com.fintracker.dto.categoria.DadosRespostaCategoria;
import br.com.fintracker.model.categoria.Categoria;
import br.com.fintracker.repository.CategoriaRepository;
import br.com.fintracker.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoriaServiceTest {

    @Mock
    private CategoriaRepository repository;

    @Mock
    private UsuarioRepository usuarioRepository;

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

        when(repository.findByNomeCategoria("ALIMENTAÇÃO")).thenReturn(Optional.empty());  // Mocked findByNomeCategoria to return empty
        when(repository.saveAndFlush(any(Categoria.class))).thenReturn(categoria);

        DadosRespostaCategoria resposta = service.inserirNoBancoDeDados(dados);

        assertNotNull(resposta);
        assertEquals("ALIMENTAÇÃO", resposta.nomeCategoria());
        verify(repository, times(1)).saveAndFlush(any(Categoria.class));
    }

    @Test
    void inserirCategoria_DeveLancarExcecaoSeCategoriaJaExistir() {
        DadosCadastroCategoria dados = new DadosCadastroCategoria("Alimentação", BigDecimal.valueOf(500));
        Categoria categoria = new Categoria(dados);

        when(repository.findByNomeCategoria("ALIMENTAÇÃO")).thenReturn(Optional.of(categoria)); // Mocked to return an existing category

        assertThrows(IllegalArgumentException.class, () -> service.inserirNoBancoDeDados(dados));
        verify(repository, never()).saveAndFlush(any(Categoria.class)); // Ensure save is not called
    }

    @Test
    void listarCategorias_DeveRetornarListaDeCategorias() {
        Categoria categoria1 = new Categoria("Alimentação", BigDecimal.valueOf(500), true);
        Categoria categoria2 = new Categoria("Transporte", BigDecimal.valueOf(300), true);

        when(repository.findAllByUsuarioIdAndIsAtivoTrue(1L)).thenReturn(Optional.of(Arrays.asList(categoria1, categoria2)));  // Assuming findAllByUsuarioIdAndIsAtivoTrue method

        List<DadosRespostaCategoria> categorias = service.listarTodos();

        assertNotNull(categorias);
        assertEquals(2, categorias.size());
        verify(repository, times(1)).findAllByUsuarioIdAndIsAtivoTrue(1L);
    }

    @Test
    void buscarCategoriaPorNome_DeveRetornarCategoriaQuandoExistir() {
        Categoria categoria = new Categoria("Alimentação", BigDecimal.valueOf(500), true);

        when(repository.findByNomeCategoria("ALIMENTAÇÃO")).thenReturn(Optional.of(categoria));

        DadosRespostaCategoria resposta = service.buscarCategoriaPorNome("Alimentação");

        assertNotNull(resposta);
        assertEquals("Alimentação", resposta.nomeCategoria());
        verify(repository, times(1)).findByNomeCategoria("ALIMENTAÇÃO");
    }

    @Test
    void buscarCategoriaPorNome_DeveLancarExcecaoSeNaoExistir() {
        when(repository.findByNomeCategoria("ALIMENTAÇÃO")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.buscarCategoriaPorNome("Alimentação"));
    }

    @Test
    void inativarCategoria_DeveInativarCategoria() {
        Categoria categoria = new Categoria("Alimentação", BigDecimal.valueOf(500), true);

        when(repository.findByIdAndUsuarioIdAndIsAtivoTrue(1L, 1L)).thenReturn(Optional.of(categoria));

        service.inativar(1L);

        assertFalse(categoria.getAtivo());
        verify(repository, times(1)).saveAndFlush(categoria);
    }

    @Test
    void inativarCategoria_DeveLancarExcecaoSeCategoriaNaoExistir() {
        when(repository.findByIdAndUsuarioIdAndIsAtivoTrue(1L, 1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.inativar(1L));
    }

    @Test
    void deletarCategoria_DeveDeletarCategoria() {
        Categoria categoria = new Categoria("Alimentação", BigDecimal.valueOf(500), true);

        when(repository.findByIdAndUsuarioIdAndIsAtivoTrue(1L, 1L)).thenReturn(Optional.of(categoria));

        service.deletar(1L);
        assertFalse(categoria.getAtivo());

        verify(repository, times(1)).delete(categoria);
    }

    @Test
    void deletarCategoria_DeveLancarExcecaoSeCategoriaNaoExistir() {
        when(repository.findByIdAndUsuarioIdAndIsAtivoTrue(1L, 1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.deletar(1L));
    }
}
