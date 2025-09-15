package com.tuempresa.smartfinder.repositorio;
import com.tuempresa.smartfinder.dominio.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
import java.util.UUID;


public interface DispositivoRepo extends JpaRepository<Dispositivo, UUID> {
}