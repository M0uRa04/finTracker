package br.com.fintracker.dto.relatorio.relatorioatingimentocotas;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DadosRespostaRelatorioAtingimentoCotas(
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
    public DadosRespostaRelatorioAtingimentoCotas (RelatorioResumoCotas relatorioResumoCotas) {
        this(
            relatorioResumoCotas.tipoRelatorio(),
            relatorioResumoCotas.dataCriacao(),
            new RangeDatasRelatorioDTO(relatorioResumoCotas.dataInicio(), relatorioResumoCotas.dataFim()),
            new DadosRespostaUsuario(relatorioResumoCotas.usuario()),
            new DadosRespostaCategoria(relatorioResumoCotas.categoria()),
            relatorioResumoCotas.totalGasto(),
            relatorioResumoCotas.porcentagemAtingimento(),
            relatorioResumoCotas.statusAtingimentoCota()
        );
    }
    
}
