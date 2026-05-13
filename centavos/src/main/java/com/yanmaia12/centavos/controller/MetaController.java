package com.yanmaia12.centavos.controller;

import com.yanmaia12.centavos.dtos.MetaDto;
import com.yanmaia12.centavos.dtos.MetaResponseDTO;
import com.yanmaia12.centavos.model.Usuario;
import com.yanmaia12.centavos.service.MetaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/metas")
public class MetaController {

    private final MetaService metaService;

    public MetaController(MetaService metaService) {
        this.metaService = metaService;
    }

    @PostMapping("/criar")
    public ResponseEntity<MetaResponseDTO> criarMeta(@RequestBody @Valid MetaDto metaDto, @AuthenticationPrincipal Usuario usuarioLogado){
        MetaResponseDTO metaResponseDTO = metaService.criarMeta(metaDto, usuarioLogado.getId());
        return ResponseEntity.status(201).body(metaResponseDTO);
    }

    @GetMapping("/usuario")
    public ResponseEntity<List<MetaResponseDTO>> listarMetasUsuario(@AuthenticationPrincipal Usuario usuarioLogado){
        List<MetaResponseDTO> listaMetas = metaService.listarMetasUsuario(usuarioLogado.getId());
        return ResponseEntity.ok(listaMetas);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> apagarMeta(@PathVariable Long id, @AuthenticationPrincipal Usuario usuarioLogado){
        metaService.apagarMeta(id, usuarioLogado.getId());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<MetaResponseDTO> atualizarMeta(@PathVariable Long id, @RequestBody @Valid MetaDto metaDto, @AuthenticationPrincipal Usuario usuarioLogado){
        MetaResponseDTO metaResponseDTO = metaService.atualizarMeta(id, metaDto, usuarioLogado.getId());
        return ResponseEntity.ok(metaResponseDTO);
    }
}
