package gomez.sistema.gestion.reservas.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Date;

@Data
@AllArgsConstructor
public class Factura {

    private Integer idFactura;
    private double monto;
    private Date fechaEmision;
    private Cita cita;
    private static int ultimoId;

    public Factura() {
        this.idFactura =++ ultimoId;
    }

    public Factura(Cita cita, Date fechaEmision, double monto) {
        this();
        this.cita = cita;
        this.fechaEmision = fechaEmision;
        this.monto = monto;
    }


}
