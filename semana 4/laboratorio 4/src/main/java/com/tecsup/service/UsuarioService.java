package com.tecsup.service;

import com.tecsup.models.Usuario;
import com.tecsup.models.Perfil;
import com.tecsup.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    @Transactional
    public Usuario guardar(Usuario usuario) {
        if (usuario.getPerfil() != null) {
            usuario.getPerfil().setUsuario(usuario);
        }
        return repository.save(usuario);
    }

    public List<Usuario> listar() {
        return repository.findAll();
    }

    public Usuario obtener(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Transactional
    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}
