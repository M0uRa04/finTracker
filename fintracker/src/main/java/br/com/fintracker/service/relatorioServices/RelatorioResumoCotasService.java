package br.com.fintracker.service.relatorioServices;

import br.com.fintracker.repository.relatorioRepositories.RelatorioResumoCotasRepository;
import br.com.fintracker.infra.security.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class RelatorioResumoCotasService {

    @Autowired
    private RelatorioResumoCotasRepository relatorioResumoCotasRepository;

    @Autowired
    private UserContext userContext;

    public void obterTotalGastoPorCategoria () {
        relatorioResumoCotasRepository.calculaTotalGastoPorCategoria(userContext.getUserId(), LocalDate.now(), LocalDate.now().plusDays(15));
    }
}
