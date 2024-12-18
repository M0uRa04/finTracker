package br.com.fintracker.controller;

import br.com.fintracker.dto.usuario.DadosAtualizacaoUsuario;
import br.com.fintracker.dto.usuario.DadosRespostaUsuario;
import br.com.fintracker.dto.usuario.DadosUsuario;
import br.com.fintracker.model.usuario.Usuario;
import br.com.fintracker.repository.UsuarioRepository;
import br.com.fintracker.service.JWTService;
import br.com.fintracker.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
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

    @MockBean
    private UsuarioRepository usuarioRepository;


    @MockBean
    private JWTService jwtService;

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void deveAceitarTokenValido() throws Exception {
        DadosUsuario dadosUsuario = new DadosUsuario("Robson", "robson@example.com", "123456");
        String token = jwtService.generateTokenJWT(new Usuario(dadosUsuario));

        mockMvc.perform(get("/login")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void criarConta_DeveRetornarCreatedQuandoUsuarioValido() throws Exception {
        DadosUsuario dadosUsuario = new DadosUsuario("Robson", "robson@example.com", "123456");
        DadosRespostaUsuario dadosRespostaUsuario = new DadosRespostaUsuario(dadosUsuario);
        Mockito.when(usuarioService.inserirNoBancoDeDados(Mockito.eq(dadosUsuario), Mockito.anyString()))
                .thenReturn(dadosRespostaUsuario);

        mockMvc.perform(post("/user")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dadosUsuario)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nomeCategoria").value("Robson"))
                .andExpect(jsonPath("$.email").value("robson@example.com"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void criarConta_DeveRetornarBadRequestQuandoDadosInvalidos() throws Exception {
        DadosUsuario dadosUsuario = new DadosUsuario("", "emailInvalido", "");

        mockMvc.perform(post("/user")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dadosUsuario)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void buscarContaPeloId_DeveRetornarUsuarioQuandoIdValido() throws Exception {
        Long id = 1L;
        DadosRespostaUsuario dadosRespostaUsuario = new DadosRespostaUsuario("Robson", "robson@example.com");
        Mockito.when(usuarioService.buscarNoBancoDeDadosPeloId(id)).thenReturn(Optional.of(dadosRespostaUsuario));

        mockMvc.perform(get("/user/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomeCategoria").value("Robson"))
                .andExpect(jsonPath("$.email").value("robson@example.com"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void buscarContaPeloId_DeveRetornarNotFoundQuandoIdNaoExistir() throws Exception {
        Long id = 1L;
        Mockito.when(usuarioService.buscarNoBancoDeDadosPeloId(id)).thenThrow(new RuntimeException("Usuário não encontrado"));

        mockMvc.perform(get("/user/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void listarTodasAsContas_DeveRetornarListaDeUsuarios() throws Exception {
        var usuarios = List.of(
                new DadosRespostaUsuario("Robson", "robson@example.com"),
                new DadosRespostaUsuario("João", "joao@example.com")
        );

        Mockito.when(usuarioService.buscarTodosOsRegistrosNoBancoDeDados()).thenReturn(usuarios);

        mockMvc.perform(get("/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nomeCategoria").value("Robson"))
                .andExpect(jsonPath("$[1].nomeCategoria").value("João"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void atualizarConta_DeveRetornarOkQuandoAtualizacaoValida() throws Exception {
        Long id = 1L;
        DadosAtualizacaoUsuario dados = new DadosAtualizacaoUsuario("Robson Atualizado", "robsonatualizado@email.com", "12659", true);

        Mockito.when(usuarioService.atualizarUsuarioComDadosParciais(id, dados))
                .thenReturn(Optional.of(new DadosRespostaUsuario("Robson Atualizado", "robson@example.com")));

        mockMvc.perform(patch("/user/{id}", id)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dados)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomeCategoria").value("Robson Atualizado"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void deletarConta_DeveRetornarNoContentQuandoIdValido() throws Exception {
        Long id = 1L;

        Mockito.doNothing().when(usuarioService).removerDoBancoDeDados(id);

        mockMvc.perform(delete("/user/deletar/{id}", id)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void inativarConta_DeveRetornarNoContentQuandoIdValido() throws Exception {
        Long id = 1L;

        DadosRespostaUsuario dadosRespostaUsuario = new DadosRespostaUsuario("Robson", "robson@example.com");
        Mockito.when(usuarioService.inativarDoBancoDeDados(id)).thenReturn(Optional.of(dadosRespostaUsuario));

        mockMvc.perform(patch("/user/inativar/{id}", id)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNoContent());
    }
}
