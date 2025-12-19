package com.cefet.centro_de_estetica.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cefet.centro_de_estetica.entity.Usuario;
import com.cefet.centro_de_estetica.enums.TipoUsuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
	Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);

    List<Usuario> findByServicos_Id(Long id);
    
    List<Usuario> findByTipo(TipoUsuario tipo);
    
    List<Usuario> findByNomeContainingIgnoreCaseAndTipo(String nome, TipoUsuario tipo);
}
