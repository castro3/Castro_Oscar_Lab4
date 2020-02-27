/*
A class that will represent the piece King.
 */
public class Pawn extends Figure {

    public Pawn(String type, String color, char x, int y) {
        super(type, color, x, y);
    }

    public boolean move(char new_x, int new_y){ // Pawn movement has some errors
        // Check if its on board.
        boolean onBoard = super.move(new_x, new_y);
        // Checks for the move and validate it by deciding if its valid or invalid.
        new_x = Character.toUpperCase(new_x);
        x = Character.toUpperCase(x);
        if(onBoard) {
            if(y == 2){
                return (x == new_x && new_y - y == 2);
            }else {
                return (x == new_x && new_y - y == 1);
            }
        }
        return false;
    }
}
