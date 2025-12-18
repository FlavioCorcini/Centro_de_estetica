package com.cefet.centro_de_estetica.service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.cefet.centro_de_estetica.dto.ServicoRequestDTO;
import com.cefet.centro_de_estetica.dto.ServicoResponseDTO;
import com.cefet.centro_de_estetica.entity.Area;
import com.cefet.centro_de_estetica.entity.Servico;
import com.cefet.centro_de_estetica.repository.AreaRepository;
import com.cefet.centro_de_estetica.repository.ServicoRepository;

@Service
public class ServicoService {

    
    private final ServicoRepository repository;
    private final AreaRepository areaRepository;
    
    public ServicoService(ServicoRepository repository, AreaRepository areaRepository) {
    	this.repository = repository;
    	this.areaRepository =areaRepository;
	}
    
    public ServicoResponseDTO salvar(ServicoRequestDTO dto) {
    	Area area = areaRepository.findById(dto.idArea()).orElseThrow(() -> new RuntimeException("Área não encontrada com o ID: " + dto.idArea()));
    	
    	Servico servico = new Servico();
        servico.setNome(dto.nome());
        servico.setDescricao(dto.descricao());
        servico.setValor(dto.valor());
        
        if (dto.tempoAtendimento() % 15 != 0) {
            throw new RuntimeException("A duração do serviço deve ser múltipla de 15 minutos (ex: 15, 30, 45 min).");
        }
        servico.setTempoAtendimento(dto.tempoAtendimento());
        
        servico.setArea(area);
        
        Servico servicoSalvo = repository.save(servico);
        return new ServicoResponseDTO(servicoSalvo);
    }
    
    public List<ServicoResponseDTO> listarTodos() {
        return repository.findAll()
        		.stream().map(ServicoResponseDTO::new).collect(Collectors.toList());
    }
    
    public List<ServicoResponseDTO> listarPorArea(Long id) {
    	return repository.findAllByAreaId(id)
    			.stream().map(ServicoResponseDTO::new).collect(Collectors.toList());
    }

    public Optional<ServicoResponseDTO> buscarPorId(Long id) {
        return repository.findById(id)
        		.map(ServicoResponseDTO::new);
    }
    
    public ServicoResponseDTO atualizar(Long id, ServicoRequestDTO dto) {
        Servico servicoEncontrado = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));

        servicoEncontrado.setNome(dto.nome());
        servicoEncontrado.setDescricao(dto.descricao());
        servicoEncontrado.setValor(dto.valor());
        if (dto.tempoAtendimento() % 15 != 0) {
            throw new RuntimeException("A duração do serviço deve ser múltipla de 15 minutos (ex: 15, 30, 45 min).");
        }
        servicoEncontrado.setTempoAtendimento(dto.tempoAtendimento());

        // Se o ID enviado for diferente do atual, busca a nova área.
        if (!servicoEncontrado.getArea().getIdArea().equals(dto.idArea())) {
            Area novaArea = areaRepository.findById(dto.idArea())
                    .orElseThrow(() -> new RuntimeException("Nova área informada não encontrada"));
            servicoEncontrado.setArea(novaArea);
        }

        Servico servicoAtualizado = repository.save(servicoEncontrado);
        return new ServicoResponseDTO(servicoAtualizado);
    }
    
    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Serviço não encontrado para deletar.");
        }
        repository.deleteById(id);
    }
}