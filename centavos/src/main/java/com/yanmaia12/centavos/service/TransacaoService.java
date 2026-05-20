package com.yanmaia12.centavos.service;

import com.yanmaia12.centavos.dtos.ResumoMensalDTO;
import com.yanmaia12.centavos.dtos.TransacaoDTO;
import com.yanmaia12.centavos.dtos.TransacaoResponseDTO;
import com.yanmaia12.centavos.enums.Categoria;
import com.yanmaia12.centavos.enums.TipoTransacao;
import com.yanmaia12.centavos.exception.AccessDeniedException;
import com.yanmaia12.centavos.exception.BusinessException;
import com.yanmaia12.centavos.exception.ResourceNotFoundException;
import com.yanmaia12.centavos.model.Meta;
import com.yanmaia12.centavos.model.Transacao;
import com.yanmaia12.centavos.model.Usuario;
import com.yanmaia12.centavos.repository.MetaRepository;
import com.yanmaia12.centavos.repository.TransacaoRepository;
import com.yanmaia12.centavos.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransacaoService {
    private final UsuarioRepository usuarioRepository;
    private final TransacaoRepository transacaoRepository;
    private final MetaRepository metaRepository;

    public TransacaoService(UsuarioRepository usuarioRepository, TransacaoRepository transacaoRepository, MetaRepository metaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.transacaoRepository = transacaoRepository;
        this.metaRepository = metaRepository;
    }

    @Transactional
    public TransacaoResponseDTO criarTransacao(TransacaoDTO transacaoDTO, Long usuarioId){
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(usuarioId);
        if (usuarioOptional.isEmpty()){
            throw new ResourceNotFoundException("Usuário não existe!");
        }
        Transacao transacao = new Transacao(transacaoDTO.valor(), transacaoDTO.descricao(),
                transacaoDTO.data(), transacaoDTO.tipo(), transacaoDTO.categoria(), usuarioOptional.get());

        Long metaId = transacaoDTO.metaId();

        if (metaId != null){

            if (transacao.getCategoria() != Categoria.COFRE) throw new BusinessException("Apenas transações da categoria cofre podem ir para metas!");

            Meta meta = metaRepository.findById(metaId)
                    .orElseThrow(() -> new ResourceNotFoundException("Meta não encontrada"));

            transacao.setMeta(meta);
            meta.setValorAtual(meta.getValorAtual().add(transacao.getValor()));
            if (meta.getValorFinal().compareTo(meta.getValorAtual()) <= 0){
                meta.setFinalizada(true);
            }

            metaRepository.save(meta);
        }

        transacao = transacaoRepository.save(transacao);
        return new TransacaoResponseDTO(transacao.getId(), transacao.getValor(),
                transacao.getDescricao(), transacao.getData(), transacao.getTipo(), transacao.getCategoria(), transacao.getMeta() != null ? transacao.getMeta().getId() : null);
    }

    @Transactional
    public void apagarTransacao(Long id, Long usuarioId){
        Optional<Transacao> transacaoOptional = transacaoRepository.findById(id);
        if (transacaoOptional.isEmpty()){
            throw new ResourceNotFoundException("Transação não encontrada");
        }

        Transacao transacao = transacaoOptional.get();

        if (!transacao.getUsuario().getId().equals(usuarioId)){
            throw new AccessDeniedException("Acesso negado: você não tem permissão para apagar a transação de outro usuário!");
        }

        if (transacao.getMeta() != null){
            Meta meta = transacao.getMeta();
            meta.setValorAtual(meta.getValorAtual().subtract(transacao.getValor()));

            if (meta.getValorFinal().compareTo(meta.getValorAtual()) > 0){
                meta.setFinalizada(false);
            }

            metaRepository.save(meta);
        }

        transacaoRepository.deleteById(id);
    }

    @Transactional
    public TransacaoResponseDTO atualizarTransacao(Long id, TransacaoDTO transacaoDTO,Long usuarioID){
        Optional<Transacao> transacaoOptional = transacaoRepository.findById(id);
        if (transacaoOptional.isEmpty()){
            throw new ResourceNotFoundException("Não existe nenhuma transação com esse ID!");
        }

        Transacao transacao = transacaoOptional.get();

        if (!transacao.getUsuario().getId().equals(usuarioID)){
            throw new AccessDeniedException("Acesso negado: você não tem permissão para atualizar a transação de outro usuário!");
        }

        if (transacao.getMeta() != null){
            Meta meta = transacao.getMeta();
            meta.setValorAtual(meta.getValorAtual().subtract(transacao.getValor()));

            if (meta.getValorFinal().compareTo(meta.getValorAtual()) > 0){
                meta.setFinalizada(false);
            }
            metaRepository.save(meta);
        }


        transacao.setValor(transacaoDTO.valor());
        transacao.setDescricao(transacaoDTO.descricao());
        transacao.setData(transacaoDTO.data());
        transacao.setTipo(transacaoDTO.tipo());
        transacao.setCategoria(transacaoDTO.categoria());

        if (transacaoDTO.metaId() != null){

            if (transacao.getCategoria() != Categoria.COFRE) throw new BusinessException("Apenas transações da categoria cofre podem ir para metas!");

            Optional<Meta> metaOptional = metaRepository.findById(transacaoDTO.metaId());
            if (metaOptional.isEmpty()) throw new ResourceNotFoundException("Nenhuma meta encontrada com esse id!");

            Meta meta = metaOptional.get();

            transacao.setMeta(meta);
            meta.setValorAtual(meta.getValorAtual().add(transacao.getValor()));

            if (meta.getValorFinal().compareTo(meta.getValorAtual()) <= 0) meta.setFinalizada(true);

            metaRepository.save(meta);
        }else transacao.setMeta(null);

        transacao = transacaoRepository.save(transacao);

        return new TransacaoResponseDTO(transacao.getId(), transacao.getValor(), transacao.getDescricao(),
                transacao.getData(), transacao.getTipo(), transacao.getCategoria(), transacao.getMeta() != null ? transacao.getMeta().getId() : null);
    }

    @Transactional()
    public List<TransacaoResponseDTO> listarTransacoesUsuario(Long usuarioId){
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(usuarioId);
        if (usuarioOptional.isEmpty()){
            throw new ResourceNotFoundException("Usuário não cadastrado!");
        }
        List<Transacao> listaTransacao = transacaoRepository.findByUsuarioId(usuarioId);
        return listaTransacao.stream().map(t -> new TransacaoResponseDTO(t.getId(), t.getValor(),
                t.getDescricao(), t.getData(), t.getTipo(), t.getCategoria(), t.getMeta() != null ? t.getMeta().getId() : null)).toList();
    }

    @Transactional
    public List<TransacaoResponseDTO> filtrarPorCategoria(Long usuarioId, String categoriaString){
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(usuarioId);
        if (usuarioOptional.isEmpty()){
            throw new ResourceNotFoundException("Usuário não encontrado");
        }

        Usuario usuario = usuarioOptional.get();
        Categoria categoria;
        try {
            categoria = Categoria.valueOf(categoriaString);
        }catch (IllegalArgumentException e){
            throw new BusinessException("Categoria inválida: " + categoriaString);
        }

        return transacaoRepository.findByUsuarioIdAndCategoria(usuario.getId(), categoria).stream().map(t -> new TransacaoResponseDTO(t.getId(), t.getValor(), t.getDescricao(), t.getData(),
                t.getTipo(), t.getCategoria(), t.getMeta().getId())).toList();
    }

    @Transactional
    public Map<String, ResumoMensalDTO> resumoMensal(Long usuarioId){
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(usuarioId);
        if (usuarioOptional.isEmpty()){
            throw new ResourceNotFoundException("Usuário não encontrado");
        }
        Usuario usuario = usuarioOptional.get();

        return usuario.getTransacoes().stream()
                .collect(Collectors.groupingBy(t -> YearMonth.from(t.getData()).toString(),
                        Collectors.collectingAndThen(Collectors.toList(), transacoes ->{
                            BigDecimal receita = transacoes.stream().filter(t -> t.getTipo() == TipoTransacao.RECEITA)
                                    .map(Transacao::getValor).reduce(BigDecimal.ZERO, BigDecimal::add);

                            BigDecimal despesa = transacoes.stream().filter(t -> t.getTipo() == TipoTransacao.DESPESA)
                                    .map(Transacao::getValor).reduce(BigDecimal.ZERO, BigDecimal::add);

                            List<TransacaoResponseDTO> listaDTOS = transacoes.stream().map(t -> new TransacaoResponseDTO(t.getId(), t.getValor(),
                                    t.getDescricao(), t.getData(), t.getTipo(), t.getCategoria(), t.getMeta() != null ? t.getMeta().getId() : null)).toList();

                            return new ResumoMensalDTO(receita, despesa, listaDTOS);
                        })));
    }
}
