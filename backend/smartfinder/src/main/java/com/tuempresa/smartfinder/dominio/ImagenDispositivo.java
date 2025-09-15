package com.tuempresa.smartfinder.dominio;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "imagenes_dispositivo")
public class ImagenDispositivo {
    @Id @GeneratedValue
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "device_id")
    private Dispositivo dispositivo;

    @Column(nullable = false, columnDefinition = "text")
    private String url;

    @Column(name = "sort_index")
    private Integer sortIndex;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public Dispositivo getDispositivo() { return dispositivo; }
    public void setDispositivo(Dispositivo dispositivo) { this.dispositivo = dispositivo; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public Integer getSortIndex() { return sortIndex; }
    public void setSortIndex(Integer sortIndex) { this.sortIndex = sortIndex; }
}
