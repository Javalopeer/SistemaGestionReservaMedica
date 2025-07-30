package gomez.sistema.gestion.reservas.controllers;

import gomez.sistema.gestion.reservas.entities.Cita;
import gomez.sistema.gestion.reservas.entities.EspecialidadConMonto;
import gomez.sistema.gestion.reservas.entities.Factura;
import gomez.sistema.gestion.reservas.repository.FacturaRepositorio;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class FacturaController implements FacturaRepositorio{

    private final List<Factura> facturas;

    public FacturaController() {
        facturas = new ArrayList<Factura>();
    }

    @Override
    public Factura asignarFactura(Cita cita) {
        double monto = EspecialidadConMonto.obtenerMontoPorNombre(
                cita.getMedico().getEspecialidad().name()
        );

        Factura nuevaFactura = new Factura(
                cita,
                new Date(System.currentTimeMillis()),
                monto
        );

        agregar(nuevaFactura);
        return nuevaFactura;
    }

    @Override
    public List<Factura> listar() {
        return facturas;
    }

    @Override
    public Factura buscar(Integer id) {
        return facturas.stream()
                .filter(f -> f.getIdFactura().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void agregar(Factura factura) {
        facturas.add(factura);
    }

    @Override
    public void actualizar(Factura factura) {
        eliminar(factura.getIdFactura());
        agregar(factura);
    }

    @Override
    public void eliminar(Integer id) {
        if (id != null) {
            facturas.removeIf(f -> id.equals(f.getIdFactura()));
    }
}}
