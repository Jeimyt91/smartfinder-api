package com.tuempresa.smartfinder.repositorio;
import com.tuempresa.smartfinder.dominio.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*; import java.util.UUID;


public interface ComentarioRepo extends JpaRepository<Comentario, UUID> {
List<Comentario> findByDispositivoIdOrderByCreatedAtDesc(UUID deviceId);
}