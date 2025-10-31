/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package org.PosPrueba.Model.Service;

import java.util.List;
import org.PosPrueba.Model.Producto;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author acer
 */
public class ServicioProductoTest {
    
    public ServicioProductoTest() {
    }

    @Test
    public void testListarProductos() throws Exception {
        System.out.println("listarProductos");
        ServicioProducto instance = null;
        List<Producto> expResult = null;
        List<Producto> result = instance.listarProductos();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testObtenerProducto() throws Exception {
        System.out.println("obtenerProducto");
        Long id = null;
        ServicioProducto instance = null;
        Producto expResult = null;
        Producto result = instance.obtenerProducto(id);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testActualizarStock() throws Exception {
        System.out.println("actualizarStock");
        Long id = null;
        int delta = 0;
        ServicioProducto instance = null;
        instance.actualizarStock(id, delta);
        fail("The test case is a prototype.");
    }
    
}
