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
        String encryptedPassword = new BCryptPasswordEncoder().encode(dadosCadastroUsuario.senha());
        var uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(dadosCadastroUsuario)
                .toUri();
        return ResponseEntity.created(uri).body(service.inserirNoBancoDeDados(dadosCadastroUsuario, encryptedPassword));
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<DadosRespostaUsuario> buscarPorId(@PathVariable Long id) {
        if (service.buscarNoBancoDeDadosPeloId(id).isPresent()) {
            return ResponseEntity.ok(service.buscarNoBancoDeDadosPeloId(id).orElseThrow(() -> new EntityNotFoundException()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    @GetMapping
    public ResponseEntity<List<DadosRespostaUsuario>> listarTodos() {
        return ResponseEntity.ok(service.buscarTodosOsRegistrosNoBancoDeDados());
    }

    @Override
    @PatchMapping("/{id}")
    @Transactional
    public ResponseEntity<DadosRespostaUsuario> atualizar(@PathVariable Long id, @RequestBody @Valid DadosAtualizacaoUsuario dadosAtualizacaoUsuario) {
        //alterar este método para autenticar o usuario via email para atualizar infos
        return ResponseEntity.ok(service.atualizarUsuarioComDadosParciais(id, dadosAtualizacaoUsuario).orElseThrow(() -> new EntityNotFoundException()));
    }

    @Override
    @PatchMapping("inativar")
    public ResponseEntity<Void> inativar(@RequestBody @Valid DadosAtualizacaoUsuario dadosAtualizacaoUsuario) {
        service.inativarDoBancoDeDados(dadosAtualizacaoUsuario.id());
        return ResponseEntity.noContent().build();
    }

    @Override
    @DeleteMapping("deletar")
    public ResponseEntity<Void> deletar(@RequestBody @Valid DadosAtualizacaoUsuario dadosAtualizacaoUsuario) {
        service.removerDoBancoDeDados(dadosAtualizacaoUsuario.id());
        return ResponseEntity.noContent().build();
    }
}
