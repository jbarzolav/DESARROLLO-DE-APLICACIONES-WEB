package com.tecsup.model;
import jakarta.persistence.*;
import java.time.*;
@Entity
public class Pedido{
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long idPedido;
    private LocalDate fecha;
    private Long idCliente;
    private Double total;
    @OneToMany(mappedBy="pedido",cascade=CascadeType.ALL,orphanRemoval=true) private java.util.List<DetallePedido> detalles;
    public Long getIdPedido(){
        return idPedido;
    }
    public void setIdPedido(Long v){
        idPedido=v;
    }
    public LocalDate getFecha(){
        return fecha;
    }
    public void setFecha(LocalDate v){
        fecha=v;
    }
    public Long getIdCliente(){
        return idCliente;
    }
    public void setIdCliente(Long v){
        idCliente=v;
    }
    public Double getTotal(){
        return total;
    }
    public void setTotal(Double v){
        total=v;
    }
    public java.util.List<DetallePedido> getDetalles(){
        return detalles;
    }
    public void setDetalles(java.util.List<DetallePedido> v){
        detalles=v;
    }
}
