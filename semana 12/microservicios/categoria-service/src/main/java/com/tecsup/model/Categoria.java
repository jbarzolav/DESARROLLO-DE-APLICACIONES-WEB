package com.tecsup.model;
import jakarta.persistence.*;
import java.time.*;
@Entity
public class Categoria{
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @jakarta.validation.constraints.NotBlank private String nombre;
    private String estado;
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
    public String getEstado(){
        return estado;
    }
    public void setEstado(String v){
        estado=v;
    }
}
