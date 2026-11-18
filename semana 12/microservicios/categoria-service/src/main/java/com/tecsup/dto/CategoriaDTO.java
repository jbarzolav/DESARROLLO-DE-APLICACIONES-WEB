package com.tecsup.dto;
import jakarta.validation.constraints.NotBlank;
public class CategoriaDTO {
    @NotBlank(message="El nombre es obligatorio") private String nombre;
    @NotBlank(message="El estado es obligatorio") private String estado;
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
