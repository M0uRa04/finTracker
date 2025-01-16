package br.com.fintracker.service;

import br.com.fintracker.dto.categoria.DadosAtualizacaoCategoria;
import br.com.fintracker.dto.categoria.DadosCadastroCategoria;
import br.com.fintracker.dto.categoria.DadosRespostaCategoria;
import br.com.fintracker.infra.security.UserContext;
import br.com.fintracker.model.categoria.Categoria;
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
        //Oportunidade para inserir em UserContext um método que Forneça o Objeto Usuário do usuário logado.
        novaCategoria.setUsuario(usuarioRepository.findById(UserContext.getUserId()).orElseThrow(() -> new EntityNotFoundException("Usuario não encontrado")));
        repository.saveAndFlush(novaCategoria);
        return new DadosRespostaCategoria(novaCategoria);
    }


    //Oportunidade de refatorar. Dividir o método em 2
    @Override
    @Transactional
    public Optional<DadosRespostaCategoria> atualizar(Long idCategoria, DadosAtualizacaoCategoria dadosAtualizacao) {
        if (dadosAtualizacao == null) {
            throw new IllegalArgumentException("Os dadosAtualizacao de atualização não podem ser nulos.");
        }

        var categoriaEncontrada = repository.findByIdAndUsuarioId(idCategoria, UserContext.getUserId()).orElseThrow(EntityNotFoundException::new);

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
        return Optional.of(new DadosRespostaCategoria(categoriaEncontrada));
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
            return new DadosRespostaCategoria("Categoria já existente", BigDecimal.valueOf(00000.00));
        }

        return new DadosRespostaCategoria(repository.findByNomeCategoria(nomeCategoria.toUpperCase()));
    }



    @Override
    @Transactional
    public void inativar(Long idCategoria) {
        var categoriaEncontrada = repository.findByIdAndUsuarioIdAndIsAtivoTrue(idCategoria, UserContext.getUserId());
        if (categoriaEncontrada.isEmpty()) {
            throw new EntityNotFoundException("Categoria não encontrada com o nomeCategoria fornecido.");
        }
        categoriaEncontrada.get().setAtivo(false);
        repository.saveAndFlush(categoriaEncontrada.get());
    }


    @Override
    @Transactional
    public void deletar(Long idCategoria) {
        var categoriaEncontrada = repository.findByIdAndUsuarioIdAndIsAtivoTrue(idCategoria, UserContext.getUserId());
        if (categoriaEncontrada.isEmpty()) {
            throw new EntityNotFoundException("Categoria não encontrada com o nomeCategoria fornecido.");
        }
        repository.delete(categoriaEncontrada.get());
    }

}
