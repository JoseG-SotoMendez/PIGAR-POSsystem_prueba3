package org.PosPrueba.Controller.Command;

public interface Comando {
    void ejecutar() throws Exception;
    void deshacer() throws Exception;
}
