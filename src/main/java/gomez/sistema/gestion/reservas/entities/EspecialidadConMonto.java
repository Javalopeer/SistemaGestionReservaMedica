package gomez.sistema.gestion.reservas.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public enum EspecialidadConMonto {

    Cirujia("Cirujia", 50000),
    Endocrinología("Endocrinología", 30000),
    Cardiología("Cardiología", 40000),
    Dermatología("Dermatología", 25000),
    Nefrología("Nefrología", 35000),
    Ginecología("Ginecología", 28000),
    Pediatría("Pediatría", 22000),
    Oftalmología("Oftalmología", 27000),
    Otorrinolaringología("Otorrinolaringología", 26000),
    Geriatria("Geriatria", 24000),
    Infectología("Infectología", 32000),
    Gastroenterología("Gastroenterología", 33000),
    Fisioterapia("Fisioterapia", 15000),
    Neurología("Neurología", 45000),
    Medicina_General("Medicina_General", 18000),
    Psicologia("Psicologia", 20000),
    Traumatología("Traumatología", 37000),
    Urología("Urología", 29000),
    Psiquiatría("Psiquiatría", 34000),
    Nutricion("Nutrición", 16000),
    Ortopedia("Ortopedia", 29000),
    Reumatología("Reumatología", 16700),
    Oncología("Oncología", 16780),
    Neumología("Neumología", 129000),
    Hematología("Hematología", 15000),
    Desconocida("Desconocida", 20000);

    private String nombre;
    private double monto;

    EspecialidadConMonto(String nombre, double monto) {
        this.nombre = nombre;
        this.monto = monto;
    }

    public static double obtenerMontoPorNombre(String nombre) {
        for (EspecialidadConMonto e : values()) {
            if (e.nombre.equalsIgnoreCase(nombre)) {
                return e.monto;
            }
        }
        return Desconocida.getMonto();
    }
}


