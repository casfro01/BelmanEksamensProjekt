package dk.eksamensprojekt.belmaneksamensprojekt.BLL.util;

import java.util.regex.Pattern;

public class EmailValidator {
    // Regex
    private static final String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    private static final Pattern pattern = Pattern.compile(emailRegex);

    /**
     * Validerer en email.
     * @param email some skal valideres.
     * @return returnerer true, hvis emailen er valid, og false hvis emailen er invalid.
     */
    public static boolean validate(String email) {
        // bruger regex til at se og emailen er accepteret ift. vores standarder.
        return pattern.matcher(email).matches();
    }
}
