package dk.eksamensprojekt.belmaneksamensprojekt.GUI.Model;

import dk.eksamensprojekt.belmaneksamensprojekt.BE.User;
import dk.eksamensprojekt.belmaneksamensprojekt.BLL.UserManager;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class UserModel {
    private UserManager userManager;
    private ObservableList<User> users;
    private SimpleObjectProperty<User> selectedUser;

    public UserModel() {
        this.userManager = new UserManager();

    }
    private List<User> loadUsers() throws Exception {
        return userManager.getAllUsers();
    }
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

    public void setSelectedUser(User selectedUser) {
        if (this.selectedUser == null) {
            this.selectedUser = new SimpleObjectProperty<>(selectedUser);
        }
        // TODO : hmmm refactor nedenfor ?
        else{
            this.selectedUser.set(selectedUser);
        }
        this.selectedUser.setValue(selectedUser);
    }

    public User login(String email, String password) throws Exception {
        return userManager.login(email, password);
    }
}
