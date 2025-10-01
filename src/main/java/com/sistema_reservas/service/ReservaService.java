package com.sistema_reservas.service;

import com.sistema_reservas.controller.dto.ReservaDTO;
import com.sistema_reservas.controller.dto.ReservaResponseDTO;
import com.sistema_reservas.dao.reservaDAO;
import com.sistema_reservas.mapper.reservaMapper;
import com.sistema_reservas.model.Espacio;
import com.sistema_reservas.model.Reserva;
import com.sistema_reservas.model.Usuario;
import com.sistema_reservas.repository.ReservaRepository;
import com.sistema_reservas.repository.UserRepository;
import com.sistema_reservas.repository.EspacioRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final UserRepository userRepository;
    private final EspacioRepository espacioRepository;
    private final reservaDAO reservaDAO;

    public ReservaService(ReservaRepository reservaRepository,
                          UserRepository userRepository,
                          EspacioRepository espacioRepository,
                          reservaDAO reservaDAO) {
        this.reservaRepository = reservaRepository;
        this.userRepository = userRepository;
        this.espacioRepository = espacioRepository;
        this.reservaDAO = reservaDAO;
    }

    public ReservaResponseDTO crearReserva(ReservaDTO dto) {
        // Verificar usuario
        Optional<Usuario> usuarioOpt = userRepository.findById(dto.getIdUsuario());
        if (usuarioOpt.isEmpty()) {
            return new ReservaResponseDTO(null, null, null,
                    null, null, null,
                    "El usuario no existe");
        }

        // Verificar espacio
        Optional<Espacio> espacioOpt = espacioRepository.findById(dto.getIdEspacio());
        if (espacioOpt.isEmpty()) {
            return new ReservaResponseDTO(null, null, null,
                    null, null, null,
                    "El espacio no existe");
        }

        // Convertir DTO a entidad
        Reserva reserva = reservaMapper.toEntity(dto, usuarioOpt.get(), espacioOpt.get());

        // Guardar reserva
        reserva = reservaRepository.save(reserva);

        return reservaMapper.toResponseDTO(reserva);
    }

    public ReservaResponseDTO actualizarEstado(Long idReserva, String nuevoEstado) {
        Optional<Reserva> opt = reservaRepository.findById(idReserva);

        if (opt.isEmpty()) {
            return new ReservaResponseDTO(null, null, null,
                    null, null, null,
                    "La reserva no existe");
        }

        Reserva reserva = opt.get();
        reserva.setEstado(nuevoEstado);
        reservaRepository.save(reserva);

        return reservaMapper.toResponseDTO(reserva);
    }

    public String eliminarReserva(Long idReserva) {
        Optional<Reserva> opt = reservaRepository.findById(idReserva);
        if (opt.isEmpty()) {
            return "La reserva no existe";
        }
        reservaRepository.deleteById(idReserva);
        return "Reserva eliminada correctamente";
    }
}
