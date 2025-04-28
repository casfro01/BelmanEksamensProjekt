package dk.eksamensprojekt.belmaneksamensprojekt.BLL.util;


import at.favre.lib.crypto.bcrypt.BCrypt;

public class PasswordHasher implements IHashing {
    @Override
    public String hashString(String input) throws Exception {
        return BCrypt.withDefaults().hashToString(12, input.toCharArray());
    }

    @Override
    public boolean compare(String input, String hash) throws Exception {
        return hashString(input).equals(hash);
    }


}
