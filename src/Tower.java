/*
A class that will represent the piece King.
 */
public class Tower extends Figure{

    public Tower(String type, String color, char x, int y) {
        super(type, color, x, y);
    }

    public boolean move(char new_x, int new_y){
        // Check if its on board.
        boolean onBoard = super.move(new_x, new_y);
        // Checks for the move and validate it by deciding if its valid or invalid.
        new_x = Character.toUpperCase(new_x);
        x = Character.toUpperCase(x);
        if(onBoard){
            return (new_x == x ^ new_y == y);
        }
        return false;
    }
}
