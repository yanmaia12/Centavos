package com.yanmaia12.centavos.controller;

import com.yanmaia12.centavos.config.JwtService;
import com.yanmaia12.centavos.config.SecurityConfiguration;
import com.yanmaia12.centavos.dtos.*;
import com.yanmaia12.centavos.model.Usuario;
import com.yanmaia12.centavos.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final AuthenticationManager manager;
    private final JwtService jwtService;

    public UsuarioController(UsuarioService usuarioService, AuthenticationManager manager, JwtService jwtService) {
        this.usuarioService = usuarioService;
        this.manager = manager;
        this.jwtService = jwtService;
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<UsuarioResponseDTO> cadastrarUsuario(@RequestBody @Valid CadastroDTO cadastroDTO){
        UsuarioResponseDTO usuarioResponseDTO = usuarioService.cadastrarUsuario(cadastroDTO);
        return ResponseEntity.status(201).body(usuarioResponseDTO);

    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> logarUsuario(@RequestBody @Valid LoginDTO loginDTO){
        var authenticationToken = new UsernamePasswordAuthenticationToken(loginDTO.email(), loginDTO.senha());
        var autentication = manager.authenticate(authenticationToken);
        Usuario usuarioLogado = (Usuario) autentication.getPrincipal();
        var tokenJwt = jwtService.gerarToken(usuarioLogado);
        return ResponseEntity.ok(new TokenResponseDTO(tokenJwt, usuarioLogado.getId(), usuarioLogado.getNome(), usuarioLogado.getEmail(), usuarioLogado.getMoeda()));
    }

    @PutMapping("/moeda")
    public ResponseEntity<UsuarioResponseDTO> atualizarMoeda(@AuthenticationPrincipal Usuario usuarioLogado, @RequestParam String moeda){
        UsuarioResponseDTO usuarioResponseDTO = usuarioService.atualizarMoeda(usuarioLogado.getId(), moeda);
        return ResponseEntity.ok(usuarioResponseDTO);
    }

    @GetMapping("/saldo")
    public ResponseEntity<BigDecimal> getSaldo(@AuthenticationPrincipal Usuario usuarioLogado){
        BigDecimal saldo = usuarioService.calcularSaldo(usuarioLogado.getId());
        return ResponseEntity.ok(saldo);
    }

}
