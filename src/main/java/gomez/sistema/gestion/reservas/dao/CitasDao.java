package gomez.sistema.gestion.reservas.dao;

import gomez.sistema.gestion.reservas.entities.Cita;
import gomez.sistema.gestion.reservas.error.AlertFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CitasDao extends GenericDaoImpl<Cita> {

    public CitasDao(Connection connection) {
        super(connection);
    }

    @Override
    public void insertar(Cita cita) {
        try{
            String sql = "INSERT INTO gerardo_citas (fecha, hora, medico, paciente) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, String.valueOf(cita.getHora()));
            ps.setString(2, String.valueOf(cita.getHora()));
            ps.setString(3, String.valueOf(cita.getMedico()));
            ps.setString(4, String.valueOf(cita.getPaciente()));
            ps.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void actualizar(Cita cita) {
        super.actualizar(cita);
    }

    public void eliminar(Cita cita) {
        String sql = "DELETE FROM gerardo_citas WHERE id = ?";
        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1, cita.getIdCita());
            ps.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public List<String> buscar(Date fecha) {
        List<String> citas = new ArrayList<>();
        String sql = "SELECT c.hora, c.fecha, m.nombre AS nombreMedico, p.nombre AS nombrePaciente " +
                "FROM gerardo_citas c " +
                "JOIN gerardo_medico m ON c.medico = m.id " +
                "JOIN gerardo_paciente p ON c.paciente = p.id " +
                "WHERE c.fecha = ?";
        try{
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(fecha.getTime()));
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                String citaBuscada = "Dr. " + rs.getString("nombreMedico") + " - " +
                        rs.getString("nombrePaciente") + " - " +
                        rs.getString("hora") + " - " + rs.getDate("fecha").toString();                     ;
                citas.add(citaBuscada);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return citas;
    }

    @Override
    public List<Cita> obtenerTodos() {
        return super.obtenerTodos();
    }
}
