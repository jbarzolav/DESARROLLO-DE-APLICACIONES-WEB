package com.tecsup.services;
import com.tecsup.model.*;
import com.tecsup.repository.*;
import com.tecsup.exception.*;
import java.util.*;
@org.springframework.stereotype.Service public class CategoriaService {
    private final CategoriaRepository repo;
    public CategoriaService(CategoriaRepository r){
        repo=r;
    }
    public List<Categoria> listar(){
        return repo.findAll();
    }
    public Categoria obtener(Long id){
        return repo.findById(id).orElseThrow(()->new ResourceNotFoundException("Categoria no encontrado con id: "+id));
    }
    public Categoria guardar(Categoria e){
        return repo.save(e);
    }
    public Categoria actualizar(Long id,Categoria e){
        obtener(id);
        e.setId(id);
        return guardar(e);
    }
    public void eliminar(Long id){
        repo.delete(obtener(id));
    }
}
