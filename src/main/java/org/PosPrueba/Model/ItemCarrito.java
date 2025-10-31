package org.PosPrueba.Model;

import javafx.beans.property.*;
import java.math.BigDecimal;

/**
 * Representa un item en el carrito de compras.
 * Usa JavaFX Properties para binding automático con la UI.
 */
public class ItemCarrito {
    private final LongProperty productoId;
    private final StringProperty nombre;
    private final IntegerProperty cantidad;
    private final ObjectProperty<BigDecimal> precioUnitario;
    private final ObjectProperty<BigDecimal> subtotal;

    public ItemCarrito(Long productoId, String nombre, int cantidad, BigDecimal precioUnitario) {
        this.productoId = new SimpleLongProperty(productoId);
        this.nombre = new SimpleStringProperty(nombre);
        this.cantidad = new SimpleIntegerProperty(cantidad);
        this.precioUnitario = new SimpleObjectProperty<>(precioUnitario);
        this.subtotal = new SimpleObjectProperty<>(precioUnitario.multiply(BigDecimal.valueOf(cantidad)));

        // Listener para actualizar subtotal cuando cambia la cantidad
        this.cantidad.addListener((obs, oldVal, newVal) -> {
            calcularSubtotal();
        });
    }

    private void calcularSubtotal() {
        BigDecimal precio = precioUnitario.get();
        int cant = cantidad.get();
        subtotal.set(precio.multiply(BigDecimal.valueOf(cant)));
    }

    // Getters para properties
    public LongProperty productoIdProperty() {
        return productoId;
    }

    public StringProperty nombreProperty() {
        return nombre;
    }

    public IntegerProperty cantidadProperty() {
        return cantidad;
    }

    public ObjectProperty<BigDecimal> precioUnitarioProperty() {
        return precioUnitario;
    }

    public ObjectProperty<BigDecimal> subtotalProperty() {
        return subtotal;
    }

    // Getters normales
    public Long getProductoId() {
        return productoId.get();
    }

    public String getNombre() {
        return nombre.get();
    }

    public int getCantidad() {
        return cantidad.get();
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario.get();
    }

    public BigDecimal getSubtotal() {
        return subtotal.get();
    }

    // Setters
    public void setCantidad(int cantidad) {
        this.cantidad.set(cantidad);
    }

    public void incrementarCantidad() {
        this.cantidad.set(this.cantidad.get() + 1);
    }

    public void decrementarCantidad() {
        if (this.cantidad.get() > 1) {
            this.cantidad.set(this.cantidad.get() - 1);
        }
    }
}
