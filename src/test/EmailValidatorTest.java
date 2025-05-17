import dk.eksamensprojekt.belmaneksamensprojekt.BLL.util.EmailValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class EmailValidatorTest {

    @Test
    @DisplayName("Valid emails")
    void validEmails(){
        String email1 = "john.doe@example.com";
        String email2 = "user_name123@sub.domain.co.uk";
        String email3 = "email123@domain.net";
        String email4 = "first-last@my-site.org";
        String email5 = "a.b.c.d@letters.io";
        String email6 = "user@domain.info";

        boolean expected = true;

        boolean email1Res = EmailValidator.validate(email1);
        boolean email2Res = EmailValidator.validate(email2);
        boolean email3Res = EmailValidator.validate(email3);
        boolean email4Res = EmailValidator.validate(email4);
        boolean email5Res = EmailValidator.validate(email5);
        boolean email6Res = EmailValidator.validate(email6);

        Assertions.assertAll(
                () -> Assertions.assertEquals(expected, email1Res),
                () -> Assertions.assertEquals(expected, email2Res),
                () -> Assertions.assertEquals(expected, email3Res),
                () -> Assertions.assertEquals(expected, email4Res),
                () -> Assertions.assertEquals(expected, email5Res),
                () -> Assertions.assertEquals(expected, email6Res)
        );
    }

    @Test
    @DisplayName("Invalid emails")
    void invalidEmails(){
        String email1 = "plainaddress"; // mangler @
        String email2 = "@missingusername.com"; // mangler front
        String email3 = "username@.com"; // mangler domæne
        String email4 = "user@@domain.com"; // dobbelt @
        String email5 = "user@domain..com"; // dobbelt punktum
        String email6 = "user@domain.toolongtld"; // for lang top-level domæne

        boolean expected = false;

        boolean email1Res = EmailValidator.validate(email1);
        boolean email2Res = EmailValidator.validate(email2);
        boolean email3Res = EmailValidator.validate(email3);
        boolean email4Res = EmailValidator.validate(email4);
        boolean email5Res = EmailValidator.validate(email5);
        boolean email6Res = EmailValidator.validate(email6);

        Assertions.assertAll(
                () -> Assertions.assertEquals(expected, email1Res),
                () -> Assertions.assertEquals(expected, email2Res),
                () -> Assertions.assertEquals(expected, email3Res),
                () -> Assertions.assertEquals(expected, email4Res),
                () -> Assertions.assertEquals(expected, email5Res),
                () -> Assertions.assertEquals(expected, email6Res)
        );
    }
}
