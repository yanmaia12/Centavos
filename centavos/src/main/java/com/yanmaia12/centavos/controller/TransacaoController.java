package com.yanmaia12.centavos.controller;

import com.yanmaia12.centavos.dtos.TransacaoDTO;
import com.yanmaia12.centavos.dtos.TransacaoResponseDTO;
import com.yanmaia12.centavos.service.TransacaoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transacoes")
public class TransacaoController {
    private final TransacaoService transacaoService;

    public TransacaoController(TransacaoService transacaoService) {
        this.transacaoService = transacaoService;
    }

    @PostMapping("/criar")
    public ResponseEntity<TransacaoResponseDTO> criarTransacao(@RequestBody @Valid TransacaoDTO transacaoDTO){
        TransacaoResponseDTO transacaoResponseDTO = transacaoService.criarTransacao(transacaoDTO);
        return ResponseEntity.status(201).body(transacaoResponseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> apagarTransacao(@PathVariable Long id){
        transacaoService.apagarTransacao(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<TransacaoResponseDTO> atualizarTransacao(@RequestBody @Valid TransacaoDTO transacaoDTO, @PathVariable Long id){
        TransacaoResponseDTO transacaoResponseDTO = transacaoService.atualizarTransacao(transacaoDTO, id);
        return ResponseEntity.ok(transacaoResponseDTO);
    }

    @GetMapping("/usuario/{id}")
    public ResponseEntity<List<TransacaoResponseDTO>> listarTransacoesUsuario(@PathVariable Long id){
        List<TransacaoResponseDTO> listaTransacoesDTO = transacaoService.listarTransacoesUsuario(id);
        return ResponseEntity.ok(listaTransacoesDTO);
    }
}
