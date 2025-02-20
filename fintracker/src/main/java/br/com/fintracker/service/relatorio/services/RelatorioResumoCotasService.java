package br.com.fintracker.service.relatorio.services;

import br.com.fintracker.dto.relatorio.relatorioatingimentocotas.DadosRespostaRelatorioResumoCotas;
import br.com.fintracker.dto.relatorio.relatorioatingimentocotas.RangeDatasRelatorioDTO;
import br.com.fintracker.dto.relatorio.relatorioatingimentocotas.TotalGastoPorCategoriaDTO;
import br.com.fintracker.infra.security.UserContext;
import br.com.fintracker.model.categoria.StatusAtingimentoCota;
import br.com.fintracker.model.relatorio.RelatorioResumoCotas;
import br.com.fintracker.model.usuario.Usuario;
import br.com.fintracker.repository.UsuarioRepository;
import br.com.fintracker.repository.relatorio.repositories.RelatorioResumoCotasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RelatorioResumoCotasService {

    @Autowired
    private RelatorioResumoCotasRepository relatorioResumoCotasRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<DadosRespostaRelatorioResumoCotas> criarDadosRespostaRelatorioResumoCotas(RangeDatasRelatorioDTO rangeDatasRelatorioDTO) {
        criarRelatorioResumoCotas(rangeDatasRelatorioDTO);
        
        var relatorioResumoCotasList = relatorioResumoCotasRepository.findAll();
        
        return relatorioResumoCotasList.stream()
                .map(relatorioResumoCota -> {
                    return new DadosRespostaRelatorioResumoCotas(relatorioResumoCota);
                })
                .collect(Collectors.toList());
    }

    private void criarRelatorioResumoCotas(RangeDatasRelatorioDTO rangeDatasRelatorioDTO) {
        // posibilidade de grande refatoração
        var totaisPorCategoria = obterTotalGastoPorCategoria(rangeDatasRelatorioDTO);

        var listaDeRelatoriosGerados =  totaisPorCategoria.stream()
                .map(totalGastoPorCategoriaDTO -> {
                    Optional<Usuario> usuarioOpt = usuarioRepository.findById(UserContext.getUserId());
                    if (usuarioOpt.isEmpty()) {
                        throw new RuntimeException("Usuário não encontrado");
                    }
                    var usuario = usuarioOpt.get();
                    var porcentagemAtingimento = calculaPorcentagemAtingimento(totalGastoPorCategoriaDTO.categoria().getCota(), totalGastoPorCategoriaDTO.totalGasto());
                    return new RelatorioResumoCotas(
                            totalGastoPorCategoriaDTO,
                            rangeDatasRelatorioDTO,
                            usuario,
                            porcentagemAtingimento,
                            StatusAtingimentoCota.setStatus(porcentagemAtingimento)
                    );
                })
                .collect(Collectors.toList());
        relatorioResumoCotasRepository.saveAll(listaDeRelatoriosGerados);       
    }


    private List<TotalGastoPorCategoriaDTO> obterTotalGastoPorCategoria(RangeDatasRelatorioDTO dto) {
        return relatorioResumoCotasRepository.calculaTotalGastoPorCategoria(UserContext.getUserId(), dto.dataInicio(), dto.dataFim());
    }

    private Float calculaPorcentagemAtingimento(BigDecimal cota, BigDecimal totalGasto) {
        if (cota.compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalArgumentException("Cota não pode ser zero");
        }
        return totalGasto.divide(cota,2, RoundingMode.HALF_UP).floatValue();
    }
}