package dk.eksamensprojekt.belmaneksamensprojekt.DAL;

import java.util.List;

public interface Repository<T, ID>{
    List<T> getAll();
    T getById(ID id);

    T create(T entity);
    void update(T entity);
    void delete(T entity);
}
