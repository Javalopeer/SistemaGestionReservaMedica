package gomez.sistema.gestion.reservas.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import gomez.sistema.gestion.reservas.entities.Cita;
import gomez.sistema.gestion.reservas.error.AlertFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class PdfExporterCita {

    public static void exportarCitas(List<Cita> citas, Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF (*.pdf)", "*.pdf"));
        fileChooser.setInitialFileName("lista_citas.pdf");

        File archivoSeleccionado = fileChooser.showSaveDialog(stage);

        if (archivoSeleccionado != null) {
            try {
                Document document = new Document(PageSize.A4.rotate()); // Horizontal
                PdfWriter.getInstance(document, new FileOutputStream(archivoSeleccionado));
                document.open();

                Font tituloFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
                Paragraph titulo = new Paragraph("Lista de Citas Médicas", tituloFont);
                titulo.setAlignment(Element.ALIGN_CENTER);

                document.add(titulo);
                document.add(new Paragraph(" ")); // Espacio

                PdfPTable table = new PdfPTable(6);
                table.setWidthPercentage(100);
                table.setWidths(new float[]{2, 4, 4, 3, 3, 3});

                // Encabezados
                table.addCell("ID");
                table.addCell("Médico");
                table.addCell("Paciente");
                table.addCell("Especialidad");
                table.addCell("Fecha");
                table.addCell("Hora");

                for (Cita cita : citas) {
                    table.addCell(String.valueOf(cita.getIdCita()));
                    table.addCell(cita.getMedico().getNombre() + " " + cita.getMedico().getApellido());
                    table.addCell(cita.getPaciente().getNombre() + " " + cita.getPaciente().getApellido());
                    table.addCell(cita.getMedico().getEspecialidad().name());
                    table.addCell(cita.getFecha().toString());
                    table.addCell(cita.getHora().toString());
                }

                document.add(table);
                document.close();

                AlertFactory.mostrarInfo("PDF generado", "El archivo se guardó correctamente.");
            } catch (Exception e) {
                e.printStackTrace();
                AlertFactory.mostrarError("Error al exportar el PDF.");
            }
        }
    }
}
