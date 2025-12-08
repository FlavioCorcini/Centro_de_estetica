package com.cefet.centro_de_estetica.entity;

import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "horario_usuario")
public class HorarioUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer diaSemana; 
    
    @Column(nullable = false)
    private LocalTime horario; 

    @ManyToOne
    @JoinColumn(name = "id_usuario_funcionario", nullable = false)
    private Usuario funcionario;

    public HorarioUsuario() {}


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getDiaSemana() { return diaSemana; }
    public void setDiaSemana(Integer diaSemana) { this.diaSemana = diaSemana; }

    public LocalTime getHorario() { return horario; }
    public void setHorario(LocalTime horario) { this.horario = horario; }

    public Usuario getFuncionario() { return funcionario; }
    public void setFuncionario(Usuario funcionario) { this.funcionario = funcionario; }
}