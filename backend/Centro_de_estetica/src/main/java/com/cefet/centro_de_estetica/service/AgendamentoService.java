package com.cefet.centro_de_estetica.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.cefet.centro_de_estetica.dto.AgendamentoRequestDTO;
import com.cefet.centro_de_estetica.dto.AgendamentoResponseDTO;
import com.cefet.centro_de_estetica.entity.Agendamento;
import com.cefet.centro_de_estetica.entity.Servico;
import com.cefet.centro_de_estetica.entity.Usuario;
import com.cefet.centro_de_estetica.enums.StatusAgendamento;
import com.cefet.centro_de_estetica.mapper.AgendamentoMapper; 
import com.cefet.centro_de_estetica.repository.AgendamentoRepository;
import com.cefet.centro_de_estetica.repository.ServicoRepository;
import com.cefet.centro_de_estetica.repository.UsuarioRepository;

@Service
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ServicoRepository servicoRepository;
    private final AgendamentoMapper mapper; 

    public AgendamentoService(AgendamentoRepository agendamentoRepository, 
                              UsuarioRepository usuarioRepository,
                              ServicoRepository servicoRepository,
                              AgendamentoMapper mapper) {
        this.agendamentoRepository = agendamentoRepository;
        this.usuarioRepository = usuarioRepository;
        this.servicoRepository = servicoRepository;
        this.mapper = mapper;
    }

    public AgendamentoResponseDTO criar(AgendamentoRequestDTO dto) {
        
        Usuario cliente = usuarioRepository.findById(dto.idCliente())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        
        Usuario funcionario = usuarioRepository.findById(dto.idFuncionario())
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado"));
        
        Servico servico = servicoRepository.findById(dto.idServico())
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));

        if (agendamentoRepository.existsByFuncionarioAndDataHora(funcionario, dto.dataHora())) {
            throw new RuntimeException("Horário indisponível! O funcionário já tem um agendamento confirmado neste horário.");
        }

        boolean fazServico = funcionario.getServicos().stream()
                .anyMatch(s -> s.getId().equals(servico.getId()));
        if (!fazServico) {
            throw new RuntimeException("Funcionário não realiza este serviço.");
        }

        Agendamento agendamento = new Agendamento();
        agendamento.setCliente(cliente);
        agendamento.setFuncionario(funcionario);
        agendamento.setServico(servico);
        agendamento.setDataHora(dto.dataHora());
        agendamento.setObservacoes(dto.observacoes());
        agendamento.setStatus(StatusAgendamento.AGENDADO);
        agendamento.setValorCobrado(servico.getValor());

        agendamentoRepository.save(agendamento);
        
        return mapper.toResponseDTO(agendamento);
    }
    
    public List<AgendamentoResponseDTO> listarTodos() {
        return agendamentoRepository.findAll().stream()
                .map(mapper::toResponseDTO) 
                .collect(Collectors.toList());
    }
    
    public List<AgendamentoResponseDTO> buscarPorCliente(Long idCliente) {
        Usuario cliente = usuarioRepository.findById(idCliente)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado com id: " + idCliente));
        
        return agendamentoRepository.findByCliente(cliente).stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<AgendamentoResponseDTO> buscarPorFuncionario(Long idFuncionario) {
        Usuario funcionario = usuarioRepository.findById(idFuncionario)
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado com id: " + idFuncionario));

        return agendamentoRepository.findByFuncionario(funcionario).stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // Buscar por DATA (O dia inteiro)
    public List<AgendamentoResponseDTO> buscarPorData(LocalDate data) {
        // Define o intervalo: 00:00:00 até 23:59:59
        LocalDateTime inicioDia = data.atStartOfDay();
        LocalDateTime fimDia = data.atTime(LocalTime.MAX);

        return agendamentoRepository.findByDataHoraBetween(inicioDia, fimDia).stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    public AgendamentoResponseDTO atualizar(Long id, AgendamentoRequestDTO dto) {
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado com id: " + id));

        // Busca as novas entidades (caso tenham mudado)
        Usuario novoFuncionario = usuarioRepository.findById(dto.idFuncionario())
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado"));
        Servico novoServico = servicoRepository.findById(dto.idServico())
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));
        Usuario novoCliente = usuarioRepository.findById(dto.idCliente())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        // Só validamos se houve mudança de horário OU de funcionário
        boolean mudouHorario = !agendamento.getDataHora().equals(dto.dataHora());
        boolean mudouFuncionario = !agendamento.getFuncionario().getId().equals(novoFuncionario.getId());

        if (mudouHorario || mudouFuncionario) {
            boolean horarioOcupado = agendamentoRepository.existeConflitoDeHorario(novoFuncionario, dto.dataHora());
            if (horarioOcupado) {
                throw new RuntimeException("O novo horário escolhido já está ocupado!");
            }
        }

        // Valida se o funcionário (novo ou atual) faz o serviço
        boolean fazServico = novoFuncionario.getServicos().stream()
                .anyMatch(s -> s.getId().equals(novoServico.getId()));
        if (!fazServico) {
            throw new RuntimeException("O funcionário selecionado não realiza este serviço.");
        }

        // Atualiza os dados
        agendamento.setCliente(novoCliente);
        agendamento.setFuncionario(novoFuncionario);
        agendamento.setServico(novoServico);
        agendamento.setDataHora(dto.dataHora());
        agendamento.setObservacoes(dto.observacoes());

        // Se mudou o serviço, atualiza o valor cobrado
        if (!agendamento.getServico().getId().equals(novoServico.getId())) {
             agendamento.setValorCobrado(novoServico.getValor());
        }

        agendamentoRepository.save(agendamento);
        return mapper.toResponseDTO(agendamento);
    }

    public void deletar(Long id) {
        if (!agendamentoRepository.existsById(id)) {
            throw new RuntimeException("Agendamento não encontrado para deletar.");
        }
        agendamentoRepository.deleteById(id);
    }

    public void atualizarStatus(Long id, StatusAgendamento novoStatus) {
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado."));
        
        // Não pode voltar para PENDENTE se já estava CONCLUIDO
        if (agendamento.getStatus() == StatusAgendamento.CONCLUIDO && novoStatus == StatusAgendamento.PENDENTE) {
             throw new RuntimeException("Não é possível reabrir um agendamento concluído.");
        }

        agendamento.setStatus(novoStatus);
        agendamentoRepository.save(agendamento);
    }
}