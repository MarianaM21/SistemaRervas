package com.sistema_reservas.service;

import com.sistema_reservas.dao.pagoDAO;
import com.sistema_reservas.model.Pago;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PagoServiceimpl implements PagoService {

    @Autowired
    private pagoDAO pagoDAO;

    @Override
    public Pago guardarPago(Pago pago) { return pagoDAO.guardarPago(pago); }

    @Override
    public List<Pago> listarPagos() { return pagoDAO.listarPagos(); }

    @Override
    public Pago obtenerPagoPorId(Long id) { return pagoDAO.obtenerPagoPorId(id); }

    @Override
    public Pago actualizarPago(Pago pago) {return pagoDAO.actualizar(pago);}

    @Override
    public void eliminarPago(Long id) { pagoDAO.eliminarPago(id); }
}
