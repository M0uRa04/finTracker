package br.com.fintracker.controller;

import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CrudController<ResponseDTO, CreateDTO, UpdateDTO> {

     ResponseEntity<ResponseDTO> inserirNoBancoDeDados(CreateDTO createDTO);
     ResponseEntity<ResponseDTO> buscarPorId(Long id);
     ResponseEntity<List<ResponseDTO>> listarTodos();
     ResponseEntity<ResponseDTO> atualizar(Long id, UpdateDTO updateDTO);
     ResponseEntity<Void> inativar(Long id);
     ResponseEntity<ResponseDTO> ativar(Long id);
     ResponseEntity<Void> deletar(Long id);
}
