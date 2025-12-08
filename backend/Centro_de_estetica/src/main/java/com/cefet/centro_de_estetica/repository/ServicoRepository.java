package com.cefet.centro_de_estetica.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cefet.centro_de_estetica.entity.Servico;

@Repository
public interface ServicoRepository extends JpaRepository<Servico, Long> {
    List<Servico> findByAreaIdArea(Long idArea);
}
