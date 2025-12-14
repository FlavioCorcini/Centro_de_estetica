package com.cefet.centro_de_estetica.dto;

import java.util.List;

import com.cefet.centro_de_estetica.entity.HorarioUsuario;
import com.cefet.centro_de_estetica.enums.StatusUsuario;
import com.cefet.centro_de_estetica.enums.TipoUsuario;

//DTO para receber dados de criação e atualização de Usuario
public record UsuarioRequestDTO (
	 String nome,
	 String telefone,
	 String email,
	 String senha,
	 StatusUsuario statusUsuario,
	 TipoUsuario tipo,
	 List<Long> servicosIDs,
	 List<HorarioUsuario> horarios
){}


