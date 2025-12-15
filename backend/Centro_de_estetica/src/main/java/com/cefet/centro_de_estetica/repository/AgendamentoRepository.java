package com.cefet.centro_de_estetica.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cefet.centro_de_estetica.entity.Agendamento;
import com.cefet.centro_de_estetica.entity.Usuario;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    // Verifica se o Funcionário já está ocupado naquele horário
    boolean existsByFuncionarioAndDataHora(Usuario funcionario, LocalDateTime dataHora);

    // Verifica se o Cliente já tem algo marcado na mesma hora
    boolean existsByClienteAndDataHora(Usuario cliente, LocalDateTime dataHora);

    List<Agendamento> findByCliente(Usuario cliente);
    
    // Para o funcionário ver a agenda dele
    List<Agendamento> findByFuncionario(Usuario funcionario);
    
    List<Agendamento> findByDataHoraBetween(LocalDateTime inicio, LocalDateTime fim);
    
    // Verifica conflito ignorando os cancelados.
    @Query("SELECT COUNT(a) > 0 FROM Agendamento a " +
           "WHERE a.funcionario = :funcionario " +
           "AND a.dataHora = :dataHora " +
           "AND a.status <> 'CANCELADO'")
    boolean existeConflitoDeHorario(@Param("funcionario") Usuario funcionario, 
                                    @Param("dataHora") LocalDateTime dataHora);
}