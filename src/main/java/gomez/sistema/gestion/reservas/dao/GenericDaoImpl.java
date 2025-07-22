package gomez.sistema.gestion.reservas.dao;

import gomez.sistema.gestion.reservas.repository.GenericDao;
import gomez.sistema.gestion.reservas.sql.Database;

import java.sql.Connection;
import java.util.List;

public abstract class GenericDaoImpl<T> implements GenericDao<T> {

    protected Connection connection;

    public GenericDaoImpl(Connection connection) {
        this.connection = connection;
    }


    @Override
    public void insertar(T t) {

    }

    @Override
    public void actualizar(T t) {

    }

    @Override
    public void eliminar(int t) {

    }

    @Override
    public T buscar(T t) {
        return null;
    }

    @Override
    public List<T> obtenerTodos() {
        return List.of();
    }
}
