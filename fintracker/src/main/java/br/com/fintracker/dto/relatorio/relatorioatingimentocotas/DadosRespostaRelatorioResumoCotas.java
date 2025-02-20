package br.com.fintracker.dto.relatorio.relatorioatingimentocotas;

import br.com.fintracker.dto.categoria.DadosRespostaCategoria;
import br.com.fintracker.dto.usuario.DadosRespostaUsuario;
import br.com.fintracker.model.categoria.StatusAtingimentoCota;
import br.com.fintracker.model.relatorio.RelatorioResumoCotas;
import br.com.fintracker.model.relatorio.TipoRelatorio;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DadosRespostaRelatorioResumoCotas(
    @NotNull
    TipoRelatorio tipoRelatorio,
    @NotNull
    LocalDateTime dataCriacao,
    @NotNull
    RangeDatasRelatorioDTO rangeDatas,
    @NotNull
    DadosRespostaUsuario dadosRespostaUsuario,
    @NotNull
    DadosRespostaCategoria dadosRespostaCategoria,
    @NotNull
    BigDecimal totalGasto,
    @NotNull
    Float porcentagemAtingimento,
    @NotNull
    StatusAtingimentoCota statusAtingimentoCota
) {
    public DadosRespostaRelatorioResumoCotas (RelatorioResumoCotas relatorioResumoCotas) {
        this(
            relatorioResumoCotas.getTipoRelatorio(),
            relatorioResumoCotas.getDataCriacao(),
            new RangeDatasRelatorioDTO(relatorioResumoCotas.getDataInicio(), relatorioResumoCotas.getDataFim()),
            new DadosRespostaUsuario(relatorioResumoCotas.getUsuario()),
            new DadosRespostaCategoria(relatorioResumoCotas.getCategoria()),
            relatorioResumoCotas.getTotalGasto(),
            relatorioResumoCotas.getPorcentagemAtingimento(),
            relatorioResumoCotas.getStatusAtingimentoCota()
        );
    }
    
}
