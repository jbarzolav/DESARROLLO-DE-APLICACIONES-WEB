package com.tecsup.services;

import com.tecsup.client.ClienteClient;
import com.tecsup.client.ProductoClient;
import com.tecsup.dto.ClienteDTO;
import com.tecsup.dto.PedidoDetalle;
import com.tecsup.dto.PedidoNuevo;
import com.tecsup.dto.ProductoDTO;
import com.tecsup.exception.ResourceNotFoundException;
import com.tecsup.model.EstadoPedido;
import com.tecsup.model.Pedido;
import com.tecsup.repository.PedidoRepository;
import feign.FeignException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PedidoService {
    private final PedidoRepository repo;
    private final ClienteClient clientes;
    private final ProductoClient productos;

    public PedidoService(PedidoRepository repo, ClienteClient clientes, ProductoClient productos) {
        this.repo = repo;
        this.clientes = clientes;
        this.productos = productos;
    }

    public List<Pedido> listar() {
        return repo.findAll();
    }

    public Pedido guardar(PedidoNuevo nuevo) {
        buscarCliente(nuevo.clienteId());
        buscarProducto(nuevo.productoId());
        return repo.save(new Pedido(nuevo.clienteId(), nuevo.productoId(), nuevo.cantidad()));
    }

    public PedidoDetalle obtener(Long id) {
        Pedido pedido = buscarPedido(id);
        return new PedidoDetalle(pedido.getId(), pedido.getCantidad(), pedido.getEstado(),
                buscarCliente(pedido.getClienteId()), buscarProducto(pedido.getProductoId()));
    }

    @Transactional
    public Pedido cambiarEstado(Long id, EstadoPedido estado) {
        Pedido pedido = buscarPedido(id);
        pedido.cambiarEstado(estado);
        return pedido;
    }

    private Pedido buscarPedido(Long id) {
        return repo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Pedido no encontrado con id: " + id));
    }

    private ClienteDTO buscarCliente(Long id) {
        try {
            return clientes.obtener(id);
        } catch (FeignException.NotFound error) {
            throw new ResourceNotFoundException("Cliente no encontrado con id: " + id);
        }
    }

    private ProductoDTO buscarProducto(Long id) {
        try {
            return productos.obtener(id);
        } catch (FeignException.NotFound error) {
            throw new ResourceNotFoundException("Producto no disponible con id: " + id);
        }
    }
}
