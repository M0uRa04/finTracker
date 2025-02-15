package br.com.fintracker.datatest;

import br.com.fintracker.model.categoria.Categoria;
import br.com.fintracker.model.transacao.TipoTransacao;
import br.com.fintracker.model.transacao.Transacao;
import br.com.fintracker.model.usuario.Perfis;
import br.com.fintracker.model.usuario.Usuario;
import br.com.fintracker.repository.CategoriaRepository;
import br.com.fintracker.repository.TransacaoRepository;
import br.com.fintracker.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Override
    public void run(String... args) throws Exception {
        List<Usuario> usuarios = carregarUsuarios();
        usuarioRepository.saveAll(usuarios);

        List<Categoria> categorias = carregarCategorias(usuarios);
        categoriaRepository.saveAll(categorias);

        List<Transacao> transacoes = carregarTransacoes(usuarios, categorias);
        transacaoRepository.saveAll(transacoes);
    }

    private static List<Usuario> carregarUsuarios() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return List.of(
                new Usuario("Robson", "robson@test.com", encoder.encode("senha123"),  Perfis.ADMIN),
                new Usuario("Helen", "helen@test.com", encoder.encode("senha456"),  Perfis.USER),
                new Usuario("Cirleide", "cirleide@test.com", encoder.encode("senha789"),  Perfis.USER)
        );
    }

    private static List<Categoria> carregarCategorias(List<Usuario> usuarios) {
        return List.of(
                new Categoria("Alimentação", BigDecimal.valueOf(1500), true, usuarios.get(0)),
                new Categoria("Transporte", BigDecimal.valueOf(500), true, usuarios.get(0)),
                new Categoria("Educação", BigDecimal.valueOf(1000), true, usuarios.get(1))
        );
    }

    private static List<Transacao> carregarTransacoes(List<Usuario> usuarios, List<Categoria> categorias) {
        return List.of(
                new Transacao(BigDecimal.valueOf(200), LocalDate.now(), categorias.get(0), "Supermercado", TipoTransacao.SAIDA, usuarios.get(0)),
                new Transacao(BigDecimal.valueOf(50), LocalDate.now().minusDays(1), categorias.get(1), "Uber", TipoTransacao.SAIDA, usuarios.get(0)),
                new Transacao(BigDecimal.valueOf(1200), LocalDate.now().minusDays(2), categorias.get(2), "Mensalidade faculdade", TipoTransacao.SAIDA, usuarios.get(1)),
                new Transacao(BigDecimal.valueOf(3000), LocalDate.now(), categorias.get(0), "Salário", TipoTransacao.ENTRADA, usuarios.get(0)),
                new Transacao(BigDecimal.valueOf(1000), LocalDate.now().minusDays(3), categorias.get(2), "Bônus", TipoTransacao.ENTRADA, usuarios.get(1))
        );
    }
}
