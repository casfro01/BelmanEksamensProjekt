package dk.eksamensprojekt.belmaneksamensprojekt.GUI;

public class ModelManager {
    private static ModelManager instance;
    // models
    private ModelManager(){
    }

    public static ModelManager getInstance(){
        if(instance == null){
            instance = new ModelManager();
        }
        return instance;
    }

    // getters and setters
}
