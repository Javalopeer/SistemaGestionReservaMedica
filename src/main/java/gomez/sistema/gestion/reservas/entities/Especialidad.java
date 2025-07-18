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
    Geriatria;

    @Override
    public String toString() {
        return this.name();
    }
}
