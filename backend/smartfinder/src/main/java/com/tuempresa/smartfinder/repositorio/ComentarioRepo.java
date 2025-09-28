package com.tuempresa.smartfinder.repositorio;
import com.tuempresa.smartfinder.dominio.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;


public interface ComentarioRepo extends JpaRepository<Comentario, UUID> {
  List<Comentario> findByDispositivoIdOrderByCreatedAtDesc(Long dispositivoId);
}