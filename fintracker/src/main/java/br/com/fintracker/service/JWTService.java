package br.com.fintracker.service;

import br.com.fintracker.model.usuario.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class JWTService {

    @Value("${jwt.secret}")
    private String secretKey;

    public String generateTokenJWT (Usuario usuario) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            String token = JWT.create()
                    .withIssuer("FinTracker")
                    .withSubject(usuario.getEmail())
                    .withExpiresAt(Date.from(LocalDateTime.now().plusHours(2L).atZone(ZoneId.systemDefault()).toInstant()))
                    .sign(algorithm);
            System.out.println("TOKEN GERADO----->  " + token);
            return token;

        } catch (JWTCreationException e ) {
            throw new RuntimeException( "Erro ao configurar o token com os parâmetros fornecidos: " + e.getMessage());
        }
    }

    public String verifyTokenJWT (String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            return JWT.require(algorithm)
                    .withIssuer("FinTracker")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException ex) {
            throw new RuntimeException("Erro ao validar o token, por favor informar um token válido." + ex.getMessage());
        }
    }
}
