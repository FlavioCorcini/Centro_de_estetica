package com.cefet.centro_de_estetica.dto;

import com.cefet.centro_de_estetica.entity.Area;

public class AreaResponseDTO {

    private Long id;
    private String nome;
    private String descricao;

    public AreaResponseDTO() {
    }

    public AreaResponseDTO(Area area) {
        this.id = area.getIdArea();
        this.nome = area.getNome();
        this.descricao = area.getDescricao();
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}