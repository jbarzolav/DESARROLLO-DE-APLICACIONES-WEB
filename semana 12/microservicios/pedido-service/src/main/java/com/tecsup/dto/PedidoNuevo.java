package com.tecsup.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PedidoNuevo(
        @NotNull @Positive Long clienteId,
        @NotNull @Positive Long productoId,
        @NotNull @Positive Integer cantidad) {}
