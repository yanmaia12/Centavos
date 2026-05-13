package com.yanmaia12.centavos.service;

import com.yanmaia12.centavos.dtos.*;
import com.yanmaia12.centavos.enums.Categoria;
import com.yanmaia12.centavos.enums.TipoTransacao;
import com.yanmaia12.centavos.model.Transacao;
import com.yanmaia12.centavos.model.Usuario;
import com.yanmaia12.centavos.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder encoder;

    public UsuarioService(UsuarioRepository usuarioRepository, BCryptPasswordEncoder encoder) {
        this.usuarioRepository = usuarioRepository;
        this.encoder = encoder;
    }

    @Transactional
    public UsuarioResponseDTO cadastrarUsuario(CadastroDTO cadastroDTO){
        boolean existeEmail = usuarioRepository.existsByEmail(cadastroDTO.email());
        if (existeEmail){
            throw new RuntimeException("Esse email já está cadastrado!");
        }
        if (!Objects.equals(cadastroDTO.senha(), cadastroDTO.confirmarSenha())){
            throw new RuntimeException("Senhas não são iguais!");
        }
        Usuario usuario = new Usuario();
        usuario.setNome(cadastroDTO.nome());
        usuario.setEmail(cadastroDTO.email());
        usuario.setSenha(encoder.encode(cadastroDTO.senha()));
        usuario.setMoeda(cadastroDTO.moeda());
        usuarioRepository.save(usuario);
        return new UsuarioResponseDTO(usuario.getNome(), usuario.getId(), usuario.getEmail(), usuario.getMoeda());
    }

    @Transactional
    public UsuarioResponseDTO atualizarMoeda(Long id, String moeda){
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
        if (usuarioOptional.isEmpty()){
            throw new RuntimeException("Usuário não encontrado");
        }
        Usuario usuario = usuarioOptional.get();
        usuario.setMoeda(moeda);
        usuario = usuarioRepository.save(usuario);
        return new UsuarioResponseDTO(usuario.getNome(), usuario.getId(), usuario.getEmail(), usuario.getMoeda());
    }

    @Transactional
    public BigDecimal calcularSaldo(Long id){
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
        if (usuarioOptional.isEmpty()){
            throw new RuntimeException("Usuário não encontrado");
        }

        Usuario usuario = usuarioOptional.get();

        BigDecimal receita = usuario.getTransacoes().stream()
                .filter(t -> t.getTipo() == TipoTransacao.RECEITA)
                .map(Transacao::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal despesa = usuario.getTransacoes().stream()
                .filter(t -> t.getTipo() == TipoTransacao.DESPESA)
                .map(Transacao::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return receita.subtract(despesa);

    }

}
