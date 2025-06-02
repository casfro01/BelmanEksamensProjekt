package dk.eksamensprojekt.belmaneksamensprojekt.GUI.Model;

// Projekt imports
import dk.eksamensprojekt.belmaneksamensprojekt.BE.LoginUser;
import dk.eksamensprojekt.belmaneksamensprojekt.BE.User;
import dk.eksamensprojekt.belmaneksamensprojekt.BLL.UserManager;

// JavaFX
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

// Java
import java.util.List;

/**
 * Denne Model håndterer {@link User}, og fungerer som mellemmand mellem BLL og GUI.
 * Denne model indeholder også en cached-liste med {@link User}.
 */
public class UserModel {
    private UserManager userManager;
    private ObservableList<User> users;
    private SimpleObjectProperty<User> selectedUser;

    public UserModel() {
        this.userManager = new UserManager();
        selectedUser = new SimpleObjectProperty<>(null);
    }
    private List<User> loadUsers() throws Exception {
        return userManager.getAllUsers();
    }

    /**
     * Hent {@link ObservableList<User>} fra datakilden med {@link User}, altså hvis den cached-liste er tom.
     */
    public ObservableList<User> getUsers() throws Exception {
        if (users == null) {
            users = FXCollections.observableArrayList();
            users.addAll(loadUsers());
        }
        return users;
    }

    public SimpleObjectProperty<User> getSelectedUser() {
        return selectedUser;
    }

    /**
     * Sæt der bliver som er logget ind
     * @param selectedUser Den {@link User} som skal være logget ind.
     */
    public void setSelectedUser(User selectedUser) {
        // TODO : hmmm refactor ? -> i form af en item klasse som holder properties i stedet?
        if (this.selectedUser == null) {
            this.selectedUser = new SimpleObjectProperty<>(selectedUser);
        }
        else{
            this.selectedUser.set(selectedUser);
        }
        this.selectedUser.setValue(selectedUser);
    }

    /**
     * Tjekker login.
     * @param email Den email som er forbundet med {@link User}
     * @param password Det password som er forbundet med {@link User}
     * @return Hvis login oplysningerne passer, så bliver den bruger({@link User}) returneret, hvis ikke kommer null tilbage
     */
    public User login(String email, String password) throws Exception {
        return userManager.login(email, password);
    }

    /**
     * Opdater {@link User} informationer.
     */
    public void updateUser(User user) throws Exception {
        userManager.update(user);
    }

    /**
     * Lav en ny bruger
     * @param baseUser bruger med basis detaljer, så som profilbillede, navn og email
     * @param loginDetails brugerens login detaljer, så som email og password
     */
    public void createUser(User baseUser, LoginUser loginDetails) throws Exception {
        User newUser = userManager.create(baseUser, loginDetails);
        users.add(newUser);
    }
}
