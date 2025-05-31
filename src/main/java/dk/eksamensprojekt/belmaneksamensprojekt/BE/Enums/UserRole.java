package dk.eksamensprojekt.belmaneksamensprojekt.BE.Enums;

/**
 * Enum der repræsenterer brugerroller i systemet med korresponderende id'er
 */
public enum UserRole {
    ADMIN(1),
    QUALITY_ASSURANCE(2),
    OPERATOR(3);

    private int id;

    UserRole(int id) {
        this.id = id;
    }

    /**
     * Henter id'et for rollen.
     * @return Integer værdi for rollen.
     */
    public int toInt() {
        return id;
    }

    /**
     * Konverterer et integer id til den korresponderende brugerrolle.
     * @param id Integer id'et for rollen.
     * @return Korresponderende brugerrolle, hvis ingen matcher så returneres der null.
     */
    public static UserRole fromInt(int id) {
        return switch (id) {
            case 1 -> ADMIN;
            case 2 -> QUALITY_ASSURANCE;
            case 3 -> OPERATOR;
            default -> null;
        };
    }

    /**
     * Konverterer enum navet til et bruger-venligt format.
     * @return Formaterer rollenavnet til en tekst-streng
     */
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

    /**
     * Henter UserRole, der matcher en streng-repræsentation (case insensitive).
     * @param value Den streng-værdi, der skal matches.
     * @return Matchende brugerrolle eller null, hvis der ikke findes noget match.
     */
    public static UserRole valueOfString(String value){
        for (UserRole role : values()) {
            if (role.toString().equalsIgnoreCase(value)) {
                return role;
            }
        }
        return null;
    }
}
