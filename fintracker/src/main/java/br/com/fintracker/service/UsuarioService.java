package br.com.fintracker.service;

import br.com.fintracker.dto.usuario.DadosAtualizacaoUsuario;
import br.com.fintracker.dto.usuario.DadosRespostaUsuario;
import br.com.fintracker.dto.usuario.UsuarioDTO;
import br.com.fintracker.model.usuario.Usuario;
import br.com.fintracker.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;

    public UsuarioService (UsuarioRepository repository) {
        this.repository = repository;
    }

    private Usuario atualizarAtributos(Usuario usuario, DadosAtualizacaoUsuario dtoAtualizacao) {
        if (dtoAtualizacao.nome() != null) {
            usuario.setNome(dtoAtualizacao.nome());
        }
        if (dtoAtualizacao.email() != null) {
            usuario.setEmail(dtoAtualizacao.email());
        }
        if (dtoAtualizacao.senha() != null) {
            usuario.setSenha(dtoAtualizacao.senha());
        }
        return usuario;
    }



    public DadosRespostaUsuario inserirNoBancoDeDados(UsuarioDTO dto) {
        var usuario = repository.saveAndFlush(new Usuario(dto));
        return new DadosRespostaUsuario(usuario);
    }


    public Optional<DadosRespostaUsuario> buscarNoBancoDeDadosPeloId(Long id) {
        Usuario usuario = repository.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado para o id fornecido."));
        return Optional.of(new DadosRespostaUsuario(usuario));
    }


    public List<DadosRespostaUsuario> buscarTodosOsRegistrosNoBancoDeDados() {
        List<DadosRespostaUsuario> usuarios = repository.findAllByisAtivoTrue().stream()
                .map(DadosRespostaUsuario::new)
                .collect(Collectors.toList());

        return usuarios;
    }

    @Deprecated
    public Optional<DadosRespostaUsuario> atualizarNoBancoDeDados(Long aLong, UsuarioDTO dto) {
        return Optional.empty();
    }

    public Optional<DadosRespostaUsuario> atualizarUsuarioComDadosParciais(Long id, DadosAtualizacaoUsuario dtoAtualizacao) {
        var usuario = repository.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado para o id fornecido."));
        repository.save(atualizarAtributos(usuario, dtoAtualizacao));
        return Optional.of(new DadosRespostaUsuario(usuario));
    }


    public void removerDoBancoDeDados(Long id) {
        repository.deleteById(id);
    }


    public Optional<DadosRespostaUsuario> inativarDoBancoDeDados(Long id) {
        var usuario = repository.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado para o id fornecido."));
        usuario.setAtivo(false);
        repository.save(usuario);
        return Optional.of(new DadosRespostaUsuario(usuario));
    }

}
