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
        return ResponseEntity.created(uri).body(service.inserirCategoria(dadosCadastroCategoria));
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<DadosRespostaCategoria> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok().body(service.buscarCategoriaPorId(id));
    }

    @Override
    @GetMapping()
    public ResponseEntity<List<DadosRespostaCategoria>> listarTodos() {
        return ResponseEntity.ok().body(service.listarwwCategorias());
    }

    @Override
    @PatchMapping("/{id}")
    public ResponseEntity<DadosRespostaCategoria> atualizar(@PathVariable Long id, @Valid @RequestBody DadosAtualizacaoCategoria u) {
        return ResponseEntity.ok().body(service.atualizarCategoria(id, u));
    }

    @Override
    @PatchMapping("/inativar")
    public ResponseEntity<Void> inativar(@RequestBody @Valid DadosAtualizacaoCategoria dadosAtualizacaoCategoria) {
        service.inativarCategoria(dadosAtualizacaoCategoria);
        return ResponseEntity.noContent().build();
    }

    @Override
    @DeleteMapping
    public ResponseEntity<Void> deletar(@RequestBody @Valid DadosAtualizacaoCategoria dadosAtualizacaoCategoria) {
        service.deletarCategoria(dadosAtualizacaoCategoria);
        return ResponseEntity.noContent().build();
    }
}
