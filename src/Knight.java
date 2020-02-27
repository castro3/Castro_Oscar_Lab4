/*
A class that will represent the piece King.
 */
public class Knight extends Figure{

    public Knight(String type, String color, char x, int y) {
        super(type, color, x, y);
    }

    public boolean move(char new_x, int new_y){
        // Check if its on board.
        boolean onBoard = super.move(new_x, new_y);
        // Checks for the move and validate it by deciding if its valid or invalid.
        new_x = Character.toUpperCase(new_x);
        x = Character.toUpperCase(x);
        int abs_x = Math.abs(Character.getNumericValue(new_x)-
                Character.getNumericValue(x));
        int abs_y = Math.abs(new_y-y);
        if(onBoard){
            return (abs_x == 1 && abs_y == 2) || ((abs_x == 2 && abs_y == 1));
        }
        return false;
    }
}
