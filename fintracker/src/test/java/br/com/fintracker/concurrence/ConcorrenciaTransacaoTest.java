package br.com.fintracker.concurrence;

import br.com.fintracker.dto.transacao.DadosCadastroTransacao;
import br.com.fintracker.model.transacao.TipoTransacao;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ConcorrenciaTransacaoTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // Para converter o objeto em JSON

    private String token; // Armazena o token do usuário autenticado

//    @BeforeEach
//    void setup() throws Exception {
//        // Simula login e armazena o token do usuário logado
//        token = autenticarUsuario();
//    }

    @Test
    void testarConcorrenciaCriacaoTransacoes() throws InterruptedException, ExecutionException {
        int numUsuarios = 3; // Defina quantos usuários diferentes testar
        ExecutorService executorService = Executors.newFixedThreadPool(numUsuarios);
        List<Future<Boolean>> resultados = new ArrayList<>();

        // Mapa para armazenar tokens dos usuários
        Map<String, String> tokens = new ConcurrentHashMap<>();
        String[] emails = {"robson@test.com", "helen@test.com", "cirleide@test.com"};
        String senha = "senha123";

        // Autenticar múltiplos usuários e armazenar tokens
        for (String email : emails) {
            resultados.add(executorService.submit(() -> {
                String token = autenticarUsuario(email, senha);
                if (token != null) {
                    tokens.put(email, token);
                    return true;
                }
                return false;
            }));
        }

        // Esperar todas as autenticações serem concluídas
        for (Future<Boolean> resultado : resultados) {
            assertTrue(resultado.get(), "Falha ao autenticar usuário");
        }

        resultados.clear(); // Limpa para reutilizar na criação de transações

        // Criar transações simultaneamente para cada usuário
        for (String email : emails) {
            String token = tokens.get(email);
            resultados.add(executorService.submit(() -> criarTransacao(token, email)));
        }

        // Verificar se todas as transações foram criadas corretamente
        for (Future<Boolean> resultado : resultados) {
            assertTrue(resultado.get(), "Falha ao criar transação corretamente");
        }

        executorService.shutdown();
    }

    // Método para autenticar e retornar o token
    private String autenticarUsuario(String email, String senha) throws Exception {
        String jsonBody = String.format("{\"email\":\"%s\", \"senha\":\"%s\"}", email, senha);

        MvcResult resultado = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = resultado.getResponse().getContentAsString();
        JsonNode jsonNode = new ObjectMapper().readTree(responseBody);
        return jsonNode.get("token").asText();
    }

    // Método para criar transação autenticada
    private boolean criarTransacao(String token, String email) throws Exception {
        
        var categoriaId;

        switch (email) {

            case "robson@test.com":
                categoriaId = 1;
                break;

            case "helen@test.com":
                categoriaId = 3;
                break;
                
            case "cirleide@test.com":
                categoriaId = 5;
                break;    

            default:
                throw new AssertionError();
        }

        String jsonBody = String.format("""
        {
            "tipoTransacao": "SAIDA",
            "categoriaId": %d,
            "dataTransacao": "2025-01-10",
            "valor": 2000.00,
            "descricao": "testando a concorrência"
        }
    """,categoriaId);

        MvcResult resultado = mockMvc.perform(post("/transacoes")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isCreated()) // Verifica se a transação foi criada corretamente
                .andReturn();

        // Opcional: Verifica se o JSON de resposta contém alguma informação relevante
        String responseBody = resultado.getResponse().getContentAsString();
        return responseBody.contains(email); // Ajuste conforme necessário
    }


}
