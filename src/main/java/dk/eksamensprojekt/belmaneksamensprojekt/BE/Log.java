package dk.eksamensprojekt.belmaneksamensprojekt.BE;

import java.time.LocalDate;


public class Log{

    private int id;
    private int UserID ;
    private LocalDate dateTime;
    private int OrderID;

    public Log(int id, int UserID, LocalDate dateTime, int OrderID) {
        this.id = id;
        this.UserID = UserID;
        this.dateTime = dateTime;
        this.OrderID = OrderID;
    }

    public int getId() {
        return id;
    }

    public int getuserID() {
        return UserID;
    }

    public LocalDate getDateTime() {
        return dateTime;
    }

    public int getOrderID() {
        return OrderID;
    }
}

