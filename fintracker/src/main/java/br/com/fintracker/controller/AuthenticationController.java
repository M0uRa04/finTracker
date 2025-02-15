package br.com.fintracker.controller;

import br.com.fintracker.dto.jwt.DadosJWT;
import br.com.fintracker.dto.usuario.DadosAtualizacaoUsuario;
import br.com.fintracker.model.usuario.Usuario;
import br.com.fintracker.repository.UsuarioRepository;
import br.com.fintracker.service.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private JWTService jwtService;

    @PostMapping("/login")
    public ResponseEntity login (@RequestBody DadosAtualizacaoUsuario dados) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(dados.email(), dados.senha());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        var tokenJWT = jwtService.generateTokenJWT((Usuario) auth.getPrincipal());
        return ResponseEntity.ok().body(new DadosJWT(tokenJWT));
    }

}
