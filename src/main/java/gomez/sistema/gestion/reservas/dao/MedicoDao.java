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
            String sql = "UPDATE gerardo_medico SET nombre = ?, apellido = ?, especialidad = ?, telefono = ?, horarioEntrada = ?, horarioSalida =? WHERE id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, medico.getNombre());
            ps.setString(2, medico.getApellido());
            ps.setString(3, medico.getEspecialidad().name());
            ps.setString(4, medico.getTelefono());
            ps.setString(5, medico.getHorarioInicio().toString());
            ps.setString(6, medico.getHorarioFin().toString());
            ps.setInt(7, medico.getId());
            ps.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public Medico buscarPorId(int id) {
        try {
            String sql = "SELECT * FROM gerardo_medico WHERE id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                String especialidad = rs.getString("especialidad");
                return new Medico(id, nombre, apellido, Especialidad.valueOf(especialidad));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
            String sql = "INSERT INTO gerardo_medico(nombre, apellido, especialidad, horarioEntrada, horarioSalida, telefono) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, medico.getNombre());
            ps.setString(2, medico.getApellido());
            ps.setString(3, medico.getEspecialidad().name());
            ps.setString(4, medico.getHorarioInicio().toString());
            ps.setString(5, medico.getHorarioFin().toString());
            ps.setString(6, medico.getTelefono());
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
                medico.setApellido(rs.getString("apellido"));
                medico.setEspecialidad(Especialidad.valueOf(rs.getString("especialidad")));
                medico.setHorarioInicio(LocalTime.parse(rs.getString("horarioEntrada")));
                medico.setHorarioFin(LocalTime.parse(rs.getString("horarioSalida")));
                medico.setTelefono(rs.getString("telefono"));
                medicos.add(medico);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return medicos;
    }

    public List<Medico> obtenerPorEspecialidad(Especialidad especialidad) {
        List<Medico> lista = new ArrayList<>();
        try {
            String sql = "SELECT * FROM gerardo_medico WHERE especialidad = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, especialidad.name());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Medico m = new Medico();
                m.setId(rs.getInt("id"));
                m.setNombre(rs.getString("nombre"));
                m.setApellido(rs.getString("apellido"));
                m.setEspecialidad(Especialidad.valueOf(rs.getString("especialidad")));
                m.setHorarioInicio(rs.getTime("horarioEntrada").toLocalTime());
                m.setHorarioFin(rs.getTime("horarioSalida").toLocalTime());
                m.setTelefono(rs.getString("telefono"));
                lista.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
