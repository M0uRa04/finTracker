package br.com.fintracker.service.relatorioServices;

import br.com.fintracker.dto.relatorio.relatorioatingimentocotas.RangeDatasRelatorioDTO;
import br.com.fintracker.dto.relatorio.relatorioatingimentocotas.TotalGastoPorCategoriaDTO;
import br.com.fintracker.infra.security.UserContext;
import br.com.fintracker.model.categoria.StatusAtingimentoCota;
import br.com.fintracker.model.relatorio.RelatorioResumoCotas;
import br.com.fintracker.repository.relatorioRepositories.RelatorioResumoCotasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RelatorioResumoCotasService {

    @Autowired
    private RelatorioResumoCotasRepository relatorioResumoCotasRepository;

    private List<TotalGastoPorCategoriaDTO> obterTotalGastoPorCategoria (RangeDatasRelatorioDTO dto) {
        return  relatorioResumoCotasRepository.calculaTotalGastoPorCategoria(UserContext.getUserId(), dto.dataInicio(), dto.dataFim());
    }

    public List <RelatorioResumoCotas> criarRelatorioResumoCotas (RangeDatasRelatorioDTO rangeDatasRelatorioDTO) {
        var totaisPorCategoria = obterTotalGastoPorCategoria(rangeDatasRelatorioDTO);

        return totaisPorCategoria.stream()
                .map(totalGastoPorCategoriaDTO -> new RelatorioResumoCotas(
                        totalGastoPorCategoriaDTO.categoria(), totalGastoPorCategoriaDTO.totalGasto(), 0.10F, StatusAtingimentoCota.DEFAULT))
                .collect(Collectors.toList());
    }
}
