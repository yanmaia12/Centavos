package com.yanmaia12.centavos.repository;

import com.yanmaia12.centavos.model.Meta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MetaRepository extends JpaRepository<Meta, Long>{
    List<Meta>  findByUsuarioId(Long id);
}
