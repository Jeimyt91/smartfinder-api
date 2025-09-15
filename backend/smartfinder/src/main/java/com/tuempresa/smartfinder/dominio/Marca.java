package com.tuempresa.smartfinder.dominio;


import jakarta.persistence.*;
import java.util.*;
import java.util.UUID;


@Entity
@Table(name = "marcas")
public class Marca {
@Id @GeneratedValue
private UUID id;


@Column(nullable=false, unique=true)
private String name;


// getters/setters
public UUID getId() { return id; }
public void setId(UUID id) { this.id = id; }
public String getName() { return name; }
public void setName(String name) { this.name = name; }
}   