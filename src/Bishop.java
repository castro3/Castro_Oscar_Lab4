/*
A class that will represent the piece King.
 */
public class Bishop extends Figure {

    public Bishop(String type, String color, char x, int y) {
        super(type, color, x, y);
    }

    public boolean move(char new_x, int new_y){
        // Check if its on board.
        boolean onBoard = super.move(new_x, new_y);
        // Checks for the move and validate it by deciding if its valid or invalid.
        int abs_x = Math.abs(Character.getNumericValue(new_x)-
                Character.getNumericValue(x));
        int abs_y = Math.abs(new_y-y);
        if(onBoard) {
            return abs_x == abs_y;
        }
        return false;
    }
}
