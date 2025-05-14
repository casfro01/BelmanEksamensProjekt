package dk.eksamensprojekt.belmaneksamensprojekt.GUI.Providers;

import dk.eksamensprojekt.belmaneksamensprojekt.GUI.WindowInvoker;

public class InvokerProvider {
    private static WindowInvoker invoker = null;

    public static WindowInvoker getInvoker() {
        if (invoker == null) {
            invoker = new WindowInvoker();
        }
        return invoker;
    }
}
