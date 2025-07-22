package gomez.sistema.gestion.reservas.dao;

import gomez.sistema.gestion.reservas.entities.Paciente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PacienteDao extends GenericDaoImpl<Paciente> {

    public PacienteDao(Connection connection) {
        super(connection);
    }

    @Override
    public void actualizar(Paciente paciente) {
        try {
            String sql = "UPDATE gerardo_paciente SET nombre = ?, apellido = ?, telefono = ? WHERE cedula = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, paciente.getNombre());
            ps.setString(2, paciente.getApellido());
            ps.setString(3, paciente.getTelefono());
            ps.setInt(4, paciente.getCedula());
            ps.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Paciente buscar(Paciente paciente) {
        Paciente pac = new Paciente();
        try {
            String sql = "SELECT * FROM gerardo_paciente WHERE nombre LIKE ? AND apellido LIKE ?";
            PreparedStatement ps = this.connection.prepareStatement(sql);
            ps.setString(1, "%" + paciente.getNombre() + "%");
            ps.setString(2, "%" + paciente.getApellido() + "%");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                pac.setNombre(rs.getString("nombre"));
                pac.setApellido(rs.getString("apellido"));
                pac.setTelefono(rs.getString("telefono"));
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return pac;
    }

    public void eliminar(Paciente paciente) {
        try {
            String sql = "DELETE FROM gerardo_paciente WHERE cedula = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, paciente.getCedula());
            ps.executeUpdate();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insertar(Paciente paciente) {
        try {
            String sql = "INSERT INTO gerardo_paciente(cedula, nombre, apellido, telefono) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setFloat(1, paciente.getCedula());
            ps.setString(2, paciente.getNombre());
            ps.setString(3, paciente.getApellido());
            ps.setString(4, paciente.getTelefono());
            ps.executeUpdate();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean existeCedula(Integer cedula) {
        String sql = "SELECT COUNT(*) FROM gerardo_paciente WHERE cedula = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1, cedula);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public List<Paciente> obtenerTodos() {
        List<Paciente> pacientes = new ArrayList<Paciente>();
        try {
            String sql = "SELECT * FROM gerardo_paciente";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                pacientes.add(new Paciente(
                        rs.getInt("cedula"),
                        rs.getString("apellido"),
                        rs.getString("nombre"),
                        rs.getString("telefono")
                ));
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return pacientes;
    }
}
