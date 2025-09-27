package com.sistema_reservas.service;

import com.sistema_reservas.model.Pago;
import com.sistema_reservas.model.Reserva;
import com.sistema_reservas.repository.PagoRepository;
import com.sistema_reservas.repository.ReservaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PagoService {

    private final PagoRepository pagoRepository;
    private final ReservaRepository reservaRepository;

    public PagoService(PagoRepository pagoRepository, ReservaRepository reservaRepository) {
        this.pagoRepository = pagoRepository;
        this.reservaRepository = reservaRepository;
    }

    public List<Pago> listarPagos() {
        return pagoRepository.findAll();
    }

    public Optional<Pago> buscarPorId(Long id) {
        return pagoRepository.findById(id);
    }

    public List<Pago> listarPorReserva(Long reservaId) {
        return pagoRepository.findByReservaId(reservaId);
    }

    public List<Pago> listarPorEstado(String estado) {
        return pagoRepository.findByEstado(estado);
    }

    @Transactional
    public Pago registrarPago(Long reservaId, Double monto, String metodo) {
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        Pago pago = new Pago();
        pago.setReserva(reserva);
        pago.setMonto(monto);
        pago.setMetodo(metodo);
        pago.setFechaPago(LocalDateTime.now());
        pago.setEstado("PENDIENTE");

        return pagoRepository.save(pago);
    }

    @Transactional
    public Pago confirmarPago(Long pagoId) {
        Pago pago = pagoRepository.findById(pagoId)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));
        pago.setEstado("CONFIRMADO");
        return pagoRepository.save(pago);
    }

    @Transactional
    public Pago marcarPagoFallido(Long pagoId) {
        Pago pago = pagoRepository.findById(pagoId)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));
        pago.setEstado("FALLIDO");
        return pagoRepository.save(pago);
    }

    public void eliminarPago(Long id) {
        pagoRepository.deleteById(id);
    }
}
