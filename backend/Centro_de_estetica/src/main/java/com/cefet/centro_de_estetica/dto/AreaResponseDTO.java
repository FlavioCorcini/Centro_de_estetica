package com.cefet.centro_de_estetica.dto;

import java.util.List;

import com.cefet.centro_de_estetica.entity.Area;
import com.cefet.centro_de_estetica.entity.Servico;

public class AreaResponseDTO {

    private Long id;
    private String nome;
    private String descricao;
    private List<Servico> servicos;
    
    public  AreaResponseDTO() {
    }

    public AreaResponseDTO(Area area) {
        this.id = area.getIdArea();
        this.nome = area.getNome();
        this.descricao = area.getDescricao();
        this.servicos = area.getServicos();
    }

    public AreaResponseDTO(Long id, String nome, String descricao, List<Servico> servicos) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.servicos = servicos;
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

	public List<Servico> getServicos() {
		return servicos;
	}

	public void setServicos(List<Servico> servicos) {
		this.servicos = servicos;
	}
    
    
}