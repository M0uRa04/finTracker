package br.com.fintracker.service;

import br.com.fintracker.dto.categoria.DadosAtualizacaoCategoria;
import br.com.fintracker.dto.categoria.DadosCadastroCategoria;
import br.com.fintracker.dto.categoria.DadosRespostaCategoria;
import br.com.fintracker.model.Categoria;
import br.com.fintracker.repository.CategoriaRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository repository;

    public DadosRespostaCategoria inserirCategoria (DadosCadastroCategoria dados) {
        if (repository.findByNomeCategoria(dados.nomeCategoria().toUpperCase()) != null) {
            throw new IllegalArgumentException("Categoria já existente com o nome fornecido.");
        }
        Categoria novaCategoria = new Categoria(dados);
        repository.saveAndFlush(novaCategoria);
        return new DadosRespostaCategoria(novaCategoria);
    }

    @Transactional
    private void atualizarCategoria (DadosAtualizacaoCategoria dados) {
        if (dados == null) {
            throw new IllegalArgumentException("Os dados de atualização não podem ser nulos.");
        }

        var categoriaEncontrada = repository.findByNomeCategoria(dados.nome().toUpperCase());

        if (!dados.nome().isBlank()) {
            categoriaEncontrada.setNomeCategoria(dados.nome().toUpperCase());
        }

        if (dados.isAtivo() != null) {
            categoriaEncontrada.setAtivo(dados.isAtivo());
        }

        if (dados.cota() != null && dados.cota().floatValue() > 0) {
            categoriaEncontrada.setCota(dados.cota());
        }

        repository.saveAndFlush(categoriaEncontrada);
    }

    public List<DadosRespostaCategoria> listarCategorias () {
        return repository.findAll().stream()
                .map(c -> new DadosRespostaCategoria(c))
                .collect(Collectors.toList());
    }

    public DadosRespostaCategoria buscarCategoriaPorNome (String nomeCategoria) {
        return new DadosRespostaCategoria(repository.findByNomeCategoria(nomeCategoria.toUpperCase()));
    }
    @Transactional
    public void inativarCategoria (DadosAtualizacaoCategoria dados) {
        var categoriaEncontrada = repository.findByNomeCategoria(dados.nome().toUpperCase());
        if (categoriaEncontrada == null) {
            throw new EntityNotFoundException("Categoria não encontrada com o nome fornecido.");
        }
        categoriaEncontrada.setAtivo(false);
        repository.saveAndFlush(categoriaEncontrada);

    }
    @Transactional

    public void deletarCategoria (DadosAtualizacaoCategoria dados) {
        var categoriaEncontrada = repository.findByNomeCategoria(dados.nome().toUpperCase());
        if (categoriaEncontrada == null) {
            throw new EntityNotFoundException("Categoria não encontrada com o nome fornecido.");
        }
        repository.delete(categoriaEncontrada);
    }


}
