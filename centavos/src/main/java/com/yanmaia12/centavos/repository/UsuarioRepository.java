package com.yanmaia12.centavos.repository;

import com.yanmaia12.centavos.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
    Boolean existsByEmail(String email);
    Optional<Usuario> findByEmail(String email);
}
