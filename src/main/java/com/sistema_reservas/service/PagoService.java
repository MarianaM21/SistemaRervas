package com.sistema_reservas.service;

import com.sistema_reservas.controller.dto.PagoDTO;
import com.sistema_reservas.controller.dto.PagoResponseDTO;
import com.sistema_reservas.dao.pagoDAO;
import com.sistema_reservas.mapper.pagoMapper;
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
    private final pagoDAO pagoDAO;

    public PagoService(PagoRepository pagoRepository, ReservaRepository reservaRepository, pagoDAO pagoDAO) {
        this.pagoRepository = pagoRepository;
        this.reservaRepository = reservaRepository;
        this.pagoDAO = pagoDAO;
    }

    public List<Pago> listarPagos() {
        return pagoRepository.findAll();
    }

    public Optional<Pago> buscarPorId(Long id) {
        return pagoRepository.findById(id);
    }

    @Transactional
    public PagoResponseDTO registrarPago(PagoDTO dto) {
        Reserva reserva = reservaRepository.findById(dto.getReservaId())
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        Pago pago = new Pago();
        pago.setReserva(reserva);
        pago.setMonto(dto.getMonto());
        pago.setMetodo(dto.getMetodo());
        pago.setFechaPago(LocalDateTime.now());
        pago.setEstado("PENDIENTE");

        pagoRepository.save(pago);

        return pagoMapper.toResponseDTO(pago, "Pago registrado con estado PENDIENTE");
    }

    @Transactional
    public PagoResponseDTO confirmarPago(Long pagoId) {
        Pago pago = pagoRepository.findById(pagoId)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));

        pago.setEstado("CONFIRMADO");
        pagoRepository.save(pago);

        return pagoMapper.toResponseDTO(pago, "Pago confirmado correctamente");
    }

    @Transactional
    public PagoResponseDTO marcarPagoFallido(Long pagoId) {
        Pago pago = pagoRepository.findById(pagoId)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));

        pago.setEstado("FALLIDO");
        pagoRepository.save(pago);

        return pagoMapper.toResponseDTO(pago, "Pago marcado como fallido");
    }

    @Transactional
    public void eliminarPago(Long id) {
        pagoDAO.eliminar(id);
    }

    public List<Pago> listarPagosPendientesPorReserva(Long reservaId) {
        return pagoDAO.obtenerPagosPendientesPorReserva(reservaId);
    }

    public List<Pago> listarPagosConfirmadosPorReserva(Long reservaId) {
        return pagoDAO.obtenerPagosConfirmadosPorReserva(reservaId);
    }

    public List<Pago> listarPagosPorUsuario(Long usuarioId) {
        return pagoDAO.obtenerPagosPorUsuario(usuarioId);
    }
}
