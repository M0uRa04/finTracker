package br.com.fintracker.controller;

import br.com.fintracker.dto.transacao.DadosAtualizacaoTransacao;
import br.com.fintracker.dto.transacao.DadosCadastroTransacao;
import br.com.fintracker.dto.transacao.DadosRespostaTransacao;
import br.com.fintracker.service.TransacaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    public ResponseEntity<DadosRespostaTransacao> buscarPorId(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<List<DadosRespostaTransacao>> listarTodos() {
        return null;
    }

    @Override
    public ResponseEntity<DadosRespostaTransacao> atualizar(Long id, DadosAtualizacaoTransacao dadosAtualizacaoTransacao) {
        return null;
    }

    @Override
    public ResponseEntity<Void> inativar(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<Void> deletar(Long id) {
        return null;
    }
}
