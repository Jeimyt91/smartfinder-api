package com.tuempresa.smartfinder.repositorio;
import com.tuempresa.smartfinder.dominio.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;


public interface EspecificacionRepo extends JpaRepository<EspecificacionDispositivo, UUID> { }