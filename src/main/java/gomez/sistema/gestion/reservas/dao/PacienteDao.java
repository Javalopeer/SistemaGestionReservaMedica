package gomez.sistema.gestion.reservas.dao;

import gomez.sistema.gestion.reservas.entities.Paciente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PacienteDao extends GenericDaoImpl<Paciente> {

    public PacienteDao(Connection connection) {
        super(connection);
    }

    public void actualizar(Paciente paciente, String cedulaOriginal) {
        try {
            String sql = "UPDATE gerardo_paciente SET cedula = ?, nombre = ?, apellido = ?, telefono = ? WHERE cedula = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, paciente.getCedula());
            ps.setString(2, paciente.getNombre());
            ps.setString(3, paciente.getApellido());
            ps.setString(4, paciente.getTelefono());
            ps.setString(5, cedulaOriginal); // Solo usamos la cédula original
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> buscar() {
        List<String> nombres = new ArrayList<>();
        String sql = "SELECT nombre, apellido FROM gerardo_paciente";
        try (PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){
            while (rs.next()) {
                String nombreCompleto = rs.getString("nombre") + " " + rs.getString("apellido");
                nombres.add(nombreCompleto);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return nombres;
    }

    public Paciente buscarPorId(int id) {
        try {
            String sql = "SELECT * FROM gerardo_paciente WHERE cedula = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String cedula = rs.getString("cedula");
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                String telefono = rs.getString("telefono");
                return new Paciente(cedula, nombre, apellido, telefono);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void eliminar(Paciente paciente) {
        try {
            String sql = "DELETE FROM gerardo_paciente WHERE cedula = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, paciente.getCedula());
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
            ps.setString(1, paciente.getCedula());
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
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Paciente paciente = new Paciente();
                paciente.setCedula(rs.getString("cedula"));
                paciente.setNombre(rs.getString("nombre"));
                paciente.setApellido(rs.getString("apellido"));
                paciente.setTelefono(rs.getString("telefono"));
                pacientes.add(paciente);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return pacientes;
    }
}
