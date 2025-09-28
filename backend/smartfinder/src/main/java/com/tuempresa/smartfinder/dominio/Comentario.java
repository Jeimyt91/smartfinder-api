package com.tuempresa.smartfinder.dominio;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;


@Entity
@Table(name="comentarios")
public class Comentario {
@Id @GeneratedValue
private UUID id;


@ManyToOne(optional=false)
@JoinColumn(name="device_id")
private Dispositivo dispositivo;


@Column(nullable=false)
private String author;


@Column(nullable=false)
private Integer rating; // 1..5


@Column(nullable=false, columnDefinition="text")
private String text;


@Column(nullable=false)
private OffsetDateTime createdAt = OffsetDateTime.now();


// getters/setters
public UUID getId() { return id; }
public void setId(UUID id) { this.id = id; }
public Dispositivo getDispositivo() { return dispositivo; }
public void setDispositivo(Dispositivo dispositivo) { this.dispositivo = dispositivo; }
public String getAuthor() { return author; }
public void setAuthor(String author) { this.author = author; }
public Integer getRating() { return rating; }
public void setRating(Integer rating) { this.rating = rating; }
public String getText() { return text; }
public void setText(String text) { this.text = text; }
public OffsetDateTime getCreatedAt() { return createdAt; }
public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
}