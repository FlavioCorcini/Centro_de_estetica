package com.cefet.centro_de_estetica.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.cefet.centro_de_estetica.dto.UsuarioRequestDTO;
import com.cefet.centro_de_estetica.dto.UsuarioResponseDTO;
import com.cefet.centro_de_estetica.entity.HorarioUsuario;
import com.cefet.centro_de_estetica.entity.Servico;
import com.cefet.centro_de_estetica.entity.Usuario;
import com.cefet.centro_de_estetica.mapper.UsuarioMapper;
import com.cefet.centro_de_estetica.repository.ServicoRepository;
import com.cefet.centro_de_estetica.repository.UsuarioRepository;

@Service
public class UsuarioService {

	private final UsuarioRepository repository;

	private final ServicoRepository servicoRepository;

	private final UsuarioMapper mapper;

	// construtor
	UsuarioService(UsuarioRepository repository, UsuarioMapper mapper, ServicoRepository servicoRepository) {
		this.repository = repository;
		this.mapper = mapper;
		this.servicoRepository = servicoRepository;
	}

	// metodos
	public UsuarioResponseDTO salvar(UsuarioRequestDTO requestDTO) {
		if (repository.existsByEmail(requestDTO.email())) {
			throw new RuntimeException("Email já cadastrado!");
		}
		//
		Usuario usuario = mapper.toEntity(requestDTO);
		// se for funcionario é pra pegar os serviços
		if (requestDTO.servicosIDs() != null && !requestDTO.servicosIDs().isEmpty()) {

			List<Servico> servicosEncontrados = servicoRepository.findAllById(requestDTO.servicosIDs());
			usuario.setServicos(servicosEncontrados);
		}
		// salva no banco
		Usuario usuarioSalvo = repository.save(usuario);
		//
		return new UsuarioResponseDTO(usuarioSalvo);
	}

	public List<UsuarioResponseDTO> listarTodos() {
		return repository.findAll().stream().map(mapper::toResponseDTO).collect(Collectors.toList());
	}

	public Optional<UsuarioResponseDTO> buscarPorId(Long id) {
		return repository.findById(id).map(mapper::toResponseDTO);
	}

	public Optional<UsuarioResponseDTO> buscarPorEmail(String email) {
		return repository.findByEmail(email).map(mapper::toResponseDTO);
	}

	public UsuarioResponseDTO atualizar(Long id, UsuarioRequestDTO dto) {
		// Tenta encontrar no banco.
		Usuario usuarioEncontrado = repository.findById(id)
				.orElseThrow(() -> new RuntimeException("Usuário não encontrado com id: " + id)); // se não encontrar

		usuarioEncontrado.setNome(dto.nome());
		usuarioEncontrado.setTelefone(dto.telefone());
		usuarioEncontrado.setEmail(dto.email());
		usuarioEncontrado.setSenha(dto.senha()); // adicionar criptografia no futuro. ESSENCIAL
		usuarioEncontrado.setStatusUsuario(dto.statusUsuario());
		usuarioEncontrado.setTipo(dto.tipo());

		if (dto.horarios() != null) {
			// Limpa a lista atual
			usuarioEncontrado.getHorarios().clear();

			List<HorarioUsuario> novosHorarios = dto.horarios().stream().map(horario -> {
				HorarioUsuario novo = new HorarioUsuario();
				novo.setDiaSemana(horario.getDiaSemana());
				novo.setHorario(horario.getHorario());
				novo.setFuncionario(usuarioEncontrado);
				return novo;
			}).collect(Collectors.toList());

			// Adiciona na lista existente
			usuarioEncontrado.getHorarios().addAll(novosHorarios);
		}
		if (dto.servicosIDs() != null) {
			List<Servico> novosServicos = servicoRepository.findAllById(dto.servicosIDs());
			usuarioEncontrado.setServicos(novosServicos);
		}

		// Salva a entidade atualizada de volta no banco de dados.
		Usuario usuarioAtualizada = repository.save(usuarioEncontrado);

		// Converte a entidade atualizada para um DTO de resposta.
		return mapper.toResponseDTO(usuarioAtualizada);
	}

	public void deletar(Long id) {
		// verificação de existência antes de deletar.

		if (!repository.existsById(id)) {
			throw new RuntimeException("Não foi possível deletar. Usuario com id " + id + " não encontrado.");
		}
		repository.deleteById(id);
	}

}
