package com.cefet.centro_de_estetica.dto;

import java.math.BigDecimal;

import com.cefet.centro_de_estetica.entity.Servico;

public class ServicoResponseDTO {

    private Long id;
    private String nome;
    private String descricao;
    private Integer tempoAtendimento;
    private BigDecimal valor;
    private Long idArea;     
    private String nomeArea; 

    public ServicoResponseDTO() {
    }

    public ServicoResponseDTO(Servico servico) {
        this.id = servico.getId();
        this.nome = servico.getNome();
        this.descricao = servico.getDescricao();
        this.tempoAtendimento = servico.getTempoAtendimento();
        this.valor = servico.getValor();
        this.idArea = servico.getArea().getIdArea();
        this.nomeArea = servico.getArea().getNome();
    }

    public ServicoResponseDTO(Long id, String nome, String descricao, BigDecimal valor, Integer tempoAtendimento, String nomeArea) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.valor = valor;
        this.tempoAtendimento = tempoAtendimento;
        this.nomeArea = nomeArea;
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

    public Integer getTempoAtendimento() {
        return tempoAtendimento; 
    }

    public void setTempoAtendimento(Integer tempoAtendimento) { 
        this.tempoAtendimento = tempoAtendimento; 
    }

    public BigDecimal getValor() { 
        return valor; 
    }

    public void setValor(BigDecimal valor) { 
        this.valor = valor;
    }

    public Long getIdArea() { 
        return idArea; 
    }
    public void setIdArea(Long idArea) { 
        this.idArea = idArea; 
    }

    public String getNomeArea() { 
        return nomeArea; 
    }

    public void setNomeArea(String nomeArea) { 
        this.nomeArea = nomeArea; 
    }
}