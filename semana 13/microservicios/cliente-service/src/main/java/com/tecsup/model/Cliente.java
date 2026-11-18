package com.tecsup.model;
import jakarta.persistence.*;
import java.time.*;
@Entity
public class Cliente{
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @jakarta.validation.constraints.NotBlank private String nombre;
    @jakarta.validation.constraints.NotBlank private String apellidos;
    private String direccion;
    private String telefono;
    private String nroDni;
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
    public String getApellidos(){
        return apellidos;
    }
    public void setApellidos(String v){
        apellidos=v;
    }
    public String getDireccion(){
        return direccion;
    }
    public void setDireccion(String v){
        direccion=v;
    }
    public String getTelefono(){
        return telefono;
    }
    public void setTelefono(String v){
        telefono=v;
    }
    public String getNroDni(){
        return nroDni;
    }
    public void setNroDni(String v){
        nroDni=v;
    }
    public String getEstado(){
        return estado;
    }
    public void setEstado(String v){
        estado=v;
    }
}
