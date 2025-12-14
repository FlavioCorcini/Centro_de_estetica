package com.cefet.centro_de_estetica.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cefet.centro_de_estetica.dto.AreaRequestDTO;
import com.cefet.centro_de_estetica.dto.AreaResponseDTO;
import com.cefet.centro_de_estetica.service.AreaService;


@RestController
@RequestMapping("/areas") 
public class AreaController {

    private final AreaService service;

    public AreaController(AreaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<AreaResponseDTO> criar(@RequestBody @Validated AreaRequestDTO dados) {
        AreaResponseDTO novaArea = service.salvar(dados);
                return ResponseEntity.status(HttpStatus.CREATED).body(novaArea);
    }

    @GetMapping
    public ResponseEntity<List<AreaResponseDTO>> listar() {
        List<AreaResponseDTO> lista = service.listarTodos();
        return ResponseEntity.ok(lista); // Status 200
    }

    @GetMapping("/{id}")
    public ResponseEntity<AreaResponseDTO> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok) 
                .orElse(ResponseEntity.notFound().build()); 
    }

    @PutMapping("/{id}")
    public ResponseEntity<AreaResponseDTO> atualizar(@PathVariable Long id, @RequestBody @Validated AreaRequestDTO dados) {
        try {
            AreaResponseDTO areaAtualizada = service.atualizar(id, dados);
            return ResponseEntity.ok(areaAtualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        try {
            service.deletar(id);
            return ResponseEntity.noContent().build(); 
        } catch (RuntimeException e) {
            // Retorna 400 Bad Request se houver erro de negócio (ex: serviços vinculados)
            // ou 404 se não achar o ID.
            return ResponseEntity.badRequest().build(); 
        }
    }
}