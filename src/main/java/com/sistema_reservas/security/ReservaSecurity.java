package com.sistema_reservas.security;

import com.sistema_reservas.model.Reserva;
import com.sistema_reservas.service.ReservaServiceimpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("reservaSecurity")
@RequiredArgsConstructor
public class ReservaSecurity {

    private final ReservaServiceimpl reservaService;

    public boolean esPropietario(Long reservaId, String emailUsuario) {
        Reserva reserva = reservaService.obtenerReservaPorId(reservaId);
        if (reserva == null) return false;
        return reserva.getUsuario().getEmail().equals(emailUsuario);
    }
}
