package br.com.fintracker.dto.categoria;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public record DadosAtualizacaoCategoria(
        String nome,
        BigDecimal cota,
        Boolean isAtivo
) {
}
