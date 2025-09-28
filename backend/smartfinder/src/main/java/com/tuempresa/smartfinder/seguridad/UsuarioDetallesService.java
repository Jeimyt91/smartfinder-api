package com.tuempresa.smartfinder.seguridad;

import com.tuempresa.smartfinder.dominio.Usuario;
import com.tuempresa.smartfinder.repositorio.UsuarioRepo;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import java.util.Optional; 

@Service
public class UsuarioDetallesService implements UserDetailsService {

    private final UsuarioRepo repo;

    public UsuarioDetallesService(UsuarioRepo repo) {
        this.repo = repo;
    }

    @Override
public UserDetails loadUserByUsername(String user) throws UsernameNotFoundException {
    Optional<Usuario> opt = user.contains("@")
            ? repo.findByCorreoIgnoreCase(user)
            : repo.findByNombreIgnoreCase(user);

    Usuario u = opt.orElseThrow(() ->
            new UsernameNotFoundException("No encontrado"));

    return org.springframework.security.core.userdetails.User
            .withUsername(u.getNombre() != null ? u.getNombre() : u.getCorreo())
            .password(u.getPassword())
            .roles(u.getRol().name())
            .build();
}
}
