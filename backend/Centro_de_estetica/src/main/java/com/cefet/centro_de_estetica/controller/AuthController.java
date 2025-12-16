package com.cefet.centro_de_estetica.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cefet.centro_de_estetica.dto.LoginRequestDTO;
import com.cefet.centro_de_estetica.dto.UsuarioResponseDTO; 
import com.cefet.centro_de_estetica.entity.Usuario;
import com.cefet.centro_de_estetica.repository.UsuarioRepository;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/login")
    public ResponseEntity<UsuarioResponseDTO> login(@RequestBody LoginRequestDTO login) {
        
        Usuario usuario = usuarioRepository.findByEmail(login.email()).orElse(null);

        // Validação de senha
        if (usuario == null || !usuario.getSenha().equals(login.senha())) {
            return ResponseEntity.status(401).build();
        }

        UsuarioResponseDTO resposta = new UsuarioResponseDTO(usuario);

        return ResponseEntity.ok(resposta);
    }
}