package br.com.fintracker.service;

import br.com.fintracker.dto.categoria.DadosAtualizacaoCategoria;
import br.com.fintracker.dto.categoria.DadosCadastroCategoria;
import br.com.fintracker.dto.categoria.DadosRespostaCategoria;
import br.com.fintracker.model.categoria.Categoria;
import br.com.fintracker.repository.CategoriaRepository;
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

    @Override
    public DadosRespostaCategoria inserirNoBancoDeDados(DadosCadastroCategoria dadosCadastro) {
        if (repository.findByNomeCategoria(dadosCadastro.nomeCategoria().toUpperCase()) != null) {
            throw new IllegalArgumentException("Categoria já existente com o nomeCategoria fornecido.");
        }
        Categoria novaCategoria = new Categoria(dadosCadastro);
        repository.saveAndFlush(novaCategoria);
        return new DadosRespostaCategoria(novaCategoria);
    }



    @Override
    @Transactional
    public Optional<DadosRespostaCategoria> atualizar(Long id, DadosAtualizacaoCategoria dadosAtualizacao) {
        if (dadosAtualizacao == null) {
            throw new IllegalArgumentException("Os dadosAtualizacao de atualização não podem ser nulos.");
        }

        var categoriaEncontrada = repository.findById(id).orElseThrow(EntityNotFoundException::new);

        if (!dadosAtualizacao.nomeCategoria().isBlank()) {
            categoriaEncontrada.setNomeCategoria(dadosAtualizacao.nomeCategoria().toUpperCase());
        }

        if (dadosAtualizacao.isAtivo() != null) {
            categoriaEncontrada.setAtivo(dadosAtualizacao.isAtivo());
        }

        if (dadosAtualizacao.cota() != null && dadosAtualizacao.cota().floatValue() > 0) {
            categoriaEncontrada.setCota(dadosAtualizacao.cota());
        }

        repository.saveAndFlush(categoriaEncontrada);
        return Optional.of(new DadosRespostaCategoria(repository.findByNomeCategoria(categoriaEncontrada.getNomeCategoria())));
    }



    @Override
    public List<DadosRespostaCategoria> listarTodos() {
        return repository.findByIsAtivoTrue().stream()
                .map(c -> new DadosRespostaCategoria(c))
                .collect(Collectors.toList());
    }


    @Override
    public Optional<DadosRespostaCategoria> buscarPorId(Long id) {
        return Optional.of(new DadosRespostaCategoria(repository.findByIdAndIsAtivoTrue(id).orElseThrow(EntityNotFoundException::new)));
    }

    public DadosRespostaCategoria buscarCategoriaPorNome (String nomeCategoria) {
        var categoriaEncontrada = repository.findByNomeCategoria(nomeCategoria);
        if(categoriaEncontrada != null) {
            return new DadosRespostaCategoria("Categoria já existente", BigDecimal.valueOf(00000.00));
        }

        return new DadosRespostaCategoria(repository.findByNomeCategoria(nomeCategoria.toUpperCase()));
    }



    @Override
    @Transactional
    public void inativar(Long id) {
        var categoriaEncontrada = repository.findById(id);
        if (categoriaEncontrada.isEmpty()) {
            throw new EntityNotFoundException("Categoria não encontrada com o nomeCategoria fornecido.");
        }
        categoriaEncontrada.get().setAtivo(false);
        repository.saveAndFlush(categoriaEncontrada.get());
    }


    @Override
    @Transactional
    public void deletar(Long id) {
        var categoriaEncontrada = repository.findById(id);
        if (categoriaEncontrada.isEmpty()) {
            throw new EntityNotFoundException("Categoria não encontrada com o nomeCategoria fornecido.");
        }
        repository.delete(categoriaEncontrada.get());
    }



}
