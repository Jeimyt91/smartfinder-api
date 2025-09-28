package com.tuempresa.smartfinder.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public class ComentarioDTO {
    private UUID id;
    private Long deviceId;
    private String author;
    private Integer rating;
    private String text;
    private OffsetDateTime createdAt;

    public ComentarioDTO(UUID id, Long deviceId, String author, Integer rating, String text, OffsetDateTime createdAt) {
        this.id = id;
        this.deviceId = deviceId;
        this.author = author;
        this.rating = rating;
        this.text = text;
        this.createdAt = createdAt;
    }

    public UUID getId() { return id; }
    public Long getDeviceId() { return deviceId; }
    public String getAuthor() { return author; }
    public Integer getRating() { return rating; }
    public String getText() { return text; }
    public OffsetDateTime getCreatedAt() { return createdAt; }

    public void setId(UUID id) { this.id = id; }
    public void setDeviceId(Long deviceId) { this.deviceId = deviceId; }
    public void setAuthor(String author) { this.author = author; }
    public void setRating(Integer rating) { this.rating = rating; }
    public void setText(String text) { this.text = text; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
}

