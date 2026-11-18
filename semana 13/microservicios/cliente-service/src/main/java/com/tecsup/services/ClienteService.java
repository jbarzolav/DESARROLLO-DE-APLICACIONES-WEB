package com.tecsup.services;
import com.tecsup.model.*;
import com.tecsup.repository.*;
import com.tecsup.exception.*;
import java.util.*;
@org.springframework.stereotype.Service public class ClienteService {
    private final ClienteRepository repo;
    public ClienteService(ClienteRepository r){
        repo=r;
    }
    public List<Cliente> listar(){
        return repo.findAll();
    }
    public Cliente obtener(Long id){
        return repo.findById(id).orElseThrow(()->new ResourceNotFoundException("Cliente no encontrado con id: "+id));
    }
    public Cliente guardar(Cliente e){
        return repo.save(e);
    }
    public Cliente actualizar(Long id,Cliente e){
        obtener(id);
        e.setId(id);
        return guardar(e);
    }
    public void eliminar(Long id){
        repo.delete(obtener(id));
    }
}
