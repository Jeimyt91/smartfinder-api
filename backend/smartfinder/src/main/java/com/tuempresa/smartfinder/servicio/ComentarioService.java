package com.tuempresa.smartfinder.servicio;


import com.tuempresa.smartfinder.dominio.*;
import com.tuempresa.smartfinder.dto.*;
import com.tuempresa.smartfinder.repositorio.*;
import org.springframework.stereotype.Service;
import java.util.*; import java.util.stream.*; import java.util.UUID;


@Service
public class ComentarioService {
private final ComentarioRepo repo; private final DispositivoRepo dispRepo;
public ComentarioService(ComentarioRepo r, DispositivoRepo d){ this.repo=r; this.dispRepo=d; }


public List<ComentarioDTO> listar(UUID deviceId){
return repo.findByDispositivoIdOrderByCreatedAtDesc(deviceId).stream().map(this::toDTO).toList();
}


public ComentarioDTO crear(UUID deviceId, String author, Integer rating, String text){
Dispositivo d = dispRepo.findById(deviceId).orElseThrow();
Comentario c = new Comentario(); c.setDispositivo(d); c.setAuthor(author==null||author.isBlank()?"Anónimo":author.trim()); c.setRating(rating==null?5:rating); c.setText(text);
return toDTO(repo.save(c));
}


private ComentarioDTO toDTO(Comentario c){
ComentarioDTO dto = new ComentarioDTO(); dto.id=c.getId(); dto.deviceId=c.getDispositivo().getId(); dto.author=c.getAuthor(); dto.rating=c.getRating(); dto.text=c.getText(); dto.createdAt=c.getCreatedAt(); return dto;
}
}