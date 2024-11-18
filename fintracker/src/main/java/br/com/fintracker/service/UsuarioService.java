package br.com.fintracker.service;

import br.com.fintracker.dto.usuario.DadosAtualizacaoUsuario;
import br.com.fintracker.dto.usuario.UsuarioDTO;
import br.com.fintracker.model.usuario.Usuario;
import br.com.fintracker.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioService implements ApiCrud <UsuarioDTO, Long> {

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
            usuario.setSenha(dtoAtualizacao.senha());
        }
        return usuario;
    }


    @Override
    public UsuarioDTO inserirNoBancoDeDados(UsuarioDTO dto) {
        var usuario = repository.saveAndFlush(new Usuario(dto));
        return new UsuarioDTO(usuario);
    }

    @Override
    public Optional<UsuarioDTO> buscarNoBancoDeDadosPeloId(Long id) {
        Usuario usuario = repository.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado para o id fornecido."));
        return Optional.of(new UsuarioDTO(usuario));
    }

    @Override
    public List<UsuarioDTO> buscarTodosOsRegistrosNoBancoDeDados() {
        List<UsuarioDTO> usuariosDTO = repository.findAll().stream()
                .map(UsuarioDTO::new)
                .collect(Collectors.toList());

        return usuariosDTO;
    }

    @Deprecated
    @Override
    public Optional<UsuarioDTO> atualizarNoBancoDeDados(Long aLong, UsuarioDTO dto) {
        return Optional.empty();
    }

    public Optional<UsuarioDTO> atualizarUsuarioComDadosParciais(Long id, DadosAtualizacaoUsuario dtoAtualizacao) {
        var usuario = repository.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado para o id fornecido."));
        repository.save(atualizarAtributos(usuario, dtoAtualizacao));
        return Optional.of(new UsuarioDTO(usuario));
    }

    @Override
    public void removerDoBancoDeDados(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Optional<UsuarioDTO> inativarDoBancoDeDados(Long id) {
        var usuario = repository.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado para o id fornecido."));
        usuario.setAtivo(false);
        repository.save(usuario);
        return Optional.of(new UsuarioDTO(usuario));
    }

}
