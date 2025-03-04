package br.com.fintracker.service;

import br.com.fintracker.dto.categoria.DadosAtualizacaoCategoria;
import br.com.fintracker.dto.categoria.DadosCadastroCategoria;
import br.com.fintracker.dto.categoria.DadosRespostaCategoria;
import br.com.fintracker.infra.security.UserContext;
import br.com.fintracker.model.categoria.Categoria;
import br.com.fintracker.model.categoria.StatusAtingimentoCota;
import br.com.fintracker.repository.CategoriaRepository;
import br.com.fintracker.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoriaService implements CrudService <DadosRespostaCategoria, DadosCadastroCategoria, DadosAtualizacaoCategoria>{

    @Autowired
    private CategoriaRepository repository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public DadosRespostaCategoria inserirNoBancoDeDados(DadosCadastroCategoria dadosCadastro) {
        var categoriaProcurada = repository.findByNomeCategoriaAndUsuarioId(dadosCadastro.nomeCategoria().toUpperCase(), UserContext.getUserId());
        if (categoriaProcurada.isPresent())  {
            throw new IllegalArgumentException("Categoria já existente com o nome fornecido.");
        }
        Categoria novaCategoria = new Categoria(dadosCadastro);
        novaCategoria.setUsuario(usuarioRepository.findById(UserContext.getUserId()).orElseThrow(() -> new EntityNotFoundException("Usuario não encontrado")));
        repository.saveAndFlush(novaCategoria);
        return new DadosRespostaCategoria(novaCategoria);
    }


    private Categoria atualizarAtributos(Optional<Categoria> categoria, DadosAtualizacaoCategoria dadosAtualizacao) {
        if (categoria.isPresent()) {
            var categoriaAtualizada = categoria.get();
            if (!dadosAtualizacao.nomeCategoria().isBlank()) {
                categoriaAtualizada.setNomeCategoria(dadosAtualizacao.nomeCategoria());
            }
            if (dadosAtualizacao.cota() != null && dadosAtualizacao.cota().floatValue() > 0) {
                categoriaAtualizada.setCota(dadosAtualizacao.cota());
            }
            if (dadosAtualizacao.isAtivo() != null) {
                categoriaAtualizada.setAtivo(dadosAtualizacao.isAtivo());
            }
            repository.saveAndFlush(categoriaAtualizada);
            return categoriaAtualizada;
        } else {
            throw new IllegalArgumentException("Os dados de atualização não podem ser nulos.");
        }
    }

    @Override
    @Transactional
    public Optional<DadosRespostaCategoria> atualizar(Long idCategoria, DadosAtualizacaoCategoria dadosAtualizacao) {
        var categoriaEncontrada = repository.findByIdAndUsuarioId(idCategoria, UserContext.getUserId());
        var categoriaAtualizada = atualizarAtributos(categoriaEncontrada, dadosAtualizacao);
        //repository.saveAndFlush(categoriaEncontrada); Salvamento redundante
        return Optional.of(new DadosRespostaCategoria(categoriaAtualizada));
    }



    @Override
    public List<DadosRespostaCategoria> listarTodos() {
        return repository.findAllByUsuarioIdAndIsAtivoTrue(UserContext.getUserId()).orElseThrow(EntityNotFoundException::new)
                .stream()
                .map(c -> new DadosRespostaCategoria(c))
                .collect(Collectors.toList());
    }


    @Override
    public Optional<DadosRespostaCategoria> buscarPorId(Long idCategoria) {
        return Optional.of(new DadosRespostaCategoria(repository.findByIdAndUsuarioIdAndIsAtivoTrue(idCategoria, UserContext.getUserId()).orElseThrow(EntityNotFoundException::new)));
    }
    
    public DadosRespostaCategoria buscarCategoriaPorNome (String nomeCategoria) {
        var categoriaEncontrada = repository.findByNomeCategoria(nomeCategoria);
        if(categoriaEncontrada.isPresent()) {
            return new DadosRespostaCategoria(categoriaEncontrada.get().getUsuario().getId(),"Categoria já existente", BigDecimal.valueOf(00000.00));
        }
        return new DadosRespostaCategoria(repository.findByNomeCategoria(nomeCategoria.toUpperCase()).orElseThrow(EntityNotFoundException::new));
    }



    @Override
    @Transactional
    public void inativar(Long idCategoria) {
        var categoriaEncontrada = repository.findByIdAndUsuarioIdAndIsAtivoTrue(idCategoria, UserContext.getUserId());
        if (categoriaEncontrada.isEmpty()) {
            throw new EntityNotFoundException("Categoria não encontrada com o id fornecido.");
        }
        categoriaEncontrada.get().setAtivo(false);
        repository.saveAndFlush(categoriaEncontrada.get());
    }

    @Override
    @Transactional
    public Optional<DadosRespostaCategoria> ativar(Long idCategoria) {
        var categoriaEncontrada = repository.findByIdAndUsuarioIdAndIsAtivoFalse(idCategoria, UserContext.getUserId());
        if (categoriaEncontrada.isEmpty()) {
            throw new EntityNotFoundException("Categoria não encontrada com o id fornecido.");
        }
        categoriaEncontrada.get().setAtivo(true);
        repository.saveAndFlush(categoriaEncontrada.get());
        return Optional.of(new DadosRespostaCategoria(categoriaEncontrada.get()));
    }

    @Override
    @Transactional
    public void deletar(Long idCategoria) {
        var categoriaEncontrada = repository.findByIdAndUsuarioIdAndIsAtivoTrue(idCategoria, UserContext.getUserId());
        if (categoriaEncontrada.isEmpty()) {
            throw new EntityNotFoundException("Categoria não encontrada com o id fornecido.");
        }
        repository.delete(categoriaEncontrada.get());
    }

}
