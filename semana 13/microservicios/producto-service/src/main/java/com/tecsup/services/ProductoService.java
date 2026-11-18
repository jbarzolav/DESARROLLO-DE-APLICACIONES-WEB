package com.tecsup.services;
import com.tecsup.model.*;
import com.tecsup.repository.*;
import com.tecsup.exception.*;
import java.util.*;
@org.springframework.stereotype.Service public class ProductoService {
    private final ProductoRepository repo;
    public ProductoService(ProductoRepository r){
        repo=r;
    }
    public List<Producto> listar(){
        return repo.findAll();
    }
    public Producto obtener(Long id){
        return repo.findById(id).orElseThrow(()->new ResourceNotFoundException("Producto no encontrado con id: "+id));
    }
    public Producto guardar(Producto e){
        return repo.save(e);
    }
    public Producto actualizar(Long id,Producto e){
        obtener(id);
        e.setId(id);
        return guardar(e);
    }
    public void eliminar(Long id){
        repo.delete(obtener(id));
    }
}
