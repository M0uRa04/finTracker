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
import java.util.stream.Collectors;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository repository;

    public DadosRespostaCategoria inserirCategoria (DadosCadastroCategoria dados) {
        if (repository.findByNomeCategoria(dados.nomeCategoria().toUpperCase()) != null) {
            throw new IllegalArgumentException("Categoria já existente com o nomeCategoria fornecido.");
        }
        Categoria novaCategoria = new Categoria(dados);
        repository.saveAndFlush(novaCategoria);
        return new DadosRespostaCategoria(novaCategoria);
    }

    @Transactional
    public DadosRespostaCategoria atualizarCategoria (Long id, DadosAtualizacaoCategoria dados) {
        if (dados == null) {
            throw new IllegalArgumentException("Os dados de atualização não podem ser nulos.");
        }

        var categoriaEncontrada = repository.findById(id).orElseThrow(EntityNotFoundException::new);

        if (!dados.nomeCategoria().isBlank()) {
            categoriaEncontrada.setNomeCategoria(dados.nomeCategoria().toUpperCase());
        }

        if (dados.isAtivo() != null) {
            categoriaEncontrada.setAtivo(dados.isAtivo());
        }

        if (dados.cota() != null && dados.cota().floatValue() > 0) {
            categoriaEncontrada.setCota(dados.cota());
        }

        repository.saveAndFlush(categoriaEncontrada);
        return new DadosRespostaCategoria(repository.findByNomeCategoria(categoriaEncontrada.getNomeCategoria()));
    }

    public List<DadosRespostaCategoria> listarCategorias () {
        return repository.findByIsAtivoTrue().stream()
                .map(c -> new DadosRespostaCategoria(c))
                .collect(Collectors.toList());
    }

    public DadosRespostaCategoria buscarCategoriaPorId (Long id) {
        return new DadosRespostaCategoria(repository.findById(id).orElseThrow(EntityNotFoundException::new));
    }

    public DadosRespostaCategoria buscarCategoriaPorNome (String nomeCategoria) {
        var categoriaEncontrada = repository.findByNomeCategoria(nomeCategoria);
        if(categoriaEncontrada != null) {
            return new DadosRespostaCategoria("Categoria já existente", BigDecimal.valueOf(00000.00));
        }

        return new DadosRespostaCategoria(repository.findByNomeCategoria(nomeCategoria.toUpperCase()));
    }

    @Transactional
    public void inativarCategoria (DadosAtualizacaoCategoria dados) {
        var categoriaEncontrada = repository.findByNomeCategoria(dados.nomeCategoria().toUpperCase());
        if (categoriaEncontrada == null) {
            throw new EntityNotFoundException("Categoria não encontrada com o nomeCategoria fornecido.");
        }
        categoriaEncontrada.setAtivo(false);
        repository.saveAndFlush(categoriaEncontrada);

    }
    @Transactional

    public void deletarCategoria (DadosAtualizacaoCategoria dados) {
        var categoriaEncontrada = repository.findByNomeCategoria(dados.nomeCategoria().toUpperCase());
        if (categoriaEncontrada == null) {
            throw new EntityNotFoundException("Categoria não encontrada com o nomeCategoria fornecido.");
        }
        repository.delete(categoriaEncontrada);
    }


}
