package com.cefet.centro_de_estetica.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cefet.centro_de_estetica.entity.HorarioUsuario;
import com.cefet.centro_de_estetica.service.HorarioUsuarioService;

@RestController
@RequestMapping("/horarios") 
public class HorarioUsuarioController {

    @Autowired
    private HorarioUsuarioService service;

    @GetMapping
    public List<HorarioUsuario> listar() {
        return service.listarTodos();
    }

    @PostMapping
    public HorarioUsuario criar(@RequestBody HorarioUsuario horario) {
        return service.salvar(horario);
    }
    
    @GetMapping("/funcionario/{idFuncionario}")
    public List<HorarioUsuario> listarPorFuncionario(@PathVariable Long idFuncionario) {
        return service.listarPorFuncionario(idFuncionario);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        service.deletar(id);
    }
}