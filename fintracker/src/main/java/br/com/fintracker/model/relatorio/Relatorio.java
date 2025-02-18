package br.com.fintracker.model.relatorio;

import br.com.fintracker.model.usuario.Usuario;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@MappedSuperclass
public abstract class Relatorio {

    @Enumerated(value = EnumType.STRING)
    private TipoRelatorio tipoRelatorio;
    @Nullable
    private LocalDate dataInicio;
    @Nullable
    private LocalDate dataFim;
    private LocalDateTime dataCriacao;

    @ManyToOne
    private Usuario usuario;

    public Relatorio () {
        this.dataCriacao = LocalDateTime.now();
    };

    public Relatorio(TipoRelatorio tipoRelatorio, LocalDate dataInicio, LocalDate dataFim, LocalDateTime dataCriacao, Usuario usuario) {
        this.tipoRelatorio = tipoRelatorio;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.dataCriacao = LocalDateTime.now();
        this.usuario = usuario;
    }

    public TipoRelatorio getTipoRelatorio() {
        return tipoRelatorio;
    }

    public void setTipoRelatorio(TipoRelatorio tipoRelatorio) {
        this.tipoRelatorio = tipoRelatorio;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Relatorio relatorio = (Relatorio) o;
        return getTipoRelatorio() == relatorio.getTipoRelatorio() && Objects.equals(getDataInicio(), relatorio.getDataInicio()) && Objects.equals(getDataFim(), relatorio.getDataFim()) && Objects.equals(getUsuario(), relatorio.getUsuario());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTipoRelatorio(), getDataInicio(), getDataFim(), getUsuario());
    }

}
