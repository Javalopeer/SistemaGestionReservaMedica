package gomez.sistema.gestion.reservas.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public enum EspecialidadConMonto {

    Cirujia("Cirujia", 50000),
    Endocrinologia("Endocrinologia", 30000),
    Cardiologia("Cardiologia", 40000),
    Dermatologia("Dermatologia", 25000),
    Nefrologia("Nefrologia", 35000),
    Ginecologia("Ginecologia", 28000),
    Pediatria("Pediatria", 22000),
    Oftalmologia("Oftalmologia", 27000),
    Otorrinolaringologia("Otorrinolaringologia", 26000),
    Geriatria("Geriatria", 24000),
    Infectologia("Infectologia", 32000),
    Gastroenterologia("Gastroenterologia", 33000),
    Fisioterapia("Fisioterapia", 15000),
    Neurologia("Neurologia", 45000),
    Medicina_General("Medicina_General", 18000),
    Psicologia("Psicologia", 20000),
    Traumatologia("Traumatologia", 37000),
    Urologia("Urologia", 29000),
    Psiquiatria("Psiquiatria", 34000),
    Nutricion("Nutricion", 16000),

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


