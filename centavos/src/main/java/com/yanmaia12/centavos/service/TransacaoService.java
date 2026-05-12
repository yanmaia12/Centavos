package com.yanmaia12.centavos.service;

import com.yanmaia12.centavos.dtos.ResumoMensalDTO;
import com.yanmaia12.centavos.dtos.TransacaoDTO;
import com.yanmaia12.centavos.dtos.TransacaoResponseDTO;
import com.yanmaia12.centavos.enums.Categoria;
import com.yanmaia12.centavos.enums.TipoTransacao;
import com.yanmaia12.centavos.model.Transacao;
import com.yanmaia12.centavos.model.Usuario;
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

    public TransacaoService(UsuarioRepository usuarioRepository, TransacaoRepository transacaoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.transacaoRepository = transacaoRepository;
    }

    @Transactional
    public TransacaoResponseDTO criarTransacao(TransacaoDTO transacaoDTO){
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(transacaoDTO.usuarioId());
        if (usuarioOptional.isEmpty()){
            throw new RuntimeException("Usuário não existe!");
        }
        Transacao transacao = new Transacao(transacaoDTO.valor(), transacaoDTO.descricao(),
                transacaoDTO.data(), transacaoDTO.tipo(), transacaoDTO.categoria(), usuarioOptional.get());

        transacao = transacaoRepository.save(transacao);
        return new TransacaoResponseDTO(transacao.getId(), transacao.getValor(),
                transacao.getDescricao(), transacao.getData(), transacao.getTipo(), transacao.getCategoria());
    }

    @Transactional
    public void apagarTransacao(Long id){
        if (!transacaoRepository.existsById(id)){
            throw new RuntimeException("Transação não encontrada");
        }
        transacaoRepository.deleteById(id);
    }

    @Transactional
    public TransacaoResponseDTO atualizarTransacao(TransacaoDTO transacaoDTO, Long id){
        Optional<Transacao> transacaoOptional = transacaoRepository.findById(id);
        if (transacaoOptional.isEmpty()){
            throw new RuntimeException("Não existe nenhuma transação com esse ID!");
        }
        Transacao transacao = transacaoOptional.get();
        transacao.setValor(transacaoDTO.valor());
        transacao.setDescricao(transacaoDTO.descricao());
        transacao.setData(transacaoDTO.data());
        transacao.setTipo(transacaoDTO.tipo());
        transacao.setCategoria(transacaoDTO.categoria());
        transacao = transacaoRepository.save(transacao);

        return new TransacaoResponseDTO(transacao.getId(), transacao.getValor(), transacao.getDescricao(),
                transacao.getData(), transacao.getTipo(), transacao.getCategoria());
    }

    @Transactional()
    public List<TransacaoResponseDTO> listarTransacoesUsuario(Long id){
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
        if (usuarioOptional.isEmpty()){
            throw new RuntimeException("Usuário não cadastrado!");
        }
        List<Transacao> listaTransacao = transacaoRepository.findByUsuarioId(id);
        return listaTransacao.stream().map(t -> new TransacaoResponseDTO(t.getId(), t.getValor(),
                t.getDescricao(), t.getData(), t.getTipo(), t.getCategoria())).toList();
    }

    @Transactional
    public List<TransacaoResponseDTO> filtrarPorCategoria(Long id, String categoriaString){
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
        if (usuarioOptional.isEmpty()){
            throw new RuntimeException("Usuário não encontrado");
        }

        Usuario usuario = usuarioOptional.get();
        Categoria categoria = Categoria.valueOf(categoriaString);

        return usuario.getTransacoes().stream()
                .filter(t -> t.getCategoria() == categoria)
                .map(t -> new TransacaoResponseDTO(t.getId(), t.getValor(), t.getDescricao(),
                        t.getData(), t.getTipo(), t.getCategoria())).toList();
    }

    @Transactional
    public Map<String, ResumoMensalDTO> resumoMensal(Long id){
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
        if (usuarioOptional.isEmpty()){
            throw new RuntimeException("Usuário não encontrado");
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
                                    t.getDescricao(), t.getData(), t.getTipo(), t.getCategoria())).toList();

                            return new ResumoMensalDTO(receita, despesa, listaDTOS);
                        })));
    }
}
