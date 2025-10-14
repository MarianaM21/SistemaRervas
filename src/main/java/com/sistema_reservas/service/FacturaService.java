package com.sistema_reservas.service;

import com.sistema_reservas.model.Factura;
import java.util.List;

public interface FacturaService {
    Factura guardarFactura(Factura factura);
    Factura obtenerPorId(Long id);
    List<Factura> listarFacturas();
    void eliminarFactura(Long id);
}
