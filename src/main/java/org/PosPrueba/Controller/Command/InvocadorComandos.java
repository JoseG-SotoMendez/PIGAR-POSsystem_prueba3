package org.PosPrueba.Controller.Command;

import java.util.ArrayDeque;
import java.util.Deque;

public class InvocadorComandos {

    private final Deque<Comando> historial = new ArrayDeque<>();

    public void ejecutarComando(Comando comando) throws Exception {
        comando.ejecutar();
        historial.push(comando);
    }

    public void deshacer() throws Exception {
        if (!historial.isEmpty()) {
            Comando cmd = historial.pop();
            cmd.deshacer();
        }
    }
}
