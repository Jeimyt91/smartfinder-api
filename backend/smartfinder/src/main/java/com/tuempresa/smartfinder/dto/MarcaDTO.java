package com.tuempresa.smartfinder.dto;

import java.util.UUID;

public class MarcaDTO {
    private UUID id;
    private String nombre;
    private String descripcion;
    private String pais;
    private Integer anioFundacion;
    private String sitioWeb;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }

    public Integer getAnioFundacion() { return anioFundacion; }
    public void setAnioFundacion(Integer anioFundacion) { this.anioFundacion = anioFundacion; }

    public String getSitioWeb() { return sitioWeb; }
    public void setSitioWeb(String sitioWeb) { this.sitioWeb = sitioWeb; }
}
public MarcaDTO obtenerPorId(UUID id) {
    return repo.findById(id)
        .map(MarcaMapper::toDTO)
        .orElseThrow(() -> new EntityNotFoundException("Marca no encontrada"));
}