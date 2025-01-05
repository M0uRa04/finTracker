package br.com.fintracker.datatest;

import br.com.fintracker.model.categoria.Categoria;
import br.com.fintracker.model.usuario.Perfis;
import br.com.fintracker.model.usuario.Usuario;
import br.com.fintracker.repository.CategoriaRepository;
import br.com.fintracker.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;
    @Override
    public void run(String... args) throws Exception {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        Usuario usuario1 = new Usuario();
        usuario1.setNome("Robson");
        usuario1.setEmail("robson@test.com");
        usuario1.setSenha(encoder.encode("senha123"));
        usuario1.setAtivo(true);
        usuario1.setPerfil(Perfis.ADMIN);

        Usuario usuario2 = new Usuario();
        usuario2.setNome("Helen");
        usuario2.setEmail("helen@test.com");
        usuario2.setSenha(encoder.encode("senha456"));
        usuario2.setAtivo(true);
        usuario2.setPerfil(Perfis.USER);


        Usuario usuario3 = new Usuario();
        usuario3.setNome("Cirleide");
        usuario3.setEmail("cirleide@test.com");
        usuario3.setSenha(encoder.encode("senha789"));
        usuario3.setAtivo(true);
        usuario3.setPerfil(Perfis.USER);

        usuarioRepository.saveAll(List.of(usuario1, usuario2, usuario3));

        Categoria categoria1 = new Categoria();
        categoria1.setNomeCategoria("Alimentação");
        categoria1.setCota(BigDecimal.valueOf(1500));
        categoria1.setAtivo(true);

        Categoria categoria2 = new Categoria();
        categoria2.setNomeCategoria("Transporte");
        categoria2.setCota(BigDecimal.valueOf(500));
        categoria2.setAtivo(true);

        Categoria categoria3 = new Categoria();
        categoria3.setNomeCategoria("Educação");
        categoria3.setCota(BigDecimal.valueOf(1000));
        categoria3.setAtivo(true);

        categoriaRepository.saveAll(List.of(categoria1, categoria2, categoria3));
    }
}
