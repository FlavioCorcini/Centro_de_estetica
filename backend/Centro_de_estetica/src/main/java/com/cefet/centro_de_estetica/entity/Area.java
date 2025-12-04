package com.cefet.centro_de_estetica.entity;

import jakarta.persistence.*;
import java.util.List;


@Entity
public class Area {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idArea;

    @Column(nullable = false, length = 20)
    private String nome; 

    @Column(nullable = false)
    private String descricao; 
    
    @OneToMany(mappedBy = "area") 
    private List<Servico> servicos;

    //
    public Area() {
    }

    //
    
    public Long getIdArea() {
        return idArea;
    }

    public void setIdArea(Long idArea) {
		this.idArea = idArea;
	}

	public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Servico> getServicos() {
        return servicos;
    }

    public void setServicos(List<Servico> servicos) {
        this.servicos = servicos;
    }

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
    
    
}
