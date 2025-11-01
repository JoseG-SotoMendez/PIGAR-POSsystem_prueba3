package org.PosPrueba.Controller;

import org.PosPrueba.Controller.Command.Comando;
import org.PosPrueba.Controller.Command.ComandoAgregar;
import org.PosPrueba.Controller.Command.InvocadorComandos;
import org.PosPrueba.Model.ItemCarrito;
import org.PosPrueba.Model.Service.ServicioProducto;
import org.PosPrueba.Model.Service.ServicioVenta;

import java.util.List;

public class ControladorPOS {

    private final InvocadorComandos invocador;
    private final ServicioProducto servicioProducto;
    private final ServicioVenta servicioVenta;

    public ControladorPOS(InvocadorComandos invocador, ServicioProducto servicioProducto, ServicioVenta servicioVenta) {
        this.invocador = invocador;
        this.servicioProducto = servicioProducto;
        this.servicioVenta = servicioVenta;
    }

    public void onAgregar(Long productoId, int cantidad) throws Exception {
        Comando cmd = new ComandoAgregar(servicioProducto, productoId, cantidad);
        invocador.ejecutarComando(cmd);
    }

    public void onDeshacer() throws Exception {
        invocador.deshacer();
    }

    /**
     * Ejecuta el cobro: delega a ServicioVenta
     * items: lista de ItemCarrito desde UI
     * clienteId puede ser null
     * devuelve id de la venta creada
     */
    public Long onCobrar(List<ItemCarrito> items, Long clienteId) throws Exception {
        return servicioVenta.crearVentaDesdeCarrito(items, clienteId);
    }
}
