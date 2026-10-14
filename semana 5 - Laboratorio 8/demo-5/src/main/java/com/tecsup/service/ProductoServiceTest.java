package com.tecsup.service;

import com.tecsup.model.Producto;
import com.tecsup.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductoServiceTest {

    @Mock
    private ProductoRepository repo;

    @InjectMocks
    private ProductoService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListarProductos() {
        List<Producto> lista = List.of(
                new Producto(1L, "Monitor", 500, 10, "Tecnología")
        );

        when(repo.findAll()).thenReturn(lista);

        List<Producto> resultado = service.listar();

        assertEquals(1, resultado.size());
        assertEquals("Monitor", resultado.get(0).getNombre());
        verify(repo, times(1)).findAll();
    }

    @Test
    void testGuardarProducto() {
        Producto producto = new Producto(1L, "Laptop", 3000, 5, "Tecnología");

        when(repo.save(producto)).thenReturn(producto);

        Producto resultado = service.guardar(producto);

        assertNotNull(resultado);
        assertEquals("Laptop", resultado.getNombre());
        verify(repo).save(producto);
    }

    @Test
    void testObtenerProducto() {
        Producto producto = new Producto(1L, "Teclado", 100, 20, "Accesorios");

        when(repo.findById(1L)).thenReturn(Optional.of(producto));

        Producto resultado = service.obtener(1L);

        assertNotNull(resultado);
        assertEquals("Teclado", resultado.getNombre());
        verify(repo).findById(1L);
    }

    @Test
    void testEliminarProducto() {
        doNothing().when(repo).deleteById(1L);

        service.eliminar(1L);

        verify(repo, times(1)).deleteById(1L);
    }

    @Test
    void testActualizarProducto() {
        Producto existente = new Producto(
                1L, "Laptop", 3000, 5, "Tecnología"
        );

        existente.setNombre("Laptop actualizada");
        existente.setPrecio(3500);
        existente.setStock(8);
        existente.setCategoria("Computación");

        when(repo.save(existente)).thenReturn(existente);

        Producto resultado = service.guardar(existente);

        assertNotNull(resultado);
        assertEquals("Laptop actualizada", resultado.getNombre());
        assertEquals(3500, resultado.getPrecio());
        assertEquals(8, resultado.getStock());
        assertEquals("Computación", resultado.getCategoria());

        verify(repo, times(1)).save(existente);
    }

    @Test
    void testObtenerProductoNoExiste() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        Producto resultado = service.obtener(99L);

        assertNull(resultado);
        verify(repo, times(1)).findById(99L);
    }
}
