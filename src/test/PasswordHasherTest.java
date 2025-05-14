import dk.eksamensprojekt.belmaneksamensprojekt.BLL.util.PasswordHasher;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PasswordHasherTest {
    private final PasswordHasher hasher = new PasswordHasher();

    @Test
    void testCorrectPasswordMatchesHash() throws Exception {
        String password = "one";
        String hashed = hasher.hashString(password);
        Assertions.assertTrue(hasher.compare(password, hashed));
    }

    @Test
    void testIncorrectPasswordFailsHash() throws Exception {
        String password = "one";
        String hashedOther = hasher.hashString("two");
        Assertions.assertFalse(hasher.compare(password, hashedOther));
    }

    @Test
    void testEmptyPassword() throws Exception {
        String password = "";
        String hashed = hasher.hashString(password);
        Assertions.assertTrue(hasher.compare(password, hashed));
    }

    @Test
    void testPasswordWithSpecialCharacters() throws Exception {
        String password = "!@#$%^&*()_+{}|:\"<>?";
        String hashed = hasher.hashString(password);
        Assertions.assertTrue(hasher.compare(password, hashed));
    }

    @Test
    void testVeryLongPasswordThrowsException1() throws Exception {
        String password = "a".repeat(71);
        String hashed = hasher.hashString(password);
        Assertions.assertTrue(hasher.compare(password, hashed));
    }

    @Test
    void testPasswordWithUnicodeCharacters() throws Exception {
        String password = "パスワード😊";
        String hashed = hasher.hashString(password);
        Assertions.assertTrue(hasher.compare(password, hashed));
    }

    @Test
    void testNullPasswordHashingThrowsException() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            hasher.hashString(null);
        });
    }

    @Test
    void testNullPasswordComparisonThrowsException() throws Exception {
        String hashed = hasher.hashString("test");
        Assertions.assertThrows(NullPointerException.class, () -> {
            hasher.compare(null, hashed);
        });

        Assertions.assertThrows(NullPointerException.class, () -> {
            hasher.compare("test", null);
        });
    }
}
