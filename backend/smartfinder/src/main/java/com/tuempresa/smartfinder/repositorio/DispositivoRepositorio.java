package com.tuempresa.smartfinder.repositorio;

import com.tuempresa.smartfinder.dominio.Dispositivo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DispositivoRepositorio extends JpaRepository<Dispositivo, Long> {
    
}