package com.cefet.centro_de_estetica.dto;

import java.time.LocalTime;

import com.cefet.centro_de_estetica.entity.HorarioUsuario;

public class HorarioUsuarioResponseDTO {

    private Long id;
    private Integer diaSemana;
    private LocalTime horario;
    private Long idFuncionario;
    private String nomeFuncionario;

    public HorarioUsuarioResponseDTO() {
    }

    public HorarioUsuarioResponseDTO(HorarioUsuario horarioUsuario) {
        this.id = horarioUsuario.getId();
        this.diaSemana = horarioUsuario.getDiaSemana();
        this.horario = horarioUsuario.getHorario();
        this.idFuncionario = horarioUsuario.getFuncionario().getId();
        this.nomeFuncionario = horarioUsuario.getFuncionario().getNome();
    }


    public Long getId() { 
        return id; 
    }

    public void setId(Long id) { 
        this.id = id; 
    }

    public Integer getDiaSemana() { 
        return diaSemana; 
    }

    public void setDiaSemana(Integer diaSemana) { 
        this.diaSemana = diaSemana; 
    }

    public LocalTime getHorario() { 
        return horario; 
    }

    public void setHorario(LocalTime horario) { 
        this.horario = horario; 
    }

    public Long getIdFuncionario() { 
        return idFuncionario; 
    }

    public void setIdFuncionario(Long idFuncionario) { 
        this.idFuncionario = idFuncionario; 
    }

    public String getNomeFuncionario() { 
        return nomeFuncionario; 
    }
    
    public void setNomeFuncionario(String nomeFuncionario) {
        this.nomeFuncionario = nomeFuncionario; 
    }
}