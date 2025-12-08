package com.cefet.centro_de_estetica.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cefet.centro_de_estetica.entity.Area;
import com.cefet.centro_de_estetica.repository.AreaRepository;

@Service 
public class AreaService {

    @Autowired
    private AreaRepository repository;

    public List<Area> listarTodas() {
        return repository.findAll();
    }

    public Area salvar(Area area) {
        return repository.save(area);
    }

    public Area buscarPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }
}
