package br.com.fintracker.controller;

import br.com.fintracker.dto.usuario.DadosAtualizacaoUsuario;
import br.com.fintracker.dto.usuario.DadosRespostaUsuario;
import br.com.fintracker.dto.usuario.DadosCadastroUsuario;
import br.com.fintracker.service.UsuarioService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UsuarioController implements CrudController <DadosRespostaUsuario, DadosCadastroUsuario, DadosAtualizacaoUsuario>{

    @Autowired
    private UsuarioService service;

    @Override
    @PostMapping
    public ResponseEntity<DadosRespostaUsuario> inserirNoBancoDeDados(@RequestBody @Valid DadosCadastroUsuario dadosCadastroUsuario) {
        if (this.service.buscarPeloEmail(dadosCadastroUsuario.email()) != null) return ResponseEntity.badRequest().build();
        var uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(dadosCadastroUsuario)
                .toUri();
        return ResponseEntity.created(uri).body(service.inserirNoBancoDeDados(dadosCadastroUsuario));
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<DadosRespostaUsuario> buscarPorId(@PathVariable Long id) {
        if (service.buscarPorId(id).isPresent()) {
            return ResponseEntity.ok(service.buscarPorId(id).orElseThrow(() -> new EntityNotFoundException()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    @GetMapping
    public ResponseEntity<List<DadosRespostaUsuario>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @Override
    @PatchMapping("/{id}")
    @Transactional
    public ResponseEntity<DadosRespostaUsuario> atualizar(@PathVariable Long id, @RequestBody @Valid DadosAtualizacaoUsuario dadosAtualizacaoUsuario) {
        //alterar este método para autenticar o usuario via email para atualizar infos
        return ResponseEntity.ok(service.atualizar(id, dadosAtualizacaoUsuario).orElseThrow(() -> new EntityNotFoundException()));
    }

    @Override
    @PatchMapping("inativar/{id}")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        service.inativar(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @DeleteMapping("deletar/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
