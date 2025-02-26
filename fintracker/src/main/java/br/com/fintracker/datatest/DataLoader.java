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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private TransacaoRepository transacaoRepository;

    private static final Random random = new Random();

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
                new Usuario("Robson", "robson@test.com", encoder.encode("senha123"), Perfis.ADMIN),
                new Usuario("Helen", "helen@test.com", encoder.encode("senha456"), Perfis.USER),
                new Usuario("Cirleide", "cirleide@test.com", encoder.encode("senha789"), Perfis.USER)
        );
    }

    private static List<Categoria> carregarCategorias(List<Usuario> usuarios) {
        return List.of(
                new Categoria("Alimentação", BigDecimal.valueOf(1500), true, usuarios.get(0)),
                new Categoria("Transporte", BigDecimal.valueOf(500), true, usuarios.get(0)),
                new Categoria("Educação", BigDecimal.valueOf(1000), true, usuarios.get(1)),
                new Categoria("Lazer", BigDecimal.valueOf(800), true, usuarios.get(1)),
                new Categoria("Saúde", BigDecimal.valueOf(700), true, usuarios.get(2)),
                new Categoria("Moradia", BigDecimal.valueOf(2000), true, usuarios.get(2)),
                new Categoria("Investimentos", BigDecimal.valueOf(3000), true, usuarios.get(0)),
                new Categoria("Salário", BigDecimal.valueOf(5000), true, usuarios.get(0))
        );
    }

    private static List<Transacao> carregarTransacoes(List<Usuario> usuarios, List<Categoria> categorias) {
        List<Transacao> transacoes = new ArrayList<>();
        LocalDate dataAtual = LocalDate.now();

        for (Usuario usuario : usuarios) {
            for (int i = 0; i < 200; i++) { // Criando 200 transações para cada usuário
                Categoria categoria = categorias.get(random.nextInt(categorias.size()));
                LocalDate dataTransacao = dataAtual.minusDays(random.nextInt(180)); // Últimos 6 meses
                BigDecimal valor = BigDecimal.valueOf(random.nextInt(500) + 10); // Valores entre 10 e 500
                TipoTransacao tipo = (random.nextBoolean()) ? TipoTransacao.SAIDA : TipoTransacao.ENTRADA;
                String descricao = tipo == TipoTransacao.SAIDA ? "Despesa " + categoria.getNomeCategoria() : "Receita " + categoria.getNomeCategoria();

                transacoes.add(new Transacao(valor, dataTransacao, categoria, descricao, tipo, usuario));
            }
        }
        return transacoes;
    }
}
