package br.com.fintracker.controller;

import br.com.fintracker.service.relatorioServices.RelatorioResumoCotasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/relatorio")
public class RelatorioController {

    @Autowired
    private RelatorioResumoCotasService relatorioResumoCotasService;
    @GetMapping("/resumo-cotas")
    public void getRelatorioResumoCotas (){
        relatorioResumoCotasService.obterTotalGastoPorCategoria();
    }
}
