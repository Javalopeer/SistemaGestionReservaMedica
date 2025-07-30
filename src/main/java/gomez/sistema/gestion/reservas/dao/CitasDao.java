package gomez.sistema.gestion.reservas.dao;

import gomez.sistema.gestion.reservas.entities.Cita;
import gomez.sistema.gestion.reservas.entities.Medico;
import gomez.sistema.gestion.reservas.entities.Paciente;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CitasDao extends GenericDaoImpl<Cita> {

    private final PacienteDao pacDao;
    private final MedicoDao medDao;

    public CitasDao(Connection connection, PacienteDao pacDao, MedicoDao medDao) {
        super(connection);
        this.pacDao = pacDao;
        this.medDao = medDao;
    }



    public int insertarCita(Cita cita) {
        int idGenerado = -1;
        String sql = "INSERT INTO gerardo_citas (fecha, hora, medico, paciente) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setDate(1, java.sql.Date.valueOf(cita.getFecha()));
            ps.setTime(2, java.sql.Time.valueOf(cita.getHora()));
            ps.setInt(3, cita.getMedico().getId());
            ps.setInt(4, Integer.parseInt(cita.getPaciente().getCedula()));

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                idGenerado = rs.getInt(1);
                cita.setIdCita(idGenerado); // Asignar el ID a la cita
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return idGenerado;
    }

    @Override
    public void actualizar(Cita cita) {
        String sql = "UPDATE gerardo_citas SET fecha = ?, hora = ?, medico = ?, paciente = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDate(1, java.sql.Date.valueOf(cita.getFecha()));
            ps.setTime(2, java.sql.Time.valueOf(cita.getHora()));
            ps.setInt(3, cita.getMedico().getId());     // ✅ ID del médico
            ps.setInt(4, Integer.parseInt(cita.getPaciente().getCedula()));   // ✅ ID del paciente
            ps.setInt(5, cita.getIdCita());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void eliminar(int idCita) {
        String sql = "DELETE FROM gerardo_citas WHERE id = ?";
        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1, idCita);
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
                        rs.getString("hora") + " - " + rs.getDate("fecha").toString();
                citas.add(citaBuscada);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return citas;
    }

    @Override
    public List<Cita> obtenerTodos() {
        List<Cita> citas = new ArrayList<>();
        try{
            String sql = "SELECT * FROM gerardo_citas";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                int idCita = rs.getInt("id");
                LocalDate fecha = rs.getDate("fecha").toLocalDate();
                LocalTime hora = rs.getTime("hora").toLocalTime();
                int idMedico = rs.getInt("medico");
                int idPaciente = rs.getInt("paciente");

                Paciente paciente = pacDao.buscarPorId(idPaciente);
                Medico medico = medDao.buscarPorId(idMedico);

                Cita cita = new Cita(idCita, fecha, hora, paciente, medico);
                citas.add(cita);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return citas;
    }

    public boolean existeCita(int idMedico, LocalDate fecha, LocalTime hora) {
        try {
            String sql = "SELECT COUNT(*) FROM gerardo_citas WHERE medico = ? AND fecha = ? AND hora = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, idMedico);
            ps.setDate(2, java.sql.Date.valueOf(fecha));
            ps.setTime(3, java.sql.Time.valueOf(hora));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
