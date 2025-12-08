package com.cefet.centro_de_estetica.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cefet.centro_de_estetica.entity.Area;

@Repository
public interface AreaRepository extends JpaRepository<Area, Long> {
}
