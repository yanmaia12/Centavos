package com.yanmaia12.centavos.repository;

import com.yanmaia12.centavos.enums.Categoria;
import com.yanmaia12.centavos.model.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransacaoRepository extends JpaRepository<Transacao, Long>{
    List<Transacao> findByUsuarioId(Long id);
    List<Transacao> findByUsuarioIdAndCategoria(Long id, Categoria categoria);
}
