package com.cefet.centro_de_estetica.mapper;

import com.cefet.centro_de_estetica.dto.AreaRequestDTO;
import com.cefet.centro_de_estetica.dto.AreaResponseDTO;
import com.cefet.centro_de_estetica.entity.Area;
import org.springframework.stereotype.Component;

@Component
public class AreaMapper {

    public AreaResponseDTO toResponse(Area area) {
        if (area == null) return null;

        return new AreaResponseDTO(
            area.getIdArea(), 
            area.getNome(),
            area.getDescricao(),
            area.getServicos()
        );
    }

    public Area toEntity(AreaRequestDTO dto) {
        if (dto == null) return null;

        Area area = new Area();
        area.setNome(dto.nome());
        area.setDescricao(dto.descricao());
        return area;
    }
}