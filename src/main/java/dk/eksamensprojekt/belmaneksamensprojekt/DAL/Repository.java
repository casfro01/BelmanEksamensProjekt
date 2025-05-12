package dk.eksamensprojekt.belmaneksamensprojekt.DAL;

import dk.eksamensprojekt.belmaneksamensprojekt.BE.User;

import java.util.List;

public interface Repository<T, ID>{
    List<T> getAll() throws Exception;
    T getById(ID id) throws Exception;

    T create(T entity) throws Exception;
    User update(T entity) throws Exception;
    void delete(T entity) throws Exception;
}
