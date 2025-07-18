package gomez.sistema.gestion.reservas.repository;

import gomez.sistema.gestion.reservas.entities.Cita;
import gomez.sistema.gestion.reservas.entities.Factura;

public interface FacturaRepositorio extends RepositorioGeneral<Factura, Integer> {
    Factura asignarFactura(Cita cita);
}
