package dk.eksamensprojekt.belmaneksamensprojekt.BE.Enums;

import java.util.Arrays;

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

    public static UserActions valueOfInt(int action) {
        UserActions[] actions = UserActions.values();
        // default action
        if (action > actions.length || action <= 0) {
            return actions[0];
        }
        return actions[action - 1];
    }

    public int toInt(){
        return id;
    }

    @Override
    public String toString() {
        String[] values = name().split("_");
        StringBuilder result = new StringBuilder();
        Arrays.stream(values).forEach(v -> result.append(v.toLowerCase()).append(" "));

        return result.toString().trim();
    }
}