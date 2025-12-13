package com.cefet.centro_de_estetica.dto;

import java.util.List;

import com.cefet.centro_de_estetica.enums.StatusUsuario;
import com.cefet.centro_de_estetica.enums.TipoUsuario;

//DTO para receber dados de criação e atualização de Usuario
public class UsuarioRequestDTO {
	private String nome;
	private String telefone;
	private String email;
	private String senha;
	private StatusUsuario statusUsuario;
	private TipoUsuario tipo;
	private List<Long> servicosIds;
	
	//GETs & SETs
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
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
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
	public List<Long> getServicosIds() { 
		return servicosIds; 
	}
    public void setServicosIds(List<Long> servicosIds) { 
    	this.servicosIds = servicosIds; 
    	}

	
	
}
