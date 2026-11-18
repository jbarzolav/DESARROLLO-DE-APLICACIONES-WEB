package com.tecsup.model;
import jakarta.persistence.*;
import java.time.*;
@Entity
public class Producto{
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @jakarta.validation.constraints.NotBlank private String nombre;
    private Double precio;
    private Integer stock;
    private Long categoriaId;
    public Long getId(){
        return id;
    }
    public void setId(Long v){
        id=v;
    }
    public String getNombre(){
        return nombre;
    }
    public void setNombre(String v){
        nombre=v;
    }
    public Double getPrecio(){
        return precio;
    }
    public void setPrecio(Double v){
        precio=v;
    }
    public Integer getStock(){
        return stock;
    }
    public void setStock(Integer v){
        stock=v;
    }
    public Long getCategoriaId(){
        return categoriaId;
    }
    public void setCategoriaId(Long v){
        categoriaId=v;
    }
}
