package com.tecsup.model;
import jakarta.persistence.*;
import java.time.*;
@Entity
public class DetallePedido{
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long idDetalle;
    private Long idProducto;
    private Integer cantidad;
    private Double precio;
    @com.fasterxml.jackson.annotation.JsonIgnore @ManyToOne @JoinColumn(name="id_pedido") private Pedido pedido;
    public Long getIdDetalle(){
        return idDetalle;
    }
    public void setIdDetalle(Long v){
        idDetalle=v;
    }
    public Long getIdProducto(){
        return idProducto;
    }
    public void setIdProducto(Long v){
        idProducto=v;
    }
    public Integer getCantidad(){
        return cantidad;
    }
    public void setCantidad(Integer v){
        cantidad=v;
    }
    public Double getPrecio(){
        return precio;
    }
    public void setPrecio(Double v){
        precio=v;
    }
    public Pedido getPedido(){
        return pedido;
    }
    public void setPedido(Pedido v){
        pedido=v;
    }
}
