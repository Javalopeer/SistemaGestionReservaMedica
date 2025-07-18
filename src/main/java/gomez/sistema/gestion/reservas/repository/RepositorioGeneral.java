package gomez.sistema.gestion.reservas.repository;

import java.util.List;

public interface RepositorioGeneral<T, ID> {
    List<T> listar();
    T buscar(ID t);
    void agregar(T t);
    void actualizar(T t);
    void eliminar(ID t);




}
