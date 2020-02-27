/*
A class that will represent the piece King.
 */
public class King extends Figure{

    public King(String type, String color, char x, int y) {
        super(type, color, x, y);
    }

    public boolean move(char new_x, int new_y){
        // Check if position is on the board
        boolean onBoard = super.move(new_x, new_y);
        // Checks for the move and validate it by deciding if its valid or invalid.
        new_x = Character.toUpperCase(new_x);
        x = Character.toUpperCase(x);
        if(onBoard) {
            return (Character.getNumericValue(new_x) - Character.getNumericValue(x) <= 1 && new_y - y <= 1);
        }
        return false;
    }
}
