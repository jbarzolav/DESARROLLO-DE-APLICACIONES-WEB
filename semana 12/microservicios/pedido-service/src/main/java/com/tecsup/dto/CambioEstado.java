package com.tecsup.dto;

import com.tecsup.model.EstadoPedido;
import jakarta.validation.constraints.NotNull;

public record CambioEstado(@NotNull EstadoPedido estado) {}
