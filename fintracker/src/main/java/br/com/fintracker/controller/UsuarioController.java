package br.com.fintracker.controller;

import br.com.fintracker.dto.usuario.DadosAtualizacaoUsuario;
import br.com.fintracker.dto.usuario.DadosUsuario;
import br.com.fintracker.service.UsuarioService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/user")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @PostMapping
    public ResponseEntity criarConta (@RequestBody @Valid DadosUsuario dadosUsuario) {
        if (this.service.buscarPeloEmail(dadosUsuario.email()) != null) return ResponseEntity.badRequest().build();
        String encryptedPassword = new BCryptPasswordEncoder().encode(dadosUsuario.senha());
        var uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(dadosUsuario)
                .toUri();
        return ResponseEntity.created(uri).body(service.inserirNoBancoDeDados(dadosUsuario, encryptedPassword));
    }

    @GetMapping("/{id}")
    public ResponseEntity buscarContaPeloId (@PathVariable Long id) {
        if (service.buscarNoBancoDeDadosPeloId(id) != null) {
            return ResponseEntity.ok(service.buscarNoBancoDeDadosPeloId(id));
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @GetMapping
    public ResponseEntity listarTodasAsContas() {
        return ResponseEntity.ok(service.buscarTodosOsRegistrosNoBancoDeDados());
    }

    @PatchMapping("/{id}")
    @Transactional
    public ResponseEntity atualizarConta (@PathVariable Long id, @RequestBody @Valid DadosAtualizacaoUsuario dadosAtualizacaoUsuario) {
        //alterar este método para autenticar o usuario via email para atualizar
        return ResponseEntity.ok(service.atualizarUsuarioComDadosParciais(id, dadosAtualizacaoUsuario));
    }

    @DeleteMapping("deletar/{id}")
    public ResponseEntity deletarConta (@PathVariable long id) {
        service.removerDoBancoDeDados(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("inativar/{id}")
    public ResponseEntity inativarConta (@PathVariable long id) {
        service.inativarDoBancoDeDados(id);
        return ResponseEntity.noContent().build();
    }
}
