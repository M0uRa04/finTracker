package br.com.fintracker.service;

import br.com.fintracker.model.usuario.Usuario;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class JWTServiceTest {

    @InjectMocks
    private JWTService jwtService;

    @Mock
    private Usuario usuario;

    @Value("${jwt.secret}")
    private String secretKey;

    @DynamicPropertySource
    static void jwtProperties(DynamicPropertyRegistry registry) {
        registry.add("jwt.secret", () -> "test-secret-key");
    }
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Configuração da chave secreta para testes
        ReflectionTestUtils.setField(jwtService, "secretKey", "test-secret-key");
    }

    @Test
    void testGenerateTokenJWT_Success() {
        // Configuração
        when(usuario.getEmail()).thenReturn("test@example.com");

        // Ação
        String token = jwtService.generateTokenJWT(usuario);

        // Validação
        assertNotNull(token);
        assertFalse(token.isEmpty());
        System.out.println("Token gerado: " + token);
    }

    @Test
    void testGenerateTokenJWT_Exception() {
        // Configuração
        when(usuario.getEmail()).thenReturn(null);

        // Ação e Validação
        assertThrows(RuntimeException.class, () -> jwtService.generateTokenJWT(usuario));
    }

    @Test
    void testVerifyTokenJWT_Success() {
        // Configuração
        when(usuario.getEmail()).thenReturn("test@example.com");
        String token = jwtService.generateTokenJWT(usuario);

        // Ação
        String subject = jwtService.verifyTokenJWT(token);

        // Validação
        assertEquals("test@example.com", subject);
    }

    @Test
    void testVerifyTokenJWT_InvalidToken() {
        // Configuração
        String invalidToken = "invalid.token.string";

        // Ação e Validação
        RuntimeException exception = assertThrows(RuntimeException.class, () -> jwtService.verifyTokenJWT(invalidToken));
        assertTrue(exception.getMessage().contains("Erro ao validar o token"));
    }

    @Test
    void testVerifyTokenJWT_ExpiredToken() {
        // Configuração de um token expirado manualmente
        when(usuario.getEmail()).thenReturn("test@example.com");
        String token = jwtService.generateTokenJWT(usuario);

        // Simulando um delay para expirar o token
        try {
            Thread.sleep(1000); // Ajuste se necessário para o tempo de expiração
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Ação e Validação
        RuntimeException exception = assertThrows(RuntimeException.class, () -> jwtService.verifyTokenJWT(token));
        assertTrue(exception.getMessage().contains("Erro ao validar o token"));
    }
}
