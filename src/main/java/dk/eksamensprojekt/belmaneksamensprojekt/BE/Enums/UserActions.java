package dk.eksamensprojekt.belmaneksamensprojekt.BE.Enums;

import java.util.Arrays;

/**
 * Enum som beskriver forskellige handlinger som brugeren kan foretage sig.
 */
public enum UserActions {
    DEFAULT(0),
    DELETE_PICTURE(1),
    TAKE_PICTURE(2),
    CREATE_REPORT(3),
    APPROVED_PICTURE(4),
    REJECT_PICTURE(5),
    APPROVED_ORDER(6),
    REJECT_ORDER(7),
    EDIT_ORDER(8);

    private int id;

    UserActions(int id){
        this.id = id;
    }

    /**
     * Finder en brugerhandling baseret på en integer.
     */
    public static UserActions valueOfInt(int action) {
        UserActions[] actions = UserActions.values();
        // default action, hvis den er 0 eller udenfor rækkevidden.
        if (action > actions.length || action <= 0) {
            return actions[0];
        }
        return actions[action - 1];
    }

    /**
     * Oversætter enum-værdien til en integer.
     */
    public int toInt(){
        return id;
    }

    @Override
    public String toString() {
        // Fordi der bruges '_' så kan denne bruges for at splitte ordene. Hvorefter man kan sætte dem sammen -
        // så det bliver mere læseligt for brugeren.
        String[] values = name().split("_");
        // bygger tekststrengen ud fra ord-arrayen og laver det til en samhørig string.
        StringBuilder result = new StringBuilder();
        Arrays.stream(values).forEach(v -> result.append(v.toLowerCase()).append(" "));

        return result.toString().trim();
    }
}