package org.PosPrueba.Model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Venta {

    private Long id;
    private Long clienteId;
    private LocalDateTime fecha;
    private BigDecimal total;

    public Venta() {
        this.fecha = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
}
