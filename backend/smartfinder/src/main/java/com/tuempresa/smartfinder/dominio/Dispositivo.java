package com.tuempresa.smartfinder.dominio;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "dispositivos")
public class Dispositivo {

    @Id @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(optional = false)
    @JoinColumn(name = "brand_id")
    private Marca marca;

    @Column(nullable = false)
    private String type; // móvil | portátil | tablet | wearable

    private LocalDate releaseDate;
    private BigDecimal price;

    @Column(length = 300)
    private String shortDesc;

    @Column(columnDefinition = "text")
    private String review;

    @OneToMany(mappedBy = "dispositivo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImagenDispositivo> imagenes = new ArrayList<>();

    @OneToMany(mappedBy = "dispositivo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EspecificacionDispositivo> especificaciones = new ArrayList<>();

    // ===== Getters / Setters =====
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Marca getMarca() { return marca; }
    public void setMarca(Marca marca) { this.marca = marca; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public LocalDate getReleaseDate() { return releaseDate; }
    public void setReleaseDate(LocalDate releaseDate) { this.releaseDate = releaseDate; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public String getShortDesc() { return shortDesc; }
    public void setShortDesc(String shortDesc) { this.shortDesc = shortDesc; }

    public String getReview() { return review; }
    public void setReview(String review) { this.review = review; }

    public List<ImagenDispositivo> getImagenes() { return imagenes; }
    public void setImagenes(List<ImagenDispositivo> imagenes) { this.imagenes = imagenes; }

    public List<EspecificacionDispositivo> getEspecificaciones() { return especificaciones; }
    public void setEspecificaciones(List<EspecificacionDispositivo> especificaciones) { this.especificaciones = especificaciones; }
}
