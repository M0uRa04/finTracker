package br.com.fintracker.service.relatorioServices;

import br.com.fintracker.repository.relatorioRepositories.RelatorioResumoCotasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RelatorioResumoCotasService {

    @Autowired
    private RelatorioResumoCotasRepository relatorioResumoCotasRepository;
    public void obterTotalGastoPorCategoria () {
        relatorioResumoCotasRepository.calculaTotalGastoPorCategoria()
    }
}
