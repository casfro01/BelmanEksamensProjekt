package dk.eksamensprojekt.belmaneksamensprojekt.BLL.util;

import java.util.regex.Pattern;

public class EmailValidator {
    private static final String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    private static final Pattern pattern = Pattern.compile(emailRegex);

    /**
     * Validates an email
     * @param email to be validated
     * @return returns true, if the email is valid, and false if the email is invalid
     */
    public static boolean validate(String email) {
        return pattern.matcher(email).matches();
    }
}
