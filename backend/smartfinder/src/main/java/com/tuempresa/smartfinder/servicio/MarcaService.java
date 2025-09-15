package com.tuempresa.smartfinder.servicio;


import com.tuempresa.smartfinder.dominio.*;
import com.tuempresa.smartfinder.dto.*;
import com.tuempresa.smartfinder.repositorio.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.*; import java.util.stream.*; import java.util.UUID;


@Service
public class MarcaService {
private final MarcaRepo marcaRepo;
public MarcaService(MarcaRepo marcaRepo){ this.marcaRepo = marcaRepo; }


public List<MarcaDTO> listar(){
return marcaRepo.findAll().stream().map(m->{
MarcaDTO dto=new MarcaDTO(); dto.id=m.getId(); dto.name=m.getName(); return dto;}).toList();
}


public MarcaDTO crear(String nombre){
Marca m = marcaRepo.findByNameIgnoreCase(nombre).orElseGet(()->{ Marca x=new Marca(); x.setName(nombre); return marcaRepo.save(x); });
MarcaDTO dto = new MarcaDTO(); dto.id=m.getId(); dto.name=m.getName(); return dto;
}


public Marca get(UUID id){ return marcaRepo.findById(id).orElseThrow(); }
}