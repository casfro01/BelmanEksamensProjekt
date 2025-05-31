package dk.eksamensprojekt.belmaneksamensprojekt.BLL;

// Projekt imports
import dk.eksamensprojekt.belmaneksamensprojekt.BE.LoginUser;
import dk.eksamensprojekt.belmaneksamensprojekt.BE.User;
import dk.eksamensprojekt.belmaneksamensprojekt.BLL.util.EmailValidator;
import dk.eksamensprojekt.belmaneksamensprojekt.BLL.util.IHashing;
import dk.eksamensprojekt.belmaneksamensprojekt.BLL.util.PasswordHasher;
import dk.eksamensprojekt.belmaneksamensprojekt.DAL.Repository;
import dk.eksamensprojekt.belmaneksamensprojekt.DAL.UserDAO;
import dk.eksamensprojekt.belmaneksamensprojekt.DAL.UserData;

// Java imports
import java.util.List;

/**
 * Manager til at håndtere brugere(Users)
 */
public class UserManager {
    private final Repository<User, Integer> userDAO;
    private final IHashing passwordHashing;

    public UserManager() {
        this.userDAO = new UserDAO();
        this.passwordHashing = new PasswordHasher();
    }

    public List<User> getAllUsers() throws Exception {
        return userDAO.getAll();
    }

    /**
     * Login. Send email og password(not hashed) ind og hvis login lykkes, bliver den fulde bruger returneret.
     * @param email Brugerens email.
     * @param password Brugerens password
     * @return Hvis informationerne er korrekt så bliver den fulde bruger sendt tilbage, ellers bliver null returneret
     *
     * (TODO: Brude flyttes til LoginManager)
     */
    public User login(String email, String password) throws Exception {
        // validér emailen
        if (!EmailValidator.validate(email))
            throw new IllegalArgumentException("Invalid email address");

        // Tjekker om UserDAO kan håndtere login
        if (userDAO instanceof UserData uData){
            LoginUser loginUser = uData.getLoginUser(email);
            // Validér login
            if (loginUser != null && passwordHashing.compare(password, loginUser.getPassword()))
                return userDAO.getById(loginUser.getUserID()); // henter brugeren med id'et
            return null;
        }
        else
            throw new Exception("Login Failed: This Login type is not supported");
    }

    /**
     * Lav en bruger
     * @param user Bruger objektet med diverse informationer på (uden id)
     * @param loginUser Brugerens login oplysninger - hvis de findes
     * @return Den nyligt lavet bruger, med id fra databasen
     */
    public User create(User user, LoginUser loginUser) throws Exception {
        if (user.getName() == null || user.getName().isEmpty())
            throw new Exception("Name is required");
        if (user.getRole() == null)
            throw new Exception("Unknown role!");
        if (!EmailValidator.validate(user.getEmail()))
            throw new Exception("Email is invalid");
        if (loginUser.getPassword() == null || loginUser.getPassword().isEmpty())
            throw new Exception("Password not set");

        User newUser = userDAO.create(user); // gemmer bruger i database
        newUser.setImagePath(user.getImagePath());

        // gem login informationer
        if (userDAO instanceof UserData uData){
            LoginUser newLoginUser = new LoginUser(newUser.getId(), loginUser.getEmail(), passwordHashing.hashString(loginUser.getPassword()));
            uData.createLoginUser(newLoginUser);
        }

        return newUser;
    }

    public void update(User user) throws Exception {
        if (user.getRole() == null)
            throw new Exception("Role is required");
        userDAO.update(user);
    }
    public User getById(int id) throws Exception {
        return userDAO.getById(id);
    }
}

