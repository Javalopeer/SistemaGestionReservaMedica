package gomez.sistema.gestion.reservas.repository;

import java.util.List;

public interface GenericDao<T> {
    void insertar(T t);
    void actualizar(T t);
    void eliminar(int t);
    T buscar(T t);
    List<T> obtenerTodos();


}
