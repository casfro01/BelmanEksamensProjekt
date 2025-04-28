import at.favre.lib.crypto.bcrypt.BCrypt;
import dk.eksamensprojekt.belmaneksamensprojekt.BLL.util.PasswordHasher;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PasswordHasherTest {
    @Test
    void hashPasswordCompare() throws Exception {
        PasswordHasher hasher = new PasswordHasher();
        String passwordOne = "one";
        String passwordTwo = "two";

        String hashedOne = hasher.hashString(passwordOne);
        String hashedTwo = hasher.hashString(passwordTwo);

        boolean actual1 = hasher.compare(passwordOne, hashedOne);
        boolean expected1 = true;

        boolean actual2 = hasher.compare(passwordOne, hashedTwo);
        boolean expected2 = false;

        Assertions.assertAll(
                () -> Assertions.assertEquals(expected1, actual1),
                () -> Assertions.assertEquals(expected2, actual2)
        );
    }
}