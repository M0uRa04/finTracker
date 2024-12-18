package br.com.fintracker.controller;

import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CrudController<ResponseDTO, CreateDTO, UpdateDTO> {

     ResponseEntity<ResponseDTO> inserirNoBancoDeDados(CreateDTO createDTO);
     ResponseEntity<ResponseDTO> buscarPorId(Long id);
     ResponseEntity<List<ResponseDTO>> listarTodos();
     ResponseEntity<ResponseDTO> atualizar(Long id, UpdateDTO updateDTO);
     ResponseEntity<Void> inativar(UpdateDTO updateDTO);
     ResponseEntity<Void> deletar(UpdateDTO updateDTO);
}
