package br.com.fintracker.service;

import java.util.List;
import java.util.Optional;

public interface CrudService <ResponseDTO, CreateDTO, UpdateDTO>{

    /**
     * Insere um novo registro no banco de dados.
     *
     * @param dadosCadastro os dados de cadastro do objeto.
     * @return os dados do objeto após a criação.
     */
    ResponseDTO inserirNoBancoDeDados(CreateDTO dadosCadastro);

    /**
     * Busca um registro no banco de dados pelo seu identificador.
     *
     * @param id o identificador único do registro.
     * @return os dados do registro, caso exista.
     */
    Optional<ResponseDTO> buscarPorId(Long id);

    /**
     * Lista todos os registros no banco de dados.
     *
     * @return uma lista contendo todos os registros.
     */
    List<ResponseDTO> listarTodos();

    /**
     * Atualiza um registro no banco de dados com novos dados.
     *
     * @param id           o identificador único do registro.
     * @param dadosAtualizacao os novos dados para atualização.
     * @return os dados do registro após a atualização.
     */
    Optional<ResponseDTO> atualizar(Long id, UpdateDTO dadosAtualizacao);

    /**
     * Inativa um registro no banco de dados.
     *
     * @param id o identificador único do registro a ser inativado.
     */
    void inativar(Long id);

    
    /**
     * ativa um registro do banco de dados.
     *
     * @param id o identificador único do registro a ser removido.
     */
    Optional<ResponseDTO> ativar(Long id);

    /**
     * Remove um registro do banco de dados.
     *
     * @param id o identificador único do registro a ser removido.
     */
    void deletar(Long id);
}
