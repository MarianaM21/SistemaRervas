package com.sistema_reservas.service;

import com.sistema_reservas.dao.reservaDAO;
import com.sistema_reservas.model.Reserva;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservaServiceimpl {

    @Autowired
    private reservaDAO reservaDAO;

    public List<Reserva> listarReservas() {
        return reservaDAO.listarReservas();
    }

    public Reserva obtenerReservaPorId(Long id) {
        return reservaDAO.obtenerPorId(id);
    }

    public Reserva guardarReserva(Reserva reserva) {
        return reservaDAO.guardarReserva(reserva);
    }

    public boolean eliminarReserva(Long id) {
        Reserva reserva = reservaDAO.obtenerPorId(id);
        if (reserva != null) {
            reservaDAO.eliminarReserva(id);
            return true;
        }
        return false;
    }



    public List<Reserva> listarReservasPorEstadoYFecha(String estado, String fechaStr) {
        LocalDateTime fecha = fechaStr != null ? LocalDateTime.parse(fechaStr) : null;
        return reservaDAO.listarPorEstadoYFecha(estado, fecha);
    }

}
