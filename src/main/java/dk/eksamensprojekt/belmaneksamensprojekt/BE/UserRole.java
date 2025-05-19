package dk.eksamensprojekt.belmaneksamensprojekt.BE;

import at.favre.lib.crypto.bcrypt.BCryptFormatter;

public enum UserRole {
    ADMIN(1),
    QUALITY_ASSURANCE(2),
    OPERATOR(3);

    private int id;

    UserRole(int id) {
        this.id = id;
    }

    public int toInt() {
        return id;
    }

    public static UserRole fromInt(int id) {
        return switch (id) {
            case 1 -> ADMIN;
            case 2 -> QUALITY_ASSURANCE;
            case 3 -> OPERATOR;
            default -> null;
        };
    }

    @Override
    public String toString(){
        String[] name = name().toLowerCase().split("_");
        StringBuilder roleName = new StringBuilder();
        for (String s : name) {
            roleName.append(s).append(" ");
        }
        roleName.replace(0, 1, roleName.substring(0, 1).toUpperCase());
        return roleName.toString();
    }
}
