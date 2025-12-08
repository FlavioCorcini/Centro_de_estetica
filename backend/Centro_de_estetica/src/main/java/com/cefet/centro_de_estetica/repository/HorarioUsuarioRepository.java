package com.cefet.centro_de_estetica.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cefet.centro_de_estetica.entity.HorarioUsuario;

@Repository
public interface HorarioUsuarioRepository extends JpaRepository<HorarioUsuario, Long> {
    List<HorarioUsuario> findByFuncionarioId(Long idFuncionario);
}
