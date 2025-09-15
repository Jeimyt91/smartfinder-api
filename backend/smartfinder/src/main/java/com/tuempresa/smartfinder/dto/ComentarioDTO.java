package com.tuempresa.smartfinder.dto;
import java.time.OffsetDateTime;
import java.util.UUID;


public class ComentarioDTO {
public UUID id; public UUID deviceId; public String author; public Integer rating; public String text; public OffsetDateTime createdAt;
}