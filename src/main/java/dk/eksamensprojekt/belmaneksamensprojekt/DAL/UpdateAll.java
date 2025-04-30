package dk.eksamensprojekt.belmaneksamensprojekt.DAL;

import java.util.List;

public interface UpdateAll<T>{
    void updateAll(List<T> items) throws Exception;
}
