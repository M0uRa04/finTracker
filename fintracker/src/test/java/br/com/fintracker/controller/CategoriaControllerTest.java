package br.com.fintracker.controller;

import br.com.fintracker.dto.categoria.DadosAtualizacaoCategoria;
import br.com.fintracker.dto.categoria.DadosCadastroCategoria;
import br.com.fintracker.dto.categoria.DadosRespostaCategoria;
import br.com.fintracker.service.CategoriaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    void buscarPorId_DeveRetornarOkComCategoriaSeEncontrada() {
        long id = 1L;
        DadosRespostaCategoria resposta = new DadosRespostaCategoria("Categoria Teste", BigDecimal.valueOf(1205));

        when(service.buscarPorId(id)).thenReturn(Optional.of(resposta));

        ResponseEntity<DadosRespostaCategoria> response = controller.buscarPorId(id);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(resposta, response.getBody());
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
                new DadosRespostaCategoria("Categoria 1", BigDecimal.valueOf(123021)),
                new DadosRespostaCategoria("Categoria 2", BigDecimal.valueOf(123021))
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
        DadosAtualizacaoCategoria dadosAtualizacao = new DadosAtualizacaoCategoria("Categoria Atualizada", BigDecimal.valueOf(504050450), true);
        DadosRespostaCategoria resposta = new DadosRespostaCategoria("Categoria Atualizada", BigDecimal.valueOf(1032103));

        when(service.atualizar(id, dadosAtualizacao)).thenReturn(Optional.of(resposta));

        ResponseEntity<DadosRespostaCategoria> response = controller.atualizar(id, dadosAtualizacao);

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
