package br.com.fintracker.controller;

import br.com.fintracker.dto.usuario.DadosAtualizacaoUsuario;
import br.com.fintracker.dto.usuario.UsuarioDTO;
import br.com.fintracker.service.UsuarioService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/user")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @PostMapping
    public ResponseEntity criarConta (@RequestBody @Valid UsuarioDTO usuarioDTO) {
        service.inserirNoBancoDeDados(usuarioDTO);
        var uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(usuarioDTO)
                .toUri();
        return ResponseEntity.created(uri).body(usuarioDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity buscarContaPeloId (@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarNoBancoDeDadosPeloId(id));
    }

    @GetMapping
    public ResponseEntity listarTodasAsContas() {
        return ResponseEntity.ok(service.buscarTodosOsRegistrosNoBancoDeDados());
    }

    @PatchMapping("/{id}")
    @Transactional
    public ResponseEntity atualizarConta (@PathVariable Long id, @RequestBody @Valid DadosAtualizacaoUsuario dadosAtualizacaoUsuario) {

        return ResponseEntity.ok(service.atualizarUsuarioComDadosParciais(id, dadosAtualizacaoUsuario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletarConta (@PathVariable long id) {
        service.removerDoBancoDeDados(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/inativar")
    public ResponseEntity inativarConta (@PathVariable long id) {
        service.inativarDoBancoDeDados(id);
        return ResponseEntity.noContent().build();
    }
}
