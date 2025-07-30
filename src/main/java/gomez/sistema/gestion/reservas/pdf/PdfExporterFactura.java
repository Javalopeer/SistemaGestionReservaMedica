package gomez.sistema.gestion.reservas.pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import gomez.sistema.gestion.reservas.entities.Factura;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;

public class PdfExporterFactura {

    public static void exportarFactura(Factura factura) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar factura como PDF");
        fileChooser.setInitialFileName("Factura_" + factura.getIdFactura() + ".pdf");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivo PDF", "*.pdf"));
        File archivo = fileChooser.showSaveDialog(new Stage());

        if (archivo != null) {
            try {
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(archivo));
                document.open();

                document.add(new Paragraph("Factura #" + factura.getIdFactura(), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16)));
                document.add(new Paragraph(" "));
                document.add(new Paragraph("Paciente: " + factura.getCita().getPaciente().getNombre()));
                document.add(new Paragraph("Médico: " + factura.getCita().getMedico().getNombre()));
                document.add(new Paragraph("Especialidad: " + factura.getCita().getMedico().getEspecialidad()));
                document.add(new Paragraph("Fecha de emisión: " + factura.getFechaEmision()));
                document.add(new Paragraph("Monto: ₡" + factura.getMonto()));
                document.add(new Paragraph(" "));

                document.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
