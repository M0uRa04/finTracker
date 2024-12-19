package br.com.fintracker.controller;

import br.com.fintracker.dto.categoria.DadosAtualizacaoCategoria;
import br.com.fintracker.dto.categoria.DadosCadastroCategoria;
import br.com.fintracker.dto.categoria.DadosRespostaCategoria;
import br.com.fintracker.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/categoria")
public class CategoriaController implements CrudController <DadosRespostaCategoria, DadosCadastroCategoria, DadosAtualizacaoCategoria>{

    @Autowired
    private CategoriaService service;
    @Override
    @PostMapping
    public ResponseEntity<DadosRespostaCategoria> inserirNoBancoDeDados(@RequestBody @Valid DadosCadastroCategoria dadosCadastroCategoria) {
        var uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(dadosCadastroCategoria)
                .toUri();
        return ResponseEntity.created(uri).body(service.inserirNoBancoDeDados(dadosCadastroCategoria));
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<DadosRespostaCategoria> buscarPorId(@PathVariable Long id) {
        if (service.buscarPorId(id).isPresent()) {
            return ResponseEntity.ok().body(service.buscarPorId(id).get());
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    @GetMapping()
    public ResponseEntity<List<DadosRespostaCategoria>> listarTodos() {
        return ResponseEntity.ok().body(service.listarTodos());
    }

    @Override
    @PatchMapping("/{id}")
    public ResponseEntity<DadosRespostaCategoria> atualizar(@PathVariable Long id, @Valid @RequestBody DadosAtualizacaoCategoria dadosAtualizacaoCategoria) {
        return ResponseEntity.ok().body(service.atualizar(id, dadosAtualizacaoCategoria).get());
    }

    @Override
    @PatchMapping("/inativar/{id}")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        service.inativar(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
