package com.cefet.centro_de_estetica.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.cefet.centro_de_estetica.entity.Usuario;
import com.cefet.centro_de_estetica.enums.StatusUsuario;
import com.cefet.centro_de_estetica.enums.TipoUsuario;

public class UsuarioResponseDTO {
    private Long id;
    private String nome;
    private String telefone;
    private String email;
    private StatusUsuario statusUsuario;
    private TipoUsuario tipo;
    
    
    private boolean ativo; 
    
    private List<ServicoResponseDTO> servicos;
    private List<HorarioUsuarioResponseDTO> horarios;
    
    public UsuarioResponseDTO() {
    }
    
    public UsuarioResponseDTO(Usuario usuario) {
        this.id = usuario.getId();
        this.nome = usuario.getNome();
        this.telefone = usuario.getTelefone();
        this.email = usuario.getEmail();
        this.statusUsuario = usuario.getStatusUsuario();
        this.tipo = usuario.getTipo();
        this.ativo = (usuario.getStatusUsuario() == StatusUsuario.ATIVO);
        
        if (usuario.getServicos() != null) {
            this.servicos = usuario.getServicos().stream()
                .map(ServicoResponseDTO::new) 
                .collect(Collectors.toList());
        } else {
            this.servicos = new ArrayList<>();
        }

        if (usuario.getHorarios() != null) {
            this.horarios = usuario.getHorarios().stream()
                .map(HorarioUsuarioResponseDTO::new) 
                .collect(Collectors.toList());
        } else {
            this.horarios = new ArrayList<>();
        }
    }
    
    public UsuarioResponseDTO(Long id, String nome, String telefone, String email, 
                              StatusUsuario statusUsuario, TipoUsuario tipo, 
                              List<ServicoResponseDTO> servicos, 
                              List<HorarioUsuarioResponseDTO> horarios) {
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
        this.statusUsuario = statusUsuario;
        this.tipo = tipo;
        this.servicos = servicos; 
        this.horarios = horarios;
        this.ativo = (statusUsuario == StatusUsuario.ATIVO);
    }
    
    // GETs & SETs
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public StatusUsuario getStatusUsuario() { return statusUsuario; }
    public void setStatusUsuario(StatusUsuario statusUsuario) { 
        this.statusUsuario = statusUsuario; 
        this.ativo = (statusUsuario == StatusUsuario.ATIVO);
    }
    
    public TipoUsuario getTipo() { return tipo; }
    public void setTipo(TipoUsuario tipo) { this.tipo = tipo; }

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
    
    public List<ServicoResponseDTO> getServicos() { return servicos; }
    public void setServicos(List<ServicoResponseDTO> servicos) { this.servicos = servicos; }

    public List<HorarioUsuarioResponseDTO> getHorarios() { return horarios; }
    public void setHorarios(List<HorarioUsuarioResponseDTO> horarios) { this.horarios = horarios; }
}