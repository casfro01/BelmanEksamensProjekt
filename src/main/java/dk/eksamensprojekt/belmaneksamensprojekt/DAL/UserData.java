package dk.eksamensprojekt.belmaneksamensprojekt.DAL;

import dk.eksamensprojekt.belmaneksamensprojekt.BE.LoginUser;

import java.sql.SQLException;

public interface UserData {
    LoginUser getLoginUser(String email) throws Exception;
    void createLoginUser(LoginUser loginUser) throws Exception;
}
