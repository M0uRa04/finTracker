package br.com.fintracker.controller;

import br.com.fintracker.dto.usuario.DadosAtualizacaoUsuario;
import br.com.fintracker.dto.usuario.DadosRespostaUsuario;
import br.com.fintracker.dto.usuario.UsuarioDTO;
import br.com.fintracker.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void criarConta_DeveRetornarCreatedQuandoUsuarioValido() throws Exception {
        UsuarioDTO usuarioDTO = new UsuarioDTO("Robson", "robson@example.com", "123456");
        DadosRespostaUsuario dadosRespostaUsuario = new DadosRespostaUsuario(usuarioDTO);
        Mockito.when(usuarioService.inserirNoBancoDeDados(usuarioDTO)).thenReturn(dadosRespostaUsuario);

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Robson"))
                .andExpect(jsonPath("$.email").value("robson@example.com"));
    }

    @Test
    void criarConta_DeveRetornarBadRequestQuandoDadosInvalidos() throws Exception {
        UsuarioDTO usuarioDTO = new UsuarioDTO("", "emailInvalido", "");

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void buscarContaPeloId_DeveRetornarUsuarioQuandoIdValido() throws Exception {
        Long id = 1L;
        DadosRespostaUsuario dadosRespostaUsuario = new DadosRespostaUsuario("Robson", "robson@example.com");
        Mockito.when(usuarioService.buscarNoBancoDeDadosPeloId(id)).thenReturn(Optional.of(dadosRespostaUsuario));

        mockMvc.perform(get("/user/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Robson"))
                .andExpect(jsonPath("$.email").value("robson@example.com"));
    }

    @Test
    void buscarContaPeloId_DeveRetornarNotFoundQuandoIdNaoExistir() throws Exception {
        Long id = 1L;
        Mockito.when(usuarioService.buscarNoBancoDeDadosPeloId(id)).thenThrow(new RuntimeException("Usuário não encontrado"));

        mockMvc.perform(get("/user/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void listarTodasAsContas_DeveRetornarListaDeUsuarios() throws Exception {
        var usuarios = List.of(
                new DadosRespostaUsuario("Robson", "robson@example.com"),
                new DadosRespostaUsuario("João", "joao@example.com")
        );

        Mockito.when(usuarioService.buscarTodosOsRegistrosNoBancoDeDados()).thenReturn(usuarios);

        mockMvc.perform(get("/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nome").value("Robson"))
                .andExpect(jsonPath("$[1].nome").value("João"));
    }

    @Test
    void atualizarConta_DeveRetornarOkQuandoAtualizacaoValida() throws Exception {
        Long id = 1L;
        DadosAtualizacaoUsuario dados = new DadosAtualizacaoUsuario("Robson Atualizado", "robsonatualizado@email.com", "12659", true);

        Mockito.when(usuarioService.atualizarUsuarioComDadosParciais(id, dados))
                .thenReturn(Optional.of(new DadosRespostaUsuario("Robson Atualizado", "robson@example.com")));

        mockMvc.perform(patch("/user/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dados)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Robson Atualizado"));
    }

    @Test
    void deletarConta_DeveRetornarNoContentQuandoIdValido() throws Exception {
        Long id = 1L;

        Mockito.doNothing().when(usuarioService).removerDoBancoDeDados(id);

        mockMvc.perform(delete("/user/deletar/{id}", id))
                .andExpect(status().isNoContent());
    }


    @Test
    void inativarConta_DeveRetornarNoContentQuandoIdValido() throws Exception {
        Long id = 1L;

        // Simulando que o método retorne um objeto após inativar a conta
        DadosRespostaUsuario dadosRespostaUsuario = new DadosRespostaUsuario("Robson", "robson@example.com");
        Mockito.when(usuarioService.inativarDoBancoDeDados(id)).thenReturn(Optional.of(dadosRespostaUsuario));

        mockMvc.perform(patch("/user/inativar/{id}", id))
                .andExpect(status().isNoContent());
    }



}
