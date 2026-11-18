package com.tecsup.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

public record ClienteDTO(Long id, String nombre, @JsonAlias("apellidos") String apellido) {}
