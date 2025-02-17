package br.com.fintracker.repository.relatorio.repositories;

import br.com.fintracker.dto.relatorio.relatorioatingimentocotas.TotalGastoPorCategoriaDTO;
import br.com.fintracker.model.relatorio.RelatorioResumoCotas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface RelatorioResumoCotasRepository extends JpaRepository <RelatorioResumoCotas, Long> {

    @Query(""" 
            SELECT new br.com.fintracker.dto.relatorio.relatorioatingimentocotas.TotalGastoPorCategoriaDTO(t.categoria, SUM(t.valor)) 
            FROM Transacao t 
            WHERE t.usuario.id = :usuarioId 
            AND t.tipoTransacao = 'SAIDA' 
            AND t.dataTransacao BETWEEN :dataInicio AND :dataFim
            GROUP BY t.categoria
            """
    )
    List<TotalGastoPorCategoriaDTO> calculaTotalGastoPorCategoria(@Param("usuarioId") Long usuarioId, @Param("dataInicio") LocalDate dataInicio, @Param("dataFim") LocalDate dataFim);
}
