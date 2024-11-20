package br.com.fintracker.service;

import br.com.fintracker.dto.usuario.DadosAtualizacaoUsuario;
import br.com.fintracker.dto.usuario.DadosRespostaUsuario;
import br.com.fintracker.dto.usuario.UsuarioDTO;
import br.com.fintracker.model.usuario.Usuario;
import br.com.fintracker.repository.UsuarioRepository;
import br.com.fintracker.utils.UsuarioTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
public class UsuarioServiceTest {

    private UsuarioService service;
    private UsuarioRepository repository;


    @BeforeEach
    void setUp() {
        repository = Mockito.mock(UsuarioRepository.class);
        service = new UsuarioService(repository);// Injetar o mock manualmente
    }

    @Test
    void testInserirNoBancoDeDados() {
        Usuario usuario = UsuarioTestUtils.criarUsuario(1L, "John", "john@example.com", "123456", true);
        UsuarioDTO dto = new UsuarioDTO(usuario);

        when(repository.saveAndFlush(any(Usuario.class))).thenReturn(usuario);

        DadosRespostaUsuario result = service.inserirNoBancoDeDados(dto);

        assertNotNull(result);
        assertEquals("John", result.nome());
        verify(repository, times(1)).saveAndFlush(any(Usuario.class));
    }

    @Test
    void testBuscarNoBancoDeDadosPeloId() {
        Usuario usuario = UsuarioTestUtils.criarUsuario(1L, "John", "john@example.com", "123456", true);

        when(repository.findById(1L)).thenReturn(Optional.of(usuario));

        Optional<DadosRespostaUsuario> result = service.buscarNoBancoDeDadosPeloId(1L);

        assertTrue(result.isPresent());
        assertEquals("John", result.get().nome());
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void testBuscarTodosOsRegistrosNoBancoDeDados() {
        Usuario usuario1 = UsuarioTestUtils.criarUsuario(1L, "John", "john@example.com", "123456", true);
        ;
        Usuario usuario2 = UsuarioTestUtils.criarUsuario(2L, "Jane", "jane@example.com", "654321", true);

        when(repository.findAllByisAtivoTrue()).thenReturn(List.of(usuario1, usuario2));

        List<DadosRespostaUsuario> result = service.buscarTodosOsRegistrosNoBancoDeDados();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("John", result.get(0).nome());
        verify(repository, times(1)).findAllByisAtivoTrue();
    }

    @Test
    void testAtualizarUsuarioComDadosParciais() {
        Usuario usuario = UsuarioTestUtils.criarUsuario(1L, "John", "john@example.com", "123456", true);
        DadosAtualizacaoUsuario dtoAtualizacao = new DadosAtualizacaoUsuario("Johnny", null, null, null);

        when(repository.findById(1L)).thenReturn(Optional.of(usuario));
        when(repository.save(any(Usuario.class))).thenReturn(usuario);

        Optional<DadosRespostaUsuario> result = service.atualizarUsuarioComDadosParciais(1L, dtoAtualizacao);

        assertTrue(result.isPresent());
        assertEquals("Johnny", usuario.getNome());
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(usuario);
    }

    @Test
    void testInativarDoBancoDeDados() {
        Usuario usuario = UsuarioTestUtils.criarUsuario(1L, "John", "john@example.com", "123456", true);

        when(repository.findById(1L)).thenReturn(Optional.of(usuario));
        when(repository.save(any(Usuario.class))).thenReturn(usuario);

        Optional<DadosRespostaUsuario> result = service.inativarDoBancoDeDados(1L);

        assertTrue(result.isPresent());
        assertFalse(usuario.getAtivo());
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(usuario);
    }

    @Test
    void testRemoverDoBancoDeDados() {
        doNothing().when(repository).deleteById(1L);

        service.removerDoBancoDeDados(1L);

        verify(repository, times(1)).deleteById(1L);
    }
}
