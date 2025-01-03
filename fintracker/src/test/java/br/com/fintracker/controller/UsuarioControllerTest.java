package br.com.fintracker.controller;

import br.com.fintracker.dto.usuario.DadosAtualizacaoUsuario;
import br.com.fintracker.dto.usuario.DadosCadastroUsuario;
import br.com.fintracker.dto.usuario.DadosRespostaUsuario;
import br.com.fintracker.model.usuario.Usuario;
import br.com.fintracker.service.UsuarioService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UsuarioControllerTest {

    @Mock
    private UsuarioService service;

    @InjectMocks
    private UsuarioController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void inserirNoBancoDeDados_DeveRetornarBadRequestSeEmailExistir() {
        DadosCadastroUsuario dadosCadastro = new DadosCadastroUsuario("UsuarioTeste1","test@example.com", "password123");

        DadosRespostaUsuario resposta = new DadosRespostaUsuario("UsuarioTeste1", "test@example.com");

        when(service.buscarPeloEmail(dadosCadastro.email())).thenReturn(new Usuario(dadosCadastro));

        ResponseEntity<DadosRespostaUsuario> response = controller.inserirNoBancoDeDados(dadosCadastro);

        assertEquals(400, response.getStatusCodeValue());
        verify(service, times(1)).buscarPeloEmail(dadosCadastro.email());
        verify(service, never()).inserirNoBancoDeDados(dadosCadastro);
    }

    @Test
    void buscarPorId_DeveRetornarOkComUsuarioSeEncontrado() {
        long id = 1L;
        DadosRespostaUsuario resposta = new DadosRespostaUsuario("Test1", "test@example.com");

        when(service.buscarPorId(id)).thenReturn(Optional.of(resposta));

        ResponseEntity<DadosRespostaUsuario> response = controller.buscarPorId(id);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(resposta, response.getBody());
        verify(service, times(1)).buscarPorId(id);
    }

    @Test
    void buscarPorId_DeveRetornarNotFoundSeNaoEncontrado() {
        long id = 1L;

        when(service.buscarPorId(id)).thenReturn(Optional.empty());

        ResponseEntity<DadosRespostaUsuario> response = controller.buscarPorId(id);

        assertEquals(404, response.getStatusCodeValue());
        verify(service, times(1)).buscarPorId(id);
    }

    @Test
    void listarTodos_DeveRetornarOkComListaDeUsuarios() {
        List<DadosRespostaUsuario> usuarios = List.of(
                new DadosRespostaUsuario("Test1", "test1@example.com"),
                new DadosRespostaUsuario("Test2", "test2@example.com")
        );

        when(service.listarTodos()).thenReturn(usuarios);

        ResponseEntity<List<DadosRespostaUsuario>> response = controller.listarTodos();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(usuarios, response.getBody());
        verify(service, times(1)).listarTodos();
    }

    @Test
    void atualizar_DeveRetornarOkComUsuarioAtualizado() {
        long id = 1L;
        DadosAtualizacaoUsuario dadosAtualizacao = new DadosAtualizacaoUsuario(1L, "test","new@example.com","newPassword123",true);
        DadosRespostaUsuario resposta = new DadosRespostaUsuario("test", "new@example.com");

        when(service.atualizar(id, dadosAtualizacao)).thenReturn(Optional.of(resposta));

        ResponseEntity<DadosRespostaUsuario> response = controller.atualizar(id, dadosAtualizacao);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(resposta, response.getBody());
        verify(service, times(1)).atualizar(id, dadosAtualizacao);
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
