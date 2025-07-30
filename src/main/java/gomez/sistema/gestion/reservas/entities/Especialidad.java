package gomez.sistema.gestion.reservas.entities;

public enum Especialidad {

    Cirujia,
    Endocrinología,
    Cardiología,
    Dermatología,
    Nefrología,
    Ginecología,
    Pediatría,
    Oftalmología,
    Otorrinolaringología,
    Geriatria,
    Infectología,
    Gastroenterología,
    Fisioterapia,
    Neurología,
    Medicina_General,
    Psicologia,
    Traumatología,
    Urología,
    Psiquiatría,
    Nutrición,
    Ortopedia,
    Reumatología,
    Oncología,
    Neumología,
    Hematología;

    @Override
    public String toString() {
        return this.name().replace("_", " ");
    }
}
