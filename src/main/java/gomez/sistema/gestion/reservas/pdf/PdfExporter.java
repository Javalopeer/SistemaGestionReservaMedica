package gomez.sistema.gestion.reservas.pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import gomez.sistema.gestion.reservas.entities.Medico;
import gomez.sistema.gestion.reservas.error.AlertFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class PdfExporter {
    public static void exportarTablaMedicos(List<Medico> medicos, Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF (*.pdf)", "*.pdf"));
        fileChooser.setInitialFileName("lista_medicos.pdf");

        // Mostrar el diálogo y obtener el archivo seleccionado
        File archivoSeleccionado = fileChooser.showSaveDialog(stage);

        if (archivoSeleccionado != null) {
            try {
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(archivoSeleccionado));
                document.open();

                document.add(new Paragraph("Lista de Médicos"));
                document.add(new Paragraph(" ")); // espacio

                PdfPTable table = getPdfPTable(medicos);

                document.add(table);
                document.close();

                AlertFactory.mostrarInfo("PDF generado", "El archivo se guardó correctamente.");
            } catch (Exception e) {
                e.printStackTrace();
                AlertFactory.mostrarError("Error al exportar el PDF.");
            }
        }
    }

    private static PdfPTable getPdfPTable(List<Medico> medicos) {
        PdfPTable table = new PdfPTable(5); // 4 columnas
        table.setWidthPercentage(100);

        // Encabezados
        table.addCell("ID");
        table.addCell("Nombre");
        table.addCell("Especialidad");
        table.addCell("Teléfono");
        table.addCell("Horario");

        // Contenido
        for (Medico m : medicos) {
            table.addCell(String.valueOf(m.getId()));
            table.addCell(m.getNombre() + " " + m.getApellido());
            table.addCell(m.getEspecialidad().name());
            table.addCell(m.getTelefono());
            table.addCell(m.getHorarioInicio() + " - " + m.getHorarioFin());
        }
        return table;
    }
}
