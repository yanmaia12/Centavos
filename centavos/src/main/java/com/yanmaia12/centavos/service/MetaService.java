package com.yanmaia12.centavos.service;

import com.yanmaia12.centavos.dtos.MetaDto;
import com.yanmaia12.centavos.dtos.MetaResponseDTO;
import com.yanmaia12.centavos.model.Meta;
import com.yanmaia12.centavos.model.Transacao;
import com.yanmaia12.centavos.model.Usuario;
import com.yanmaia12.centavos.repository.MetaRepository;
import com.yanmaia12.centavos.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import jdk.dynalink.linker.LinkerServices;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.nio.file.OpenOption;
import java.util.List;
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

    @Transactional
    public List<MetaResponseDTO> listarMetasUsuario(Long userId){
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(userId);
        if (usuarioOptional.isEmpty()){
            throw new RuntimeException("Usuário não encontrado!");
        }

        List<Meta> listaMetas = metaRepository.findByUsuarioId(userId);

        return listaMetas.stream().map(m -> new MetaResponseDTO(m.getId(), m.getNome(), m.getDescricao(),
                m.getValorFinal(), m.getValorAtual(), m.getData(), m.getFinalizada())).toList();
    }

    @Transactional
    public void apagarMeta(Long metaId, Long userId){
        Optional<Meta> metaOptional = metaRepository.findById(metaId);
        if (metaOptional.isEmpty()) throw new RuntimeException("Meta não encontrada!");

        Meta meta = metaOptional.get();

        if (!meta.getUsuario().getId().equals(userId)) throw new RuntimeException("Acesso negado! Você não pode apagar a meta de outro usuário!");

        for (Transacao transacao : meta.getTransacoes()){
            transacao.setMeta(null);
        }

        metaRepository.deleteById(metaId);
    }

    @Transactional
    public MetaResponseDTO atualizarMeta(Long metaId, MetaDto metaDto, Long userId){
        Optional<Meta> metaOptional = metaRepository.findById(metaId);
        if (metaOptional.isEmpty()) throw new RuntimeException("Meta não encontrada!");

        Meta meta = metaOptional.get();

        if (!meta.getUsuario().getId().equals(userId)) throw new RuntimeException("Acesso negado! Você não pode atualizar a meta de outro usuário!");

        meta.setNome(metaDto.nome());
        meta.setDescricao(metaDto.descricao());
        meta.setValorFinal(metaDto.valorFinal());

        if (meta.getValorFinal().compareTo(meta.getValorAtual()) <= 0){
            meta.setFinalizada(true);
        }else meta.setFinalizada(false);

        metaRepository.save(meta);
        return new MetaResponseDTO(meta.getId(), meta.getNome(), meta.getDescricao(),
                meta.getValorFinal(), meta.getValorAtual(), meta.getData(), meta.getFinalizada());
    }
}
