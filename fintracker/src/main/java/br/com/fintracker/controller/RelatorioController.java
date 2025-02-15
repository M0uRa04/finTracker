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

    //preciso colocar neste método: O @RequestBody e criar o DTO que é enviado para o método com os paremetros de dataInicio e dataFim 
    @GetMapping("/resumo-cotas")
    public void getRelatorioResumoCotas (){
        relatorioResumoCotasService.obterTotalGastoPorCategoria();
    }
}
