package dk.eksamensprojekt.belmaneksamensprojekt.BLL.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordHasherTest {
    @Test
    void hashPasswordCompare() throws Exception {
        PasswordHasher hasher = new PasswordHasher();
        String passwordOne = "one";
        String passwordTwo = "two";

        String hashedOne = hasher.hashString(passwordOne);
        String hashedTwo = hasher.hashString(passwordTwo);

        boolean actual1 = hashedOne.equals(hashedTwo);
        boolean expected1 = false;

        boolean actual2 = hashedOne.equals(hasher.hashString(passwordOne));
        boolean expected2 = true;

        Assertions.assertAll(
                () -> Assertions.assertEquals(expected1, actual1),
                () -> Assertions.assertEquals(expected2, actual2)
        );
    }
}