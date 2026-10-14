package com.tecsup.service;

import com.tecsup.model.Categoria;
import com.tecsup.repository.CategoriaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CategoriaServiceTest {

    @Mock
    private CategoriaRepository repo;

    @InjectMocks
    private CategoriaService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListarCategorias() {
        List<Categoria> lista = List.of(
                new Categoria(1L, "Tecnología", "Productos tecnológicos")
        );

        when(repo.findAll()).thenReturn(lista);

        List<Categoria> resultado = service.listar();

        assertEquals(1, resultado.size());
        assertEquals("Tecnología", resultado.get(0).getNombre());
        verify(repo, times(1)).findAll();
    }

    @Test
    void testGuardarCategoria() {
        Categoria categoria = new Categoria(
                1L, "Accesorios", "Accesorios de computadora"
        );

        when(repo.save(categoria)).thenReturn(categoria);

        Categoria resultado = service.guardar(categoria);

        assertNotNull(resultado);
        assertEquals("Accesorios", resultado.getNombre());
        verify(repo).save(categoria);
    }

    @Test
    void testObtenerCategoria() {
        Categoria categoria = new Categoria(
                1L, "Hogar", "Productos para el hogar"
        );

        when(repo.findById(1L)).thenReturn(Optional.of(categoria));

        Categoria resultado = service.obtener(1L);

        assertNotNull(resultado);
        assertEquals("Hogar", resultado.getNombre());
        verify(repo).findById(1L);
    }

    @Test
    void testObtenerCategoriaNoExiste() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        Categoria resultado = service.obtener(99L);

        assertNull(resultado);
        verify(repo).findById(99L);
    }

    @Test
    void testEliminarCategoria() {
        doNothing().when(repo).deleteById(1L);

        service.eliminar(1L);

        verify(repo, times(1)).deleteById(1L);
    }
}
