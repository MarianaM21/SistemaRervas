package com.sistema_reservas.service;

import com.sistema_reservas.model.Pago;
import java.util.List;

public interface PagoService {
    Pago guardarPago(Pago pago);
    List<Pago> listarPagos();
    Pago obtenerPagoPorId(Long id);
    void eliminarPago(Long id);
    Pago actualizarPago(Pago pago);
}




