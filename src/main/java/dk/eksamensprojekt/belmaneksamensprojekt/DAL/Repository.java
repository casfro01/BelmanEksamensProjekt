package dk.eksamensprojekt.belmaneksamensprojekt.DAL;

import java.sql.SQLException;
import java.util.List;

public interface Repository<T, ID>{
    List<T> getAll() throws Exception;
    T getById(ID id) throws Exception;

    T create(T entity) throws Exception;
    void update(T entity) throws Exception;
    void delete(T entity) throws Exception;
}
