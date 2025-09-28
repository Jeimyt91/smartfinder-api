package com.tuempresa.smartfinder.servicio;

import com.tuempresa.smartfinder.dominio.Dispositivo;
import com.tuempresa.smartfinder.dto.DispositivoDTO;
import com.tuempresa.smartfinder.repositorio.DispositivoRepositorio;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
public class DispositivoServicio {

  private final DispositivoRepositorio repo;

  public DispositivoServicio(DispositivoRepositorio repo) {
    this.repo = repo;
  }

  public Page<Dispositivo> listar(int page, int size) {
    return repo.findAll(PageRequest.of(page, size, Sort.by("id").descending()));
  }

  public Dispositivo obtener(Long id) {
    return repo.findById(id)
      .orElseThrow(() -> new IllegalArgumentException("No existe dispositivo id=" + id));
  }

  public Dispositivo crear(DispositivoDTO dto) {
    Dispositivo d = new Dispositivo();
    d.setNombre(dto.nombre());
    d.setMarca(dto.marca());
    d.setTipo(dto.tipo());
    d.setPrecio(dto.precio());
    d.setDisponible(dto.disponible());
    return repo.save(d);
  }

  public Dispositivo actualizar(Long id, DispositivoDTO dto) {
    Dispositivo d = obtener(id);
    d.setNombre(dto.nombre());
    d.setMarca(dto.marca());
    d.setTipo(dto.tipo());
    d.setPrecio(dto.precio());
    d.setDisponible(dto.disponible());
    return repo.save(d);
  }

  public void eliminar(Long id) {
    if (!repo.existsById(id)) {
      throw new IllegalArgumentException("No existe dispositivo id=" + id);
    }
    repo.deleteById(id);
  }
}
