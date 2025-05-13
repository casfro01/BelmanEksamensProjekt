package dk.eksamensprojekt.belmaneksamensprojekt.BE;

import java.sql.Date;
import java.time.LocalDateTime;

public class Log{

    private int id;
    private int UserID ;
    private Date dateTime;
    private int OrderID;

    public Log(int id, int UserID, Date dateTime, int OrderID) {
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

    public Date getDateTime() {
        return dateTime;
    }

    public int getOrderID() {
        return OrderID;
    }
}

