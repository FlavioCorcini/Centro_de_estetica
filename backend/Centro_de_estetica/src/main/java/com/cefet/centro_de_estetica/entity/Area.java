package com.cefet.centro_de_estetica.entity;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;


@Entity
@Table(name = "tb_area")
public class Area {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nome",nullable = false, length = 45)
    private String nome; 

    @Column(name = "descricao",nullable = false, length= 100)
    private String descricao; 
    
    @OneToMany(mappedBy = "area") 
    @JsonIgnore
    private List<Servico> servicos;

    //Construtor
    public Area() {
    }

    //GETs & SETs
    public Long getIdArea() {
        return id;
    }

    public void setIdArea(Long idArea) {
		this.id = idArea;
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
