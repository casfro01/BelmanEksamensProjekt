package dk.eksamensprojekt.belmaneksamensprojekt.BE;

import dk.eksamensprojekt.belmaneksamensprojekt.BE.Enums.UserActions;

import java.time.LocalDate;


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

