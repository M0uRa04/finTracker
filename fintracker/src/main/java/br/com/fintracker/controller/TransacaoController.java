package br.com.fintracker.controller;

import br.com.fintracker.dto.transacao.DadosAtualizacaoTransacao;
import br.com.fintracker.dto.transacao.DadosCadastroTransacao;
import br.com.fintracker.dto.transacao.DadosRespostaTransacao;
import br.com.fintracker.service.TransacaoService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/transacao")
public class TransacaoController implements CrudController <DadosRespostaTransacao, DadosCadastroTransacao, DadosAtualizacaoTransacao> {

    @Autowired
    private TransacaoService service;
    @Override
    @PostMapping
    public ResponseEntity<DadosRespostaTransacao> inserirNoBancoDeDados(@RequestBody @Valid DadosCadastroTransacao dadosCadastroTransacao) {
        var uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(dadosCadastroTransacao)
                .toUri();
        return ResponseEntity.created(uri).body(service.inserirNoBancoDeDados(dadosCadastroTransacao));
    }

    @Override
    @GetMapping("/{idTransacao}")
    public ResponseEntity<DadosRespostaTransacao> buscarPorId(@PathVariable Long idTransacao) {
        Long idUsuario = service.getAuthenticateUserId();
        var transacaoBuscada = service.buscarTransacaoPorIdEUsuario(idTransacao, idUsuario);
        return ResponseEntity.ok(transacaoBuscada.orElseThrow());
    }

    @Override
    @GetMapping
    public ResponseEntity<List<DadosRespostaTransacao>> listarTodos() {
        Long idUsuario = service.getAuthenticateUserId();
        return ResponseEntity.ok(service.listarTodos(idUsuario));
    }

    @Override
    @PatchMapping("/{idTransacao}")
    public ResponseEntity<DadosRespostaTransacao> atualizar(@PathVariable Long idTransacao, @RequestBody DadosAtualizacaoTransacao dadosAtualizacaoTransacao) {
        var idUsuario = service.getAuthenticateUserId();
        return ResponseEntity.ok(service.atualizar(idTransacao, idUsuario, dadosAtualizacaoTransacao).orElseThrow(() -> new EntityNotFoundException("Transação não encontrada para o ID fornecido.")));
    }

    @Override
    @Deprecated
    public ResponseEntity<Void> inativar(Long id) {
        return null;
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
