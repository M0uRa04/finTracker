package br.com.fintracker.controller;

import br.com.fintracker.dto.usuario.DadosAtualizacaoUsuario;
import br.com.fintracker.dto.usuario.DadosCadastroUsuario;
import br.com.fintracker.dto.usuario.DadosRespostaUsuario;
import br.com.fintracker.model.usuario.Usuario;
import br.com.fintracker.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @Autowired
    private ObjectMapper objectMapper;

    private DadosCadastroUsuario cadastroUsuario;
    private DadosAtualizacaoUsuario atualizacaoUsuario;
    private DadosRespostaUsuario respostaUsuario;
    

    @BeforeEach
    void setUp() {
        cadastroUsuario = new DadosCadastroUsuario("john.doe@example.com", "password123", "John Doe");
        atualizacaoUsuario = new DadosAtualizacaoUsuario(1L, "John Updated", "john.doe@example.com", "John Updated", true);
        respostaUsuario = new DadosRespostaUsuario("John Updated", "john.doe@example.com");
    }

    @Test
    void deveInserirUsuarioComSucesso() throws Exception {
        Mockito.when(usuarioService.buscarPeloEmail(anyString())).thenReturn(null);
        Mockito.when(usuarioService.inserirNoBancoDeDados(any(DadosCadastroUsuario.class), anyString()))
                .thenReturn(respostaUsuario);

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cadastroUsuario)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.nome").value("John Updated"));
    }

    @Test
    void deveRetornarBadRequestSeEmailJaExistir() throws Exception {
        Mockito.when(usuarioService.buscarPeloEmail(anyString())).thenReturn(new Usuario());

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cadastroUsuario)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornarUsuarioPorId() throws Exception {
        Mockito.when(usuarioService.buscarNoBancoDeDadosPeloId(anyLong()))
                .thenReturn(Optional.of(respostaUsuario));

        mockMvc.perform(get("/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.nome").value("John Updated"));
    }

    @Test
    void deveRetornarNotFoundSeUsuarioNaoExistir() throws Exception {
        Mockito.when(usuarioService.buscarNoBancoDeDadosPeloId(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/user/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveListarTodosUsuarios() throws Exception {
        Mockito.when(usuarioService.buscarTodosOsRegistrosNoBancoDeDados())
                .thenReturn(List.of(respostaUsuario));

        mockMvc.perform(get("/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].email").value("john.doe@example.com"))
                .andExpect(jsonPath("$[0].nome").value("John Updated"));
    }

    @Test
    void deveAtualizarUsuarioComSucesso() throws Exception {
        Mockito.when(usuarioService.atualizarUsuarioComDadosParciais(anyLong(), any(DadosAtualizacaoUsuario.class)))
                .thenReturn(Optional.of(respostaUsuario));

        mockMvc.perform(patch("/user/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(atualizacaoUsuario)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.nome").value("John Updated"));
    }

    @Test
    void deveInativarUsuarioComSucesso() throws Exception {
        mockMvc.perform(patch("/user/inativar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(atualizacaoUsuario)))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveDeletarUsuarioComSucesso() throws Exception {
        mockMvc.perform(delete("/user/deletar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(atualizacaoUsuario)))
                .andExpect(status().isNoContent());
    }
}
