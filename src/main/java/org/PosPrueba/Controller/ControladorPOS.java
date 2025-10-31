package org.PosPrueba.Controller;

import org.PosPrueba.Controller.Command.Comando;
import org.PosPrueba.Controller.Command.ComandoAgregar;
import org.PosPrueba.Controller.Command.InvocadorComandos;
import org.PosPrueba.Model.Service.ServicioProducto;

public class ControladorPOS {

    private final InvocadorComandos invocador;
    private final ServicioProducto servicioProducto;

    public ControladorPOS(InvocadorComandos invocador, ServicioProducto servicioProducto) {
        this.invocador = invocador;
        this.servicioProducto = servicioProducto;
    }

    public void onAgregar(Long productoId, int cantidad) throws Exception {
        Comando cmd = new ComandoAgregar(servicioProducto, productoId, cantidad);
        invocador.ejecutarComando(cmd);
    }

    public void onDeshacer() throws Exception {
        invocador.deshacer();
    }

    // onCobrar(...) se implementará más adelante
}
