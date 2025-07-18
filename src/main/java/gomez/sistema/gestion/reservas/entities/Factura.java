package gomez.sistema.gestion.reservas.entities;

import java.sql.Date;

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

    public Cita getCita() {
        return cita;
    }

    public void setCita(Cita cita) {
        this.cita = cita;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public Integer getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(Integer idFactura) {
        this.idFactura = idFactura;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

}
