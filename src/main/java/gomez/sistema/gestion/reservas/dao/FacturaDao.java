package gomez.sistema.gestion.reservas.dao;

import gomez.sistema.gestion.reservas.entities.Cita;
import gomez.sistema.gestion.reservas.entities.Factura;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FacturaDao extends GenericDaoImpl<Factura> {

    private final CitasDao citasDao;

    public FacturaDao(Connection connection, CitasDao citasDao) {
        super(connection);
        this.citasDao = citasDao;
    }

    @Override
    public void insertar(Factura factura) {
        String sql = "INSERT INTO gerardo_factura (idCita, fechaEmision, monto) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, factura.getCita().getIdCita()); // Asegúrate que Cita tiene getId()
            stmt.setDate(2, factura.getFechaEmision());
            stmt.setDouble(3, factura.getMonto());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // Mejor usar un logger
        }
    }

    @Override
    public void actualizar(Factura factura) {
        String sql = "UPDATE gerardo_factura SET idCita = ?, fechaEmision = ?, monto = ? WHERE idFactura = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, factura.getCita().getIdCita());
            stmt.setDate(2, factura.getFechaEmision());
            stmt.setDouble(3, factura.getMonto());
            stmt.setInt(4, factura.getIdFactura());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void eliminar(int idFactura) {
        try {
            String sql = "DELETE FROM gerardo_factura WHERE idFactura = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, idFactura);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Factura buscar(Factura factura) {
        String sql = "SELECT * FROM gerardo_factura WHERE idFactura = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, factura.getIdFactura());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return construirFactura(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Factura> obtenerTodos() {
        List<Factura> lista = new ArrayList<>();
        String sql = "SELECT * FROM gerardo_factura";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(construirFactura(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    private Factura construirFactura(ResultSet rs) throws SQLException {
        int idFactura = rs.getInt("idFactura");
        int idCita = rs.getInt("idCita");
        Date fecha = rs.getDate("fechaEmision");
        double monto = rs.getDouble("monto");

        Cita citaCompleta = citasDao.obtenerTodos()
                .stream()
                .filter(c -> c.getIdCita() == idCita)
                .findFirst()
                .orElse(null);

        Factura factura = new Factura(citaCompleta, fecha, monto);
        factura.setIdFactura(idFactura);
        return factura;
    }

    public boolean existeFacturaParaCita(int idCita) {
        try {
            String sql = "SELECT COUNT(*) FROM gerardo_factura WHERE idCita = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, idCita);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
