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

    private List<TotalGastoPorCategoriaDTO> obterTotalGastoPorCategoria () {
        relatorioResumoCotasRepository.calculaTotalGastoPorCategoria(Long usuarioId, LocalDate dataInicio, LocalDate dataFim);
    }

    public List <RelatorioResumoCotas> criarRelatorioResumoCotas (
        List<TotalGastoPorCategoriaDTO> totaisPorCategoria, 
        LocalDate dataInicio, 
        LocalDate dataFim, 
        Usuario usuario, 
        Float porcentagemAtingimento, 
        StatusAtingimentoCota statusAtingimentoCota
    ) {
        return totaisPorCategoria.stream()
        .map(dto -> new RelatorioResumoCotas(
                
        ))    
    
    }
}
