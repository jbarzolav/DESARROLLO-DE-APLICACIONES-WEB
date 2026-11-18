package com.tecsup.services;
import com.tecsup.model.*;
import com.tecsup.repository.*;
import com.tecsup.exception.*;
import java.util.*;
@org.springframework.stereotype.Service public class PedidoService {
    private final PedidoRepository repo;
    public PedidoService(PedidoRepository r){
        repo=r;
    }
    public List<Pedido> listar(){
        return repo.findAll();
    }
    public Pedido obtener(Long id){
        return repo.findById(id).orElseThrow(()->new ResourceNotFoundException("Pedido no encontrado con id: "+id));
    }
    public Pedido guardar(Pedido e){
        if(e.getDetalles()!=null)e.getDetalles().forEach(d->d.setPedido(e));
        return repo.save(e);
    }
    public Pedido actualizar(Long id,Pedido e){
        obtener(id);
        e.setIdPedido(id);
        return guardar(e);
    }
    public void eliminar(Long id){
        repo.delete(obtener(id));
    }
}
