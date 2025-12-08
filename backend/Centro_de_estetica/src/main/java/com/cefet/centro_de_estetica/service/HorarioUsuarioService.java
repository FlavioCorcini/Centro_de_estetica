package com.cefet.centro_de_estetica.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cefet.centro_de_estetica.entity.HorarioUsuario;
import com.cefet.centro_de_estetica.repository.HorarioUsuarioRepository;

@Service
public class HorarioUsuarioService {

    @Autowired
    private HorarioUsuarioRepository repository;

    public List<HorarioUsuario> listarTodos() {
        return repository.findAll();
    }

    public HorarioUsuario salvar(HorarioUsuario horario) {
        return repository.save(horario);
    }

    public List<HorarioUsuario> listarPorFuncionario(Long idFuncionario) {
        return repository.findByFuncionarioId(idFuncionario);
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }
}