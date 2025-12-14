package com.cefet.centro_de_estetica.mapper;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.cefet.centro_de_estetica.dto.UsuarioRequestDTO;
import com.cefet.centro_de_estetica.dto.UsuarioResponseDTO;
import com.cefet.centro_de_estetica.entity.HorarioUsuario;
import com.cefet.centro_de_estetica.entity.Usuario;

@Component
public class UsuarioMapper {
	public UsuarioResponseDTO toResponseDTO(Usuario usuario) {
		if(usuario == null) return null;// talvez seja melhor lançar uma exceção
	
		return new UsuarioResponseDTO(usuario);
	}
	
	public Usuario toEntity(UsuarioRequestDTO dto) {
		if (dto == null) return null;
		
		Usuario usuario = new Usuario();
		usuario.setNome(dto.nome());
	    usuario.setTelefone(dto.telefone());
	    usuario.setEmail(dto.email());
	    usuario.setSenha(dto.senha()); 
	    usuario.setStatusUsuario(dto.statusUsuario());
	    usuario.setTipo(dto.tipo());
	    
	    if (dto.horarios() != null && !dto.horarios().isEmpty()) {
            List<HorarioUsuario> listaHorarios = dto.horarios().stream().map(horarioDTO -> {
                HorarioUsuario novoHorario = new HorarioUsuario();
                
                novoHorario.setDiaSemana(horarioDTO.getDiaSemana());
                novoHorario.setHorario(horarioDTO.getHorario());
                
                novoHorario.setFuncionario(usuario); 
                
                return novoHorario;
            }).collect(Collectors.toList());
            
            usuario.setHorarios(listaHorarios);
        }
	    
	 // 	A lista de serviços (servicosIds) NÃO é mapeada aqui.
	 // isso é feito no Service usando o repository, pois precisamos buscar os objetos no banco.
	    
		return usuario;
	}
}
