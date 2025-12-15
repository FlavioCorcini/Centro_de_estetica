package com.cefet.centro_de_estetica.entity;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_servico")
public class Servico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome",nullable = false, length = 45)
    private String nome;

    @Column(name = "descricao",length = 100)
    private String descricao;
    
    @Column(name = "tempo_atendimento",nullable = false)
    private LocalTime tempoAtendimento;
    
    @Column(name = "valor",nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @ManyToOne 
    @JoinColumn(name = "id_area") 
    @JsonIgnore
    private Area area;
    
    @ManyToMany(mappedBy = "servicos")
    @JsonIgnore
    private List<Usuario> funcionarios;

    public Servico() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public LocalTime getTempoAtendimento() { return tempoAtendimento; }
    public void setTempoAtendimento(LocalTime tempoAtendimento) { this.tempoAtendimento = tempoAtendimento; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public Area getArea() { return area; }
    public void setArea(Area area) { this.area = area; }

	public List<Usuario> getFuncionarios() {
		return funcionarios;
	}

	public void setFuncionarios(List<Usuario> funcionarios) {
		this.funcionarios = funcionarios;
	}
}
