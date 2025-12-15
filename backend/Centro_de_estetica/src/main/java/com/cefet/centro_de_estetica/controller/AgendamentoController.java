package com.cefet.centro_de_estetica.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cefet.centro_de_estetica.dto.AgendamentoRequestDTO;
import com.cefet.centro_de_estetica.dto.AgendamentoResponseDTO;
import com.cefet.centro_de_estetica.dto.AtualizacaoStatusDTO;
import com.cefet.centro_de_estetica.service.AgendamentoService;


@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {

    private final AgendamentoService service;

    // Injeção de dependência via construtor
    public AgendamentoController(AgendamentoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<AgendamentoResponseDTO> criar(@RequestBody @Validated AgendamentoRequestDTO dto) {
        AgendamentoResponseDTO novoAgendamento = service.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoAgendamento);
    }

    @GetMapping
    public ResponseEntity<List<AgendamentoResponseDTO>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }
    
 // GET /agendamentos/cliente/1
    @GetMapping("/cliente/{id}")
    public ResponseEntity<List<AgendamentoResponseDTO>> buscarPorCliente(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorCliente(id));
    }

    // GET /agendamentos/funcionario/1
    @GetMapping("/funcionario/{id}")
    public ResponseEntity<List<AgendamentoResponseDTO>> buscarPorFuncionario(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorFuncionario(id));
    }

    // GET /agendamentos/data?data=2025-12-25
    @GetMapping("/data")
    public ResponseEntity<List<AgendamentoResponseDTO>> buscarPorData(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        
        return ResponseEntity.ok(service.buscarPorData(data));
    }

    // Atualizar dados do agendamento (Mudar horário, mudar serviço...)
    @PutMapping("/{id}")
    public ResponseEntity<AgendamentoResponseDTO> atualizar(
            @PathVariable Long id, 
            @RequestBody @Validated AgendamentoRequestDTO dto) {
        
        AgendamentoResponseDTO atualizado = service.atualizar(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> atualizarStatus(
            @PathVariable Long id, 
            @RequestBody @Validated AtualizacaoStatusDTO dto) {
        
        service.atualizarStatus(id, dto.status());
        return ResponseEntity.noContent().build();
    }

    // Deletar do banco de dados (apaga o histórico)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}