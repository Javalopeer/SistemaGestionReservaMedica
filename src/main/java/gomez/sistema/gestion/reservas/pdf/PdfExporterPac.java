package gomez.sistema.gestion.reservas.pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import gomez.sistema.gestion.reservas.entities.Paciente;
import gomez.sistema.gestion.reservas.error.AlertFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class PdfExporterPac {

    public static void exportarTablaPacientes(List<Paciente> pacientes, Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF (*.pdf)", "*.pdf"));
        fileChooser.setInitialFileName("lista_pacientes.pdf");

        // Mostrar el diálogo y obtener el archivo seleccionado
        File archivoSeleccionado = fileChooser.showSaveDialog(stage);

        if (archivoSeleccionado != null) {
            try {
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(archivoSeleccionado));
                document.open();

                document.add(new Paragraph("Lista de Pacientes"));
                document.add(new Paragraph(" ")); // espacio

                PdfPTable table = getPdfPTable(pacientes);

                document.add(table);
                document.close();

                AlertFactory.mostrarInfo("PDF generado", "El archivo se guardó correctamente.");
            } catch (Exception e) {
                e.printStackTrace();
                AlertFactory.mostrarError("Error al exportar el PDF.");
            }
        }
    }

    private static PdfPTable getPdfPTable(List<Paciente> pacientes) {
        PdfPTable table = new PdfPTable(4); // 4 columnas
        table.setWidthPercentage(100);

        // Encabezados
        table.addCell("Cedula");
        table.addCell("Nombre");
        table.addCell("Apellido");
        table.addCell("Teléfono");

        // Contenido
        for (Paciente m : pacientes) {
            table.addCell(m.getCedula());
            table.addCell(m.getNombre());
            table.addCell(m.getApellido());
            table.addCell(m.getTelefono());
        }
        return table;
    }
}
