package com.cefet.centro_de_estetica.entity;

import java.time.LocalTime;

import com.cefet.centro_de_estetica.enums.DiaDaSemana;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_horario_usuario")
public class HorarioUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dia_semana",nullable = false, length = 3)
    @Enumerated(EnumType.STRING)
    private DiaDaSemana diaSemana; 
    
    @Column(name = "horario_inicio",nullable = false)
    private LocalTime horarioInicio;
    
    @Column(name = "horario_fim",nullable = false)
    private LocalTime horarioFim;

    @ManyToOne
    @JoinColumn(name = "id_usuario_funcionario", nullable = false)
    @JsonIgnore
    private Usuario funcionario;

    public HorarioUsuario() {}


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public DiaDaSemana getDiaSemana() { return diaSemana; }
    public void setDiaSemana(DiaDaSemana diaSemana) { this.diaSemana = diaSemana; }


	public LocalTime getHorarioInicio() {
		return horarioInicio;
	}


	public void setHorarioInicio(LocalTime horarioInicio) {
		this.horarioInicio = horarioInicio;
	}


	public LocalTime getHorarioFim() {
		return horarioFim;
	}


	public void setHorarioFim(LocalTime horarioFim) {
		this.horarioFim = horarioFim;
	}


	public Usuario getFuncionario() {
		return funcionario;
	}


	public void setFuncionario(Usuario funcionario) {
		this.funcionario = funcionario;
	}

    
}