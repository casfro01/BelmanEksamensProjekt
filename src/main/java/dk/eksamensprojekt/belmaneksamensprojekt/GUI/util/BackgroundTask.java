package dk.eksamensprojekt.belmaneksamensprojekt.GUI.util;

import javafx.concurrent.Task;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Denne klasse har til formål at lave en baggrundstråd, så man kan flytte noget ressourcekrævende væk fra GUI-tråden.
 */
public class BackgroundTask {

    /**
     * Udfør en række opgaver på en anden tråd
     * @param task Hovedopgaven som skal udgøres -> så som at hente ting fra en datakilde.
     * @param onSuccess Den opgave som skal udføres, hvis hovedopgaven lykkes
     *                  (dette er en consumer så den kan tage et input fra hovedopgaven).
     * @param onError Den opgave der skal udføres hvis noget fejler
     * @param <T> Den type som der arbejdes med. Dette kunne være en Be-entitet, liste el.lign.
     */
    public static <T> void execute(Supplier<T> task, Consumer<T> onSuccess, Consumer<Exception> onError) {
        execute(task, onSuccess, onError, null);
    }

    /**
     * Udfør en række opgaver på en anden tråd
     * @param task Hovedopgaven som skal udgøres -> så som at hente ting fra en datakilde.
     * @param onSuccess Den opgave som skal udføres, hvis hovedopgaven lykkes
     *                  (dette er en consumer så den kan tage et input fra hovedopgaven).
     * @param onError Den opgave der skal udføres hvis noget fejler
     * @param onLoading Den opgave som skal laves imens man venter på de andre opgaver færdiggøres.
     *                  (Dette er en boolean, så når der loades -> true, og når den er færdig -> false).
     * @param <T> Den type som der arbejdes med. Dette kunne være en Be-entitet, liste el.lign.
     */
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
