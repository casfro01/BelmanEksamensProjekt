package dk.eksamensprojekt.belmaneksamensprojekt.BE;

public enum UserActions {
        Delete_Image(1),
        Take_Picture(2),
        Create_Report(3),
        Create_Edit_Report(4);

        private int id;

        UserActions(int id){
            this.id = id;
        }
    }

