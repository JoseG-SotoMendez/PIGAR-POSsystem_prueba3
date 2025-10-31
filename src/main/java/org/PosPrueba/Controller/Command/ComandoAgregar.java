package org.PosPrueba.Controller.Command;

import org.PosPrueba.Model.Service.ServicioProducto;

public class ComandoAgregar implements Comando {

    private final ServicioProducto servicioProducto;
    private final Long productoId;
    private final int cantidad;
    private boolean ejecutado;

    public ComandoAgregar(ServicioProducto servicioProducto, Long productoId, int cantidad) {
        this.servicioProducto = servicioProducto;
        this.productoId = productoId;
        this.cantidad = cantidad;
        this.ejecutado = false;
    }

    @Override
    public void ejecutar() throws Exception {
        servicioProducto.actualizarStock(productoId, -cantidad);
        ejecutado = true;
    }

    @Override
    public void deshacer() throws Exception {
        if (ejecutado) {
            servicioProducto.actualizarStock(productoId, cantidad);
            ejecutado = false;
        }
    }
}
