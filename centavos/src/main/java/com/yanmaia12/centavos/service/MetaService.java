package com.yanmaia12.centavos.service;

import com.yanmaia12.centavos.dtos.MetaDto;
import com.yanmaia12.centavos.dtos.MetaResponseDTO;
import com.yanmaia12.centavos.model.Meta;
import com.yanmaia12.centavos.model.Usuario;
import com.yanmaia12.centavos.repository.MetaRepository;
import com.yanmaia12.centavos.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class MetaService{

    private final MetaRepository metaRepository;
    private final UsuarioRepository usuarioRepository;

    public MetaService(MetaRepository metaRepository, UsuarioRepository usuarioRepository) {
        this.metaRepository = metaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public MetaResponseDTO criarMeta(MetaDto metaDto, Long userId){
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(userId);
        if (usuarioOptional.isEmpty()){
            throw new RuntimeException("Usuário não encontrado!");
        }
        Usuario usuario = usuarioOptional.get();

        Meta meta = new Meta();
        meta.setNome(metaDto.nome());
        meta.setDescricao(metaDto.descricao());
        meta.setValorFinal(metaDto.valorFinal());
        meta.setValorAtual(BigDecimal.ZERO);
        meta.setData(metaDto.data());
        meta.setFinalizada(false);
        meta.setUsuario(usuario);

        meta = metaRepository.save(meta);

        return new MetaResponseDTO(meta.getId(), meta.getNome(), meta.getDescricao(), meta.getValorFinal(), meta.getValorAtual(), meta.getData(), meta.getFinalizada());
    }
}
