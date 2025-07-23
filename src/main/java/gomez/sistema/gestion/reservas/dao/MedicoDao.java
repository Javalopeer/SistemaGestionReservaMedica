package gomez.sistema.gestion.reservas.dao;

import gomez.sistema.gestion.reservas.entities.Especialidad;
import gomez.sistema.gestion.reservas.entities.Medico;

import java.sql.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class MedicoDao extends GenericDaoImpl<Medico> {

    public MedicoDao(Connection connection) {
        super(connection);
    }

    @Override
    public void actualizar(Medico medico) {
        try {
            String sql = "UPDATE gerardo_medico SET nombre = ? especialidad = ? WHERE id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, medico.getNombre());
            ps.setString(2, medico.getEspecialidad().name());
            ps.setInt(3, medico.getId());
            ps.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void actualizarHorario(Medico medico) {
        try {
            String sql = "UPDATE gerardo_medico SET horarioEntrada = ?, horarioSalida = ? WHERE id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, medico.getHorarioInicio().toString());
            ps.setString(2, medico.getHorarioFin().toString());
            ps.setInt(3, medico.getId());
            ps.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public List<String> buscar() {
        List<String> nombres = new ArrayList<>();
        String sql = "SELECT nombre, apellido FROM gerardo_medico";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String nombreCompleto = rs.getString("nombre") + " " + rs.getString("apellido");
                nombres.add(nombreCompleto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nombres;
    }


    public void eliminar(Medico medico) {
        String sql = "DELETE FROM gerardo_medico WHERE id = ?";
        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1, medico.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insertar(Medico medico) {
        try {
            String sql = "INSERT INTO gerardo_medico(nombre, especialidad, horarioEntrada, horarioSalida, telefono) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, medico.getNombre());
            ps.setString(2, medico.getEspecialidad().name());
            ps.setString(3, medico.getHorarioInicio().toString());
            ps.setString(4, medico.getHorarioFin().toString());
            ps.setInt(5, medico.getTelefono());
            ps.execute();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Medico> obtenerTodos() {
        List<Medico> medicos = new ArrayList<>();
        try {
            String sql = "SELECT * FROM gerardo_medico";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Medico medico = new Medico();
                medico.setId(rs.getInt("id"));
                medico.setNombre(rs.getString("nombre"));
                medico.setEspecialidad(Especialidad.valueOf(rs.getString("especialidad")));
                medico.setHorarioInicio(LocalTime.parse(rs.getString("horarioEntrada")));
                medico.setHorarioFin(LocalTime.parse(rs.getString("horarioSalida")));
                medico.setTelefono(rs.getInt("telefono"));
                medicos.add(medico);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return medicos;
    }
}
