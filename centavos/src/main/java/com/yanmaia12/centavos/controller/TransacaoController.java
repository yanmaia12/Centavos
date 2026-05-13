package com.yanmaia12.centavos.controller;

import com.yanmaia12.centavos.dtos.ResumoMensalDTO;
import com.yanmaia12.centavos.dtos.TransacaoDTO;
import com.yanmaia12.centavos.dtos.TransacaoResponseDTO;
import com.yanmaia12.centavos.dtos.UsuarioResponseDTO;
import com.yanmaia12.centavos.model.Usuario;
import com.yanmaia12.centavos.service.TransacaoService;
import com.yanmaia12.centavos.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/transacoes")
public class TransacaoController {
    private final TransacaoService transacaoService;

    public TransacaoController(TransacaoService transacaoService) {
        this.transacaoService = transacaoService;
    }

    @PostMapping("/criar")
    public ResponseEntity<TransacaoResponseDTO> criarTransacao(@RequestBody @Valid TransacaoDTO transacaoDTO, @AuthenticationPrincipal Usuario usuarioLogado){
        TransacaoResponseDTO transacaoResponseDTO = transacaoService.criarTransacao(transacaoDTO, usuarioLogado.getId());
        return ResponseEntity.status(201).body(transacaoResponseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> apagarTransacao(@PathVariable Long id, @AuthenticationPrincipal Usuario usuarioLogado){
        transacaoService.apagarTransacao(id, usuarioLogado.getId());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<TransacaoResponseDTO> atualizarTransacao(@PathVariable Long id, @RequestBody @Valid TransacaoDTO transacaoDTO, @AuthenticationPrincipal Usuario usuarioLogado){
        TransacaoResponseDTO transacaoResponseDTO = transacaoService.atualizarTransacao(id, transacaoDTO, usuarioLogado.getId());
        return ResponseEntity.ok(transacaoResponseDTO);
    }

    @GetMapping("/usuario")
    public ResponseEntity<List<TransacaoResponseDTO>> listarTransacoesUsuario(@AuthenticationPrincipal Usuario usuarioLogado, @RequestParam(required = false) String categoriaString){
        if (categoriaString != null){
            List<TransacaoResponseDTO> listaTransacoesDTO = transacaoService.filtrarPorCategoria(usuarioLogado.getId(), categoriaString);
            return ResponseEntity.ok(listaTransacoesDTO);
        }
        List<TransacaoResponseDTO> listaTransacoesDTO = transacaoService.listarTransacoesUsuario(usuarioLogado.getId());
        return ResponseEntity.ok(listaTransacoesDTO);
    }

    @GetMapping("/usuario/resumo-mensal")
    public ResponseEntity<Map<String, ResumoMensalDTO>> resumoMensal(@AuthenticationPrincipal Usuario usuarioLogado){
        Map<String, ResumoMensalDTO> resumoMensal = transacaoService.resumoMensal(usuarioLogado.getId());
        return ResponseEntity.ok(resumoMensal);
    }
}
