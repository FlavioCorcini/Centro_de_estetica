package com.cefet.centro_de_estetica.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cefet.centro_de_estetica.entity.Servico;
import com.cefet.centro_de_estetica.repository.ServicoRepository;

@Service
public class ServicoService {

    @Autowired
    private ServicoRepository repository;

    public List<Servico> listarTodos() {
        return repository.findAll();
    }

    public Servico salvar(Servico servico) {
        return repository.save(servico);
    }

    public List<Servico> buscarPorArea(Long idArea) {
        return repository.findByAreaIdArea(idArea);
    }
    
    public void deletar(Long id) {
        repository.deleteById(id);
    }
}