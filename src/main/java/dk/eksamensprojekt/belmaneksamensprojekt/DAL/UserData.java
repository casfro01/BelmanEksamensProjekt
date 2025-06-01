package dk.eksamensprojekt.belmaneksamensprojekt.DAL;

import dk.eksamensprojekt.belmaneksamensprojekt.BE.LoginUser;

/**
 * Interface med henblik at opdatere/hente forskellige bruger data.
 * Heriblandt login-oplysninger.
 */
public interface UserData {
    /**
     * Hent login-oplysninger på en bruger baseret på email.
     * @param email Den email som brugeren har brugt til deres profil.
     * @return Et objekt med login-oplysninger, så som password og email.
     */
    LoginUser getLoginUser(String email) throws Exception;

    /**
     * Lav login-oplysninger til en bruger (email og password)
     */
    void createLoginUser(LoginUser loginUser) throws Exception;
}
