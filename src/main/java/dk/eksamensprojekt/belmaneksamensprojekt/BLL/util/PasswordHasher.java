package dk.eksamensprojekt.belmaneksamensprojekt.BLL.util;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class PasswordHasher implements IHashing {
    /**
     * Hasher et input med BCrypt.
     * @return Det hashed input som streng-format.
     */
    @Override
    public String hashString(String input) {
        return BCrypt.withDefaults().hashToString(12, input.toCharArray());
    }

    /**
     * Sammenlinger et non-hash med et hashed hash
     * @param input En streng-værdi som ikke er hashed
     * @param hash Et hash som streng-format.
     * @return Hvis inputtet(et ikke hash) og hash(et hash) passer med hinanden så returneres der true, hvis ikke så false.
     */
    @Override
    public boolean compare(String input, String hash) {
        return BCrypt.verifyer().verify(input.toCharArray(), hash.toCharArray()).verified;
    }
}
