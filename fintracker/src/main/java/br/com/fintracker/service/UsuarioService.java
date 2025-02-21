package br.com.fintracker.service;

import br.com.fintracker.dto.usuario.DadosAtualizacaoUsuario;
import br.com.fintracker.dto.usuario.DadosRespostaUsuario;
import br.com.fintracker.dto.usuario.DadosCadastroUsuario;
import br.com.fintracker.model.usuario.Usuario;
import br.com.fintracker.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioService implements CrudService <DadosRespostaUsuario, DadosCadastroUsuario, DadosAtualizacaoUsuario>{

    @Autowired
    private UsuarioRepository repository;

    private Usuario atualizarAtributos(Usuario usuario, DadosAtualizacaoUsuario dtoAtualizacao) {
        if (dtoAtualizacao.nome() != null) {
            usuario.setNome(dtoAtualizacao.nome());
        }
        if (dtoAtualizacao.email() != null) {
            usuario.setEmail(dtoAtualizacao.email());
        }
        if (dtoAtualizacao.senha() != null) {
            String encryptedPassword = new BCryptPasswordEncoder().encode(dtoAtualizacao.senha());
            usuario.setSenha(encryptedPassword);
        }
        return usuario;
    }

    @Override
    public DadosRespostaUsuario inserirNoBancoDeDados(DadosCadastroUsuario dadosCadastro) {
        String encryptedPassword = new BCryptPasswordEncoder().encode(dadosCadastro.senha());
        var usuario = repository.saveAndFlush(new Usuario(dadosCadastro, encryptedPassword));
        return new DadosRespostaUsuario(usuario);
    }


    @Override
    public Optional<DadosRespostaUsuario> buscarPorId(Long id) {
        Usuario usuario = repository.findByIdAndIsAtivoTrue(id);
        return Optional.of(new DadosRespostaUsuario(usuario));
    }


    @Override
    public List<DadosRespostaUsuario> listarTodos() {
        List<DadosRespostaUsuario> usuarios = repository.findAllByisAtivoTrue().stream()
                .map(DadosRespostaUsuario::new)
                .collect(Collectors.toList());

        return usuarios;
    }


    @Override
    public Optional<DadosRespostaUsuario> atualizar(Long id, DadosAtualizacaoUsuario dadosAtualizacao) {
        var usuario = repository.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado para o id fornecido."));
        repository.save(atualizarAtributos(usuario, dadosAtualizacao));
        return Optional.of(new DadosRespostaUsuario(usuario));
    }

    public Optional<DadosRespostaUsuario> atualizarUsuario (DadosAtualizacaoUsuario dadosAtualizacao) {
        var usuario = repository.findById(UserContext.getUserId()).orElseThrow(() -> new RuntimeException("Usuário não encontrado para o id fornecido."));
        repository.save(atualizarAtributos(usuario, dadosAtualizacao));
        return Optional.of(new DadosRespostaUsuario(usuario));
    }

    @Override
    public void inativar(Long id) {
        var usuario = repository.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado para o id fornecido."));
        usuario.setAtivo(false);
        repository.save(usuario);
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        repository.deleteById(id);
    }

    public UserDetails buscarPeloEmail (String email) {
        return repository.findByEmail(email);
    }

}
