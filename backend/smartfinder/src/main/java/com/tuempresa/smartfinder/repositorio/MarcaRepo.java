package com.tuempresa.smartfinder.repositorio;
import com.tuempresa.smartfinder.dominio.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
import java.util.UUID;


public interface MarcaRepo extends JpaRepository<Marca, UUID> {
Optional<Marca> findByNameIgnoreCase(String name);
}