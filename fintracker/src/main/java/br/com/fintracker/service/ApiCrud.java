package br.com.fintracker.service;

import java.util.List;
import java.util.Optional;

public interface ApiCrud <T, ID>{
    T inserirNoBancoDeDados(T t);
    Optional<T> buscarNoBancoDeDados(ID id);
    List<T> buscarTodosOsRegistrosNoBancoDeDados();
    Optional<T> atualizarNoBancoDeDados(ID id, T t);
    Optional<T> removerDoBancoDeDados(ID id);
    Optional<T> inativarDoBancoDeDados(ID id);
}
