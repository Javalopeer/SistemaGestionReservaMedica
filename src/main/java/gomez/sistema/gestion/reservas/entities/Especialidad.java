package gomez.sistema.gestion.reservas.entities;

public enum Especialidad {

    Cirujia,
    Endocrinologia,
    Cardiologia,
    Dermatologia,
    Nefrologia,
    Ginecologia,
    Pediatria,
    Oftalmologia,
    Otorrinolaringologia,
    Geriatria,
    Infectologia,
    Gastroenterologia,
    Fisioterapia,
    Neurologia,
    Medicina_General,
    Psicologia,
    Traumatologia,
    Urologia,
    Psiquiatria,
    Nutricion;

    @Override
    public String toString() {
        return this.name().replace("_", " ");
    }
}
