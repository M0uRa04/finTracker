package br.com.fintracker.model.relatorio;

import br.com.fintracker.model.usuario.Usuario;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;

@MappedSuperclass
public abstract class Relatorio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private TipoRelatorio tipoRelatorio;
    private LocalDate dataInicio;
    private LocalDate dataFim;

    @ManyToOne
    private Usuario usuario;

    public Relatorio () {};

    public Relatorio(Long id, TipoRelatorio tipoRelatorio, LocalDate dataInicio, LocalDate dataFim, Usuario usuario) {
        this.id = id;
        this.tipoRelatorio = tipoRelatorio;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.usuario = usuario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        return Objects.equals(getId(), relatorio.getId()) && getTipoRelatorio() == relatorio.getTipoRelatorio() && Objects.equals(getDataInicio(), relatorio.getDataInicio()) && Objects.equals(getDataFim(), relatorio.getDataFim()) && Objects.equals(getUsuario(), relatorio.getUsuario());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTipoRelatorio(), getDataInicio(), getDataFim(), getUsuario());
    }

    private void gerarRelatorio () {};
}
