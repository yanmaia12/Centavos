package com.yanmaia12.centavos.controller;

import com.yanmaia12.centavos.dtos.CadastroDTO;
import com.yanmaia12.centavos.dtos.LoginDTO;
import com.yanmaia12.centavos.dtos.TransacaoResponseDTO;
import com.yanmaia12.centavos.dtos.UsuarioResponseDTO;
import com.yanmaia12.centavos.model.Usuario;
import com.yanmaia12.centavos.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<UsuarioResponseDTO> cadastrarUsuario(@RequestBody @Valid CadastroDTO cadastroDTO){
        UsuarioResponseDTO usuarioResponseDTO = usuarioService.cadastrarUsuario(cadastroDTO);
        return ResponseEntity.status(201).body(usuarioResponseDTO);

    }

    @PostMapping("/login")
    public ResponseEntity<UsuarioResponseDTO> logarUsuario(@RequestBody @Valid LoginDTO loginDTO){
        UsuarioResponseDTO usuarioResponseDTO = usuarioService.logarUsuario(loginDTO);
        return ResponseEntity.ok(usuarioResponseDTO);
    }

    @PutMapping("/{id}/moeda")
    public ResponseEntity<UsuarioResponseDTO> atualizarMoeda(@PathVariable Long id, @RequestParam String moeda){
        UsuarioResponseDTO usuarioResponseDTO = usuarioService.atualizarMoeda(id, moeda);
        return ResponseEntity.ok(usuarioResponseDTO);
    }

}
