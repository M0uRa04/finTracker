package br.com.fintracker.service.relatorioServices;

import br.com.fintracker.repository.relatorioRepositories.RelatorioResumoCotasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RelatorioResumoCotasService {

    @Autowired
    private RelatorioResumoCotasRepository relatorioResumoCotasRepository;
    public Map<String, BigDecimal> obterTotalGastoPorCategoria () {
        List<Object[]> resultados = relatorioResumoCotasRepository.calculaTotalGasto();

        Map<String, BigDecimal> totalPorCategoria = new HashMap<>();
        for (Object[] resultado : resultados) {
            String nomeCategoria = (String) resultado[0];
            BigDecimal totalGasto = (BigDecimal) resultado[1];
            totalPorCategoria.put(nomeCategoria, totalGasto);
        }

        return totalPorCategoria;
    }
}
