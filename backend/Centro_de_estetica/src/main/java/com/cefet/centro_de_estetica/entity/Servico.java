package com.cefet.centro_de_estetica.entity;

import java.math.BigDecimal;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Servico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 45)
    private String nome;

    @Column(length = 100)
    private String descricao;
    
    @Column(nullable = false)
    private LocalTime tempoAtendimento;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @ManyToOne 
    @JoinColumn(name = "id_area") 
    private Area area;

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
}
