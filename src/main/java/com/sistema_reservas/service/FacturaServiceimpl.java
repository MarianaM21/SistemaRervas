package com.sistema_reservas.service;

import com.sistema_reservas.dao.facturaDAO;
import com.sistema_reservas.model.Factura;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FacturaServiceimpl implements FacturaService {

    @Autowired
    private facturaDAO facturaDAO;

    @Override
    public Factura guardarFactura(Factura factura) {
        return facturaDAO.guardarFactura(factura);
    }

    @Override
    public Factura obtenerPorId(Long id) {
        return facturaDAO.obtenerPorId(id);
    }

    @Override
    public List<Factura> listarFacturas() {
        return facturaDAO.listarFacturas();
    }

    @Override
    public void eliminarFactura(Long id) {
        facturaDAO.eliminarFactura(id);
    }

    public Factura obtenerPorPagoId(Long pagoId) {
        return facturaDAO.obtenerPorPagoId(pagoId);
    }


}
