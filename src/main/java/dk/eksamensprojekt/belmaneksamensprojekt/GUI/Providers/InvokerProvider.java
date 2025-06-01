package dk.eksamensprojekt.belmaneksamensprojekt.GUI.Providers;

import dk.eksamensprojekt.belmaneksamensprojekt.GUI.WindowInvoker;

/**
 * Denne klasse har det formål at stille en invoker til rådighed -> deraf er det en provider.
 */
public class InvokerProvider {
    private static WindowInvoker invoker = null;

    /**
     * Hent en invoker som kan udføre kommandoer.
     */
    public static WindowInvoker getInvoker() {
        if (invoker == null) {
            invoker = new WindowInvoker();
        }
        return invoker;
    }
}
