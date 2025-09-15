package com.tuempresa.smartfinder.dominio;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "especificaciones_dispositivo")
public class EspecificacionDispositivo {
    @Id @GeneratedValue
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "device_id")
    private Dispositivo dispositivo;

    @Column(nullable = false)
    private String clave;

    @Column(nullable = false)
    private String valor;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public Dispositivo getDispositivo() { return dispositivo; }
    public void setDispositivo(Dispositivo dispositivo) { this.dispositivo = dispositivo; }

    public String getClave() { return clave; }
    public void setClave(String clave) { this.clave = clave; }

    public String getValor() { return valor; }
    public void setValor(String valor) { this.valor = valor; }
}
