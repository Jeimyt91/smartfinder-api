package com.tuempresa.smartfinder.dto;

import com.tuempresa.smartfinder.dominio.Marca;

public class MarcaMapper {

    public static MarcaDTO toDTO(Marca entity) {
        MarcaDTO dto = new MarcaDTO();
        dto.setId(entity.getId());
        dto.setNombre(entity.getNombre());
        dto.setDescripcion(entity.getDescripcion());
        dto.setPais(entity.getPais());
        dto.setAnioFundacion(entity.getAnioFundacion());
        dto.setSitioWeb(entity.getSitioWeb());
        return dto;
    }

    public static Marca toEntity(MarcaDTO dto) {
        Marca entity = new Marca();
        entity.setNombre(dto.getNombre());
        entity.setDescripcion(dto.getDescripcion());
        entity.setPais(dto.getPais());
        entity.setAnioFundacion(dto.getAnioFundacion());
        entity.setSitioWeb(dto.getSitioWeb());
        return entity;
    }
}
