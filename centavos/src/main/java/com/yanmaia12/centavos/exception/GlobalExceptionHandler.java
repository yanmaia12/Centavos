package com.yanmaia12.centavos.exception;

import com.yanmaia12.centavos.dtos.ErroResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErroResponseDTO> handleNotFound(ResourceNotFoundException e){
        return ResponseEntity.status(404).body(new ErroResponseDTO(e.getMessage()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErroResponseDTO> handleForbidden(AccessDeniedException e){
        return ResponseEntity.status(403).body(new ErroResponseDTO(e.getMessage()));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErroResponseDTO> handleBusiness(BusinessException e){
        return ResponseEntity.status(400).body(new ErroResponseDTO(e.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErroResponseDTO> handleIllegalArgument(IllegalArgumentException e){
        return ResponseEntity.status(400).body(new ErroResponseDTO(e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponseDTO> handleGeneric(Exception e){
        return ResponseEntity.status(500).body(new ErroResponseDTO(e.getMessage()));
    }
}
