package com.tuempresa.smartfinder.servicio;

import com.tuempresa.smartfinder.dominio.Marca;
import com.tuempresa.smartfinder.dto.MarcaDTO;
import com.tuempresa.smartfinder.dto.MarcaMapper;
import com.tuempresa.smartfinder.repositorio.MarcaRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MarcaService {

    private final MarcaRepo repo;

    public MarcaService(MarcaRepo repo){ this.repo = repo; }

    public List<MarcaDTO> listar(){
        return repo.findAll()
                .stream()
                .map(MarcaMapper::toDTO)
                .collect(Collectors.toList());
    }

    public MarcaDTO crear(MarcaDTO dto){
        if (dto.getNombre() == null || dto.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        Marca saved = repo.save(MarcaMapper.toEntity(dto));
        return MarcaMapper.toDTO(saved);
    }
}
