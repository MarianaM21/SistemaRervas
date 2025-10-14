package com.sistema_reservas.service;

import com.sistema_reservas.model.Reserva;
import java.util.List;

public interface ReservaService {
    Reserva guardarReserva(Reserva reserva);
    List<Reserva> obtenerTodasLasReservas();
    Reserva obtenerReservaPorId(Long id);
    boolean eliminarReserva(Long id);
}
