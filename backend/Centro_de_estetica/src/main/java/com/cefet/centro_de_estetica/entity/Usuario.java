package com.cefet.centro_de_estetica.entity;


import com.cefet.centro_de_estetica.enums.StatusUsuario;
import com.cefet.centro_de_estetica.enums.TipoUsuario;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_usuario")
public class Usuario {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idUsuario")
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String nome;
    
    @Column(nullable = false, length = 11) 
    private String telefone;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String senha;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusUsuario statusUsuario;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoUsuario tipo;
    
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


    
	
}
