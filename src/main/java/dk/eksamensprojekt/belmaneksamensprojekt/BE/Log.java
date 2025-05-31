package dk.eksamensprojekt.belmaneksamensprojekt.BE;

import dk.eksamensprojekt.belmaneksamensprojekt.BE.Enums.UserActions;
import java.time.LocalDate;

/**
 * Log klasse som indeholder informationer om hvad en bruger har foretaget sig.
 * Disse informationer er hvem, hvad og hvornår -> på en specifik ordre.
 * Dette hjælper med at "back-tracking" for at placere ansvar.
 *
 * Dog er det et godt spørgsmål, hvorvidt denne slags overvågning skal bruges og hvorvidt det er tilladt.
 */
public class Log{

    private int id;
    private BaseUser user;
    private LocalDate dateTime;
    private BaseOrder order;
    private UserActions action;

    public Log(int id, BaseUser user, UserActions action, LocalDate dateTime, BaseOrder order) {
        this.id = id;
        this.user = user;
        this.action = action;
        this.dateTime = dateTime;
        this.order = order;
    }

    public int getId() {
        return id;
    }

    public BaseUser getUser() {
        return user;
    }

    public LocalDate getDateTime() {
        return dateTime;
    }

    public BaseOrder getOrder() {
        return order;
    }

    public UserActions getAction() {
        return action;
    }
}

