package dk.eksamensprojekt.belmaneksamensprojekt.GUI.util;

import javafx.concurrent.Task;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class BackgroundTask {

    public static <T> void execute(Supplier<T> task, Consumer<T> onSuccess, Consumer<Exception> onError) {
        execute(task, onSuccess, onError, null);
    }

    public static <T> void execute(Supplier<T> task, Consumer<T> onSuccess, Consumer<Exception> onError, Consumer<Boolean> onLoading){
        // hvis der skal ske noget imens man venter
        if (onLoading != null)
            onLoading.accept(true);

        // opsættelse hovedopgaven
        Task<T> exeTask = new Task<>() {
            @Override
            protected T call() throws Exception {
                return task.get();
            }
        };

        // hvis eksekveringen lykkes
        exeTask.setOnSucceeded(event -> {
            T result = task.get();
            if (result != null)
                onSuccess.accept(result);
            else
                onError.accept(new Exception("Could not fetch data"));

            // afslut loading
            if (onLoading != null)
                onLoading.accept(false);
        });

        // hvis den fejler
        exeTask.setOnFailed(event -> {
            Throwable ex = exeTask.getException();
            if (ex instanceof Exception e)
                onError.accept(e);
            else
                onError.accept(new Exception("Unknown error", ex));

            // afslut loading
            if (onLoading != null)
                onLoading.accept(false);
        });

        // eksekvere tråden
        new Thread(exeTask).start();
    }
}
