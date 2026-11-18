package com.tecsup.dto;

import com.tecsup.model.EstadoPedido;

public record PedidoDetalle(
        Long id,
        int cantidad,
        EstadoPedido estado,
        ClienteDTO cliente,
        ProductoDTO producto) {}
