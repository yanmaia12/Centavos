package com.yanmaia12.centavos.service;

import com.yanmaia12.centavos.repository.MetaRepository;
import com.yanmaia12.centavos.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class MetaService{

    private final MetaRepository metaRepository;
    private final UsuarioRepository usuarioRepository;

    public MetaService(MetaRepository metaRepository, UsuarioRepository usuarioRepository) {
        this.metaRepository = metaRepository;
        this.usuarioRepository = usuarioRepository;
    }


}
