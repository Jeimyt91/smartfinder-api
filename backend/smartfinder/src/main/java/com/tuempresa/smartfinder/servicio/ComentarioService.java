package com.tuempresa.smartfinder.servicio;

import com.tuempresa.smartfinder.dominio.Comentario;
import com.tuempresa.smartfinder.dominio.Dispositivo;
import com.tuempresa.smartfinder.dto.ComentarioDTO;
import com.tuempresa.smartfinder.repositorio.ComentarioRepo;
import com.tuempresa.smartfinder.repositorio.DispositivoRepositorio;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComentarioService {

  private final ComentarioRepo repo;
  private final DispositivoRepositorio dRepo;

  public ComentarioService(ComentarioRepo repo, DispositivoRepositorio dRepo) {
    this.repo = repo;
    this.dRepo = dRepo;
  }

  public List<ComentarioDTO> listar(Long deviceId) {
    return repo.findByDispositivoIdOrderByCreatedAtDesc(deviceId)
               .stream()
               .map(this::toDTO)
               .toList();
  }

  public ComentarioDTO crear(Long deviceId, String author, Integer rating, String text) {
    Dispositivo d = dRepo.findById(deviceId)
        .orElseThrow(() -> new IllegalArgumentException("No existe dispositivo id=" + deviceId));

    Comentario c = new Comentario();
    c.setDispositivo(d);
    c.setAuthor((author == null || author.isBlank()) ? "Anónimo" : author.trim());
    c.setRating(rating);
    c.setText(text);
    // si createdAt no es automático, setéalo aquí:
    // c.setCreatedAt(OffsetDateTime.now());

    return toDTO(repo.save(c));
  }

  private ComentarioDTO toDTO(Comentario c) {
    return new ComentarioDTO(
        c.getId(),
        c.getDispositivo().getId(),
        c.getAuthor(),
        c.getRating(),
        c.getText(),
        c.getCreatedAt()
    );
  }
}
