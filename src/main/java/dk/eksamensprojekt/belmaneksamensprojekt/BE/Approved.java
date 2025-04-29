package dk.eksamensprojekt.belmaneksamensprojekt.BE;

public enum Approved {
    Approved(1),
    NotApproved(0),
    NotReviewed(2);

    private int id;

    Approved(int id){
        this.id = id;
    }

    public boolean toBoolean(){
        return id == 1;
    }
}
