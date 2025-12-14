package com.cefet.centro_de_estetica.dto;

import java.util.List;

import com.cefet.centro_de_estetica.entity.HorarioUsuario;
import com.cefet.centro_de_estetica.entity.Servico;
import com.cefet.centro_de_estetica.entity.Usuario;
import com.cefet.centro_de_estetica.enums.StatusUsuario;
import com.cefet.centro_de_estetica.enums.TipoUsuario;

//DTO para enviar dados de Usuario como resposta
public class UsuarioResponseDTO {
	private Long id;
	private String nome;
	private String telefone;
	private String email;
	private StatusUsuario statusUsuario;
	private TipoUsuario tipo;
	private List<Servico> servicos;
	private List<HorarioUsuario> horarios;
	
	// Construtor que converte Entidade -> DTO 
	public UsuarioResponseDTO() {
	}
	
    public UsuarioResponseDTO(Usuario usuario) {
        this.id = usuario.getId();
        this.nome = usuario.getNome();
        this.telefone = usuario.getTelefone();
        this.email = usuario.getEmail();
        this.statusUsuario = usuario.getStatusUsuario();
        this.tipo = usuario.getTipo();
        this.servicos = usuario.getServicos(); 
        this.horarios = usuario.getHorarios();
    }
    
    public UsuarioResponseDTO(Long id, String nome, String telefone, String email, 
    							 StatusUsuario statusUsuario, TipoUsuario tipo, List<Servico> servicos, List<HorarioUsuario> horarios) {
    	this.id = id;
    	this.nome = nome;
    	this.telefone = telefone;
    	this.email = email;
    	this.statusUsuario = statusUsuario;
    	this.tipo = tipo;
    	this.servicos = servicos; 
    	this.horarios = horarios;
    }
	
	//GETs & SETs
	
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
	public String getTelefone() {
		return telefone;
	}
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public StatusUsuario getStatusUsuario() {
		return statusUsuario;
	}
	public void setStatusUsuario(StatusUsuario statusUsuario) {
		this.statusUsuario = statusUsuario;
	}
	public TipoUsuario getTipo() {
		return tipo;
	}
	public void setTipo(TipoUsuario tipo) {
		this.tipo = tipo;
	}
	public List<Servico> getServicos() {
		return servicos;
	}
	public void setServicos(List<Servico> servicos) {
		this.servicos = servicos;
	}

	public List<HorarioUsuario> getHorarios() {
		return horarios;
	}

	public void setHorarios(List<HorarioUsuario> horarios) {
		this.horarios = horarios;
	}
	
	
}
