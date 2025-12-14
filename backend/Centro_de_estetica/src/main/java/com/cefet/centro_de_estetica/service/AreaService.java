package com.cefet.centro_de_estetica.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.cefet.centro_de_estetica.dto.AreaRequestDTO;
import com.cefet.centro_de_estetica.dto.AreaResponseDTO;
import com.cefet.centro_de_estetica.entity.Area;
import com.cefet.centro_de_estetica.repository.AreaRepository;

@Service
public class AreaService {

    private final AreaRepository repository;

    public AreaService(AreaRepository repository) {
        this.repository = repository;
    }

    
    public AreaResponseDTO salvar(AreaRequestDTO dto) {
         if (repository.existsByNome(dto.nome())) throw new RuntimeException("Área já existe");

        Area area = new Area();
        area.setNome(dto.nome());
        area.setDescricao(dto.descricao());

        Area areaSalva = repository.save(area);

        return new AreaResponseDTO(areaSalva);
    }

    public List<AreaResponseDTO> listarTodos() {
        return repository.findAll().stream().map(AreaResponseDTO::new)
                .collect(Collectors.toList());
    }

    public Optional<AreaResponseDTO> buscarPorId(Long id) {
        return repository.findById(id).map(AreaResponseDTO::new);
    }

    
    public AreaResponseDTO atualizar(Long id, AreaRequestDTO dto) {
        Area areaEncontrada = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Área não encontrada com id: " + id));

        
        areaEncontrada.setNome(dto.nome());
        areaEncontrada.setDescricao(dto.descricao());
        
        Area areaAtualizada = repository.save(areaEncontrada);

        return new AreaResponseDTO(areaAtualizada);
    }

    
    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Área não encontrada para deletar.");
        }
        
        
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Não é possível deletar esta área pois ela possui serviços vinculados.");
        }
    }
}