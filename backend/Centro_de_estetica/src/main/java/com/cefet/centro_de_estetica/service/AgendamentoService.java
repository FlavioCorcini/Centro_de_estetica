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

    public AgendamentoService(AgendamentoRepository agendamentoRepository, UsuarioRepository usuarioRepository,
            ServicoRepository servicoRepository, AgendamentoMapper mapper) {
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

        // --- LÓGICA DE BLOQUEIO POR INTERVALO (DURAÇÃO) ---
        LocalDateTime inicioDesejado = dto.dataHora();
        
        // Calcula o término somando a duração (LocalTime) ao início
        LocalDateTime fimDesejado = inicioDesejado
                .plusMinutes(servico.getTempoAtendimento());

        // Busca todos os agendamentos do profissional no dia selecionado
        LocalDateTime inicioDia = inicioDesejado.toLocalDate().atStartOfDay();
        LocalDateTime fimDia = inicioDesejado.toLocalDate().atTime(LocalTime.MAX);
        
        List<Agendamento> agendamentosDoDia = agendamentoRepository
                .findByFuncionarioAndDataHoraBetween(funcionario, inicioDia, fimDia);

        // Verifica sobreposição: (NovoInício < ExistenteFim) AND (NovoFim > ExistenteInício)
        for (Agendamento ag : agendamentosDoDia) {
            if (ag.getStatus() == StatusAgendamento.CANCELADO) continue;

            LocalDateTime exInicio = ag.getDataHora();
            LocalDateTime exFim = exInicio
                    .plusMinutes(ag.getServico().getTempoAtendimento());

            if (inicioDesejado.isBefore(exFim) && fimDesejado.isAfter(exInicio)) {
                throw new RuntimeException("Horário indisponível! Conflito com agendamento das " + exInicio.toLocalTime());
            }
        }
        // --- FIM DA LÓGICA DE BLOQUEIO ---

        boolean fazServico = funcionario.getServicos().stream().anyMatch(s -> s.getId().equals(servico.getId()));
        if (!fazServico) {
            throw new RuntimeException("Funcionário não realiza este serviço.");
        }

        Agendamento agendamento = new Agendamento();
        agendamento.setCliente(cliente);
        agendamento.setFuncionario(funcionario);
        agendamento.setServico(servico);
        agendamento.setDataHora(dto.dataHora());
        agendamento.setObservacoes(dto.observacoes());
        agendamento.setStatus(StatusAgendamento.PENDENTE);
        agendamento.setValorCobrado(servico.getValor());

        agendamentoRepository.save(agendamento);
        return mapper.toResponseDTO(agendamento);
    }

    public List<AgendamentoResponseDTO> listarTodos() {
        return agendamentoRepository.findAll().stream().map(mapper::toResponseDTO).collect(Collectors.toList());
    }

    public List<AgendamentoResponseDTO> buscarPorCliente(Long idCliente) {
        Usuario cliente = usuarioRepository.findById(idCliente)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado com id: " + idCliente));

        return agendamentoRepository.findByCliente(cliente).stream().map(mapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<AgendamentoResponseDTO> buscarPorFuncionario(Long idFuncionario) {
        Usuario funcionario = usuarioRepository.findById(idFuncionario)
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado com id: " + idFuncionario));

        return agendamentoRepository.findByFuncionario(funcionario).stream().map(mapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<AgendamentoResponseDTO> buscarAgendaDoDia(Long usuarioId, LocalDate data) {
        LocalDateTime inicioDia = data.atStartOfDay();
        LocalDateTime fimDia = data.atTime(LocalTime.MAX);
        
        List<Agendamento> agendamentos;

        if (usuarioId == null) {
            agendamentos = agendamentoRepository.findByDataHoraBetween(inicioDia, fimDia);
        } else {
            Usuario usuario = usuarioRepository.findById(usuarioId)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            switch (usuario.getTipo()) {
                case FUNCIONARIO:
                    agendamentos = agendamentoRepository.findByFuncionarioAndDataHoraBetween(usuario, inicioDia, fimDia);
                    break;
                case CLIENTE:
                    agendamentos = agendamentoRepository.findByClienteAndDataHoraBetween(usuario, inicioDia, fimDia);
                    break;
                case ADMIN:
                    agendamentos = agendamentoRepository.findByDataHoraBetween(inicioDia, fimDia);
                    break;
                default:
                    throw new RuntimeException("Tipo de usuário desconhecido");
            }
        }

        return agendamentos.stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<AgendamentoResponseDTO> buscarPorData(LocalDate data) {
        LocalDateTime inicioDia = data.atStartOfDay();
        LocalDateTime fimDia = data.atTime(LocalTime.MAX);

        return agendamentoRepository.findByDataHoraBetween(inicioDia, fimDia).stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());
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
        
        if (agendamento.getStatus() == StatusAgendamento.CONCLUIDO && novoStatus == StatusAgendamento.PENDENTE) {
             throw new RuntimeException("Não é possível reabrir um agendamento concluído.");
        }

        agendamento.setStatus(novoStatus);
        agendamentoRepository.save(agendamento);
    }
}