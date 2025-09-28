package com.tuempresa.smartfinder.repositorio;

import com.tuempresa.smartfinder.dominio.Marca;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MarcaRepo extends JpaRepository<Marca, UUID> {}
