package com.cefet.centro_de_estetica.dto;

import java.util.List;

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
	
	// Construtor que converte Entidade -> DTO 
    public UsuarioResponseDTO(Usuario usuario) {
        this.id = usuario.getId();
        this.nome = usuario.getNome();
        this.telefone = usuario.getTelefone();
        this.email = usuario.getEmail();
        this.statusUsuario = usuario.getStatusUsuario();
        this.tipo = usuario.getTipo();
        this.servicos = usuario.getServicos(); // Passa a lista de serviços
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
	
	
}
