package com.tuempresa.smartfinder.servicio;

import com.tuempresa.smartfinder.dominio.Dispositivo;
import com.tuempresa.smartfinder.dominio.EspecificacionDispositivo;
import com.tuempresa.smartfinder.dominio.ImagenDispositivo;
import com.tuempresa.smartfinder.dto.DispositivoCrearActualizarDTO;
import com.tuempresa.smartfinder.dto.DispositivoDTO;
import com.tuempresa.smartfinder.repositorio.DispositivoRepo;
import com.tuempresa.smartfinder.repositorio.MarcaRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// ===== IMPORTS QUE FALTABAN =====
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
// =================================


public class DispositivoService {

  private final DispositivoRepo dispRepo;
  private final MarcaRepo marcaRepo;

  public DispositivoService(DispositivoRepo d, MarcaRepo m) { this.dispRepo = d; this.marcaRepo = m; }

  public List<DispositivoDTO> listar(String q, UUID brandId, String type, String sort) {
    List<Dispositivo> all = dispRepo.findAll();
    Stream<Dispositivo> st = all.stream();

    if (q != null && !q.trim().isEmpty()) {
      final String t = q.toLowerCase();
      st = st.filter(d ->
        (d.getName() + " " + d.getMarca().getName() + " " +
         Optional.ofNullable(d.getShortDesc()).orElse(""))
        .toLowerCase()
        .contains(t)
      );
    }
    if (brandId != null) {
      st = st.filter(d -> d.getMarca().getId().equals(brandId));
    }
    if (type != null && !type.trim().isEmpty()) {
      st = st.filter(d -> type.equalsIgnoreCase(d.getType()));
    }

    Comparator<Dispositivo> cmp =
      Comparator.comparing(Dispositivo::getReleaseDate,
        Comparator.nullsLast(Comparator.naturalOrder()))
        .reversed();

    if ("release_asc".equals(sort)) {
      cmp = Comparator.comparing(Dispositivo::getReleaseDate,
              Comparator.nullsLast(Comparator.naturalOrder()));
    }
    if ("price_asc".equals(sort)) {
      cmp = Comparator.comparing(d ->
        Optional.ofNullable(d.getPrice()).orElse(java.math.BigDecimal.ZERO));
    }
    if ("price_desc".equals(sort)) {
      cmp = Comparator.<Dispositivo, java.math.BigDecimal>comparing(d ->
        Optional.ofNullable(d.getPrice()).orElse(java.math.BigDecimal.ZERO)).reversed();
    }

    return st.sorted(cmp)
             .map(this::toDTO)
             .collect(Collectors.toList());
  }

  public DispositivoDTO obtener(UUID id) { return toDTO(dispRepo.findById(id).orElseThrow()); }

  @Transactional
  public DispositivoDTO crear(DispositivoCrearActualizarDTO in) {
    Dispositivo d = new Dispositivo();
    aplicar(d, in);
    return toDTO(dispRepo.save(d));
  }

  @Transactional
  public DispositivoDTO actualizar(UUID id, DispositivoCrearActualizarDTO in) {
    Dispositivo d = dispRepo.findById(id).orElseThrow();
    d.getImagenes().clear();
    d.getEspecificaciones().clear();
    aplicar(d, in);
    return toDTO(dispRepo.save(d));
  }

  public void eliminar(UUID id) { dispRepo.deleteById(id); }

  private void aplicar(Dispositivo d, DispositivoCrearActualizarDTO in) {
    d.setName(in.name);
    d.setType(in.type);
    d.setReleaseDate(in.releaseDate);
    d.setPrice(in.price);
    d.setShortDesc(in.shortDesc);
    d.setReview(in.review);
    d.setMarca(marcaRepo.findById(in.brandId).orElseThrow());

    int i = 0;
    for (String url : in.images) {
      if (url == null || url.trim().isEmpty()) continue;
      ImagenDispositivo im = new ImagenDispositivo();
      im.setDispositivo(d);
      im.setUrl(url);
      im.setSortIndex(i++);
      d.getImagenes().add(im);
    }
    for (Map.Entry<String,String> e : in.specs.entrySet()) {
      String k = e.getKey(); String v = e.getValue();
      if (k != null && !k.trim().isEmpty() && v != null && !v.trim().isEmpty()) {
        EspecificacionDispositivo s = new EspecificacionDispositivo();
        s.setDispositivo(d); s.setClave(k); s.setValor(v);
        d.getEspecificaciones().add(s);
      }
    }
  }

  private DispositivoDTO toDTO(Dispositivo d) {
    DispositivoDTO dto = new DispositivoDTO();
    dto.id = d.getId();
    dto.name = d.getName();
    dto.brandId = d.getMarca().getId();
    dto.brandName = d.getMarca().getName();
    dto.type = d.getType();
    dto.releaseDate = d.getReleaseDate();
    dto.price = d.getPrice();
    dto.shortDesc = d.getShortDesc();
    dto.review = d.getReview();

    dto.images = d.getImagenes().stream()
      .sorted(Comparator.comparing(i -> Optional.ofNullable(i.getSortIndex()).orElse(0)))
      .map(ImagenDispositivo::getUrl)
      .collect(Collectors.toList());

    dto.specs = d.getEspecificaciones().stream()
      .collect(Collectors.toMap(
        EspecificacionDispositivo::getClave,
        EspecificacionDispositivo::getValor,
        (a,b) -> a,
        LinkedHashMap::new
      ));
    return dto;
  }
}
