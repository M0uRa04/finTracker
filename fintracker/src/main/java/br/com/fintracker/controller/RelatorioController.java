package br.com.fintracker.controller;

import br.com.fintracker.dto.relatorio.relatorioatingimentocotas.RangeDatasRelatorioDTO;
import br.com.fintracker.service.relatorio.services.RelatorioResumoCotasService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/relatorio")
public class RelatorioController {

    @Autowired
    private RelatorioResumoCotasService relatorioResumoCotasService;

    @GetMapping("/resumo-cotas")
    public ResponseEntity getRelatorioResumoCotas (@Valid @RequestBody RangeDatasRelatorioDTO dto){
        return ResponseEntity.ok(relatorioResumoCotasService.criarDadosRespostaRelatorioResumoCotas(dto));
    }
}
