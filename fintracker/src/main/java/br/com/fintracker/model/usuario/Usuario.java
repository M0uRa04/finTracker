package br.com.fintracker.model.usuario;

import br.com.fintracker.dto.usuario.DadosCadastroUsuario;
import br.com.fintracker.model.categoria.Categoria;
import br.com.fintracker.model.relatorio.Relatorio;
import br.com.fintracker.model.relatorio.RelatorioResumoCotas;
import br.com.fintracker.model.transacao.Transacao;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "usuario")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String email;
    private String senha;
    private Boolean isAtivo;
    @Enumerated(EnumType.STRING)
    private Perfis perfil;

    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
    private List<Transacao>transacoes;

    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
    private List<Categoria>categorias;

    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
    private List<RelatorioResumoCotas> relatorioResumoCotas;
    public Usuario () {};

    public Usuario (String nome, String email, String senha, Perfis perfil) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.isAtivo = true;
        this.perfil = perfil;
    };

    public Usuario(DadosCadastroUsuario dto) {
        this.nome = dto.nome();
        this.email = dto.email();
        this.senha = dto.senha();
        this.isAtivo = true;
        this.perfil = Perfis.USER;
    }

    public Usuario(DadosCadastroUsuario dto, String senhaEncriptada) {
        this.nome = dto.nome();
        this.email = dto.email();
        this.senha = senhaEncriptada;
        this.isAtivo = true;
        this.perfil = Perfis.USER;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Boolean getAtivo() {
        return isAtivo;
    }

    public void setAtivo(Boolean ativo) {
        isAtivo = ativo;
    }

    public Perfis getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfis perfil) {
        this.perfil = perfil;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.perfil == Perfis.ADMIN) return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"),
                new SimpleGrantedAuthority("ROLE_USER"));
        else return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(getId(), usuario.getId()) && Objects.equals(getNome(), usuario.getNome()) && Objects.equals(getEmail(), usuario.getEmail()) && Objects.equals(getSenha(), usuario.getSenha()) && Objects.equals(isAtivo, usuario.isAtivo) && getPerfil() == usuario.getPerfil() && Objects.equals(transacoes, usuario.transacoes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getNome(), getEmail(), getSenha(), isAtivo, getPerfil(), transacoes);
    }


    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nomeCategoria='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", senha='" + senha + '\'' +
                ", isAtivo=" + isAtivo +
                ", perfil=" + perfil +
                ", transacoes=" + transacoes +
                '}';
    }


}
