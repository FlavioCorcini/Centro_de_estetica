package com.cefet.centro_de_estetica.dto;

import java.time.LocalDateTime;
import com.cefet.centro_de_estetica.entity.Agendamento;
import com.cefet.centro_de_estetica.enums.StatusAgendamento;

public class AgendamentoResponseDTO {

    private Long id;
    private LocalDateTime dataHora;
    private StatusAgendamento status;
    private String observacoes;
    private ServicoResponseDTO servico;
    
    // Apenas nomes para facilitar o front-end
    private String nomeCliente;
    private String nomeFuncionario;

    public AgendamentoResponseDTO(Agendamento agendamento) {
        this.id = agendamento.getId();
        this.dataHora = agendamento.getDataHora();
        this.status = agendamento.getStatus();
        this.observacoes = agendamento.getObservacoes();
        
        // Null Safety (Evita erro se algo vier nulo)
        if (agendamento.getCliente() != null) 
            this.nomeCliente = agendamento.getCliente().getNome();
            
        if (agendamento.getFuncionario() != null) 
            this.nomeFuncionario = agendamento.getFuncionario().getNome();
            
        if (agendamento.getServico() != null) 
            this.servico = new ServicoResponseDTO(agendamento.getServico());
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }
    public StatusAgendamento getStatus() { return status; }
    public void setStatus(StatusAgendamento status) { this.status = status; }
    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
    public String getNomeCliente() { return nomeCliente; }
    public void setNomeCliente(String nomeCliente) { this.nomeCliente = nomeCliente; }
    public String getNomeFuncionario() { return nomeFuncionario; }
    public void setNomeFuncionario(String nomeFuncionario) { this.nomeFuncionario = nomeFuncionario; }
    public ServicoResponseDTO getServico() { return servico; }
    public void setServico(ServicoResponseDTO servico) { this.servico = servico; }
}