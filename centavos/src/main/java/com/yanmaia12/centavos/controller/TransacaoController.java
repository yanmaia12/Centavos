package com.yanmaia12.centavos.controller;

import com.yanmaia12.centavos.dtos.ResumoMensalDTO;
import com.yanmaia12.centavos.dtos.TransacaoDTO;
import com.yanmaia12.centavos.dtos.TransacaoResponseDTO;
import com.yanmaia12.centavos.dtos.UsuarioResponseDTO;
import com.yanmaia12.centavos.service.TransacaoService;
import com.yanmaia12.centavos.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<TransacaoResponseDTO>> listarTransacoesUsuario(@PathVariable Long id, @RequestParam(required = false) String categoriaString){
        if (categoriaString != null){
            List<TransacaoResponseDTO> listaTransacoesDTO = transacaoService.filtrarPorCategoria(id, categoriaString);
            return ResponseEntity.ok(listaTransacoesDTO);
        }
        List<TransacaoResponseDTO> listaTransacoesDTO = transacaoService.listarTransacoesUsuario(id);
        return ResponseEntity.ok(listaTransacoesDTO);
    }

    @GetMapping("/usuario/{id}/resumo-mensal")
    public ResponseEntity<Map<String, ResumoMensalDTO>> resumoMensal(@PathVariable Long id){
        Map<String, ResumoMensalDTO> resumoMensal = transacaoService.resumoMensal(id);
        return ResponseEntity.ok(resumoMensal);
    }
}
